package com.rtkay;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtkay.lex.DialogAction;
import com.rtkay.lex.LexQuery;
import com.rtkay.lex.LexRespond;
import com.rtkay.lex.Message;
import com.rtkay.model.definition.EntryResult;
import com.rtkay.model.definition.Inflector;
import com.rtkay.model.definition.Sense;
import com.rtkay.model.sentences.Example;
import com.rtkay.model.sentences.UseInSentence;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.rtkay.lex.LexQueryFactory.createLexQuery;


public class Oxford implements RequestHandler<Map<String, Object>, Object> {
    private String BASE_URL = "https://od-api.oxforddictionaries.com/api/v2/";
    private Message message = new Message("PlainText");
    private List<String> responses;
    private List<String> responsesSecondary;
    String output = "";


    @Override
    public Object handleRequest(Map<String, Object> stringObjectMap, Context context) {
        responses = new ArrayList<>();
        responsesSecondary = new ArrayList<>();
        LexQuery lexQuery = createLexQuery(stringObjectMap);
        String word = lexQuery.getWord();
        responses.add("Here's what I found on " + word + ".");
        responses.add(word + " means");
        responsesSecondary.add("It can also mean");
        responsesSecondary.add("Another meaning of it is");
        String intentName = lexQuery.getIntentName();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        OxfordAPI service = retrofit.create(OxfordAPI.class);
        if (intentName.matches("use_in_sentence")) {
            Call<UseInSentence> resultCall = service.getSentence("en-gb", word);
            try {
                getSentence(resultCall);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (intentName.matches("definitions")) {
            Call<EntryResult> resultCall = service.getDefinition("en-gb", word);
            Call<EntryResult> resultCalls = service.getDefinition("en-gb", Inflector.getInstance().singularize(word));
            try {
                if (!getDefinition(resultCall)) {
                    if (!getDefinition(resultCalls)) {
                        output = "I'm sorry, I couldn't find anything on " + word;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        message.setContent(output);
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexRespond(dialogAction);
    }

    private void getSentence(Call<UseInSentence> resultCall) throws IOException {
        Response<UseInSentence> exampleResponse = resultCall.execute();
        UseInSentence finals = exampleResponse.body();
        System.out.println(resultCall.request().url().toString());
        if (exampleResponse.isSuccessful()) {
            List<Example> exampleList = finals.getResults().get(0).getLexicalEntries().get(0).getEntries().get(0).getSenses().get(0).getExamples();
            int results = exampleList.size();
            output = exampleList.get(0).getText();
            if (results > 1) {
                String second = exampleList.get(1).getText();
                output = output + ". You can also say: " + second + ".";
                output = output.substring(0, 1).toUpperCase() + output.substring(1);
            }

        } else {
            System.out.println(exampleResponse.errorBody().string());
            output = "I couldn't find anything";
        }

    }

    private boolean getDefinition(Call<EntryResult> resultCall) throws IOException {
        Response<EntryResult> response = resultCall.execute();
        EntryResult finals = response.body();
        if (response.isSuccessful()) {
            List<Sense> getSenses = finals.getResults().get(0).getLexicalEntries().get(0).getEntries().get(0).getSenses();
            int results = getSenses.size();
            String first = getSenses.get(0).getDefinitions().get(0);
            output = getRandomElement(responses) + " " + first;
            output = output.substring(0, 1).toUpperCase() + output.substring(1);
            if (results > 1) {
                String second = getSenses.get(1).getDefinitions().get(0);
                output = getRandomElement(responses) + " " + first + ". " + getRandomElement(responsesSecondary) + " " + second + ".";
                output = output.substring(0, 1).toUpperCase() + output.substring(1);
            }
            return true;
        } else {
            return false;
        }

    }

    private String getRandomElement(List<String> responses) {
        Random rand = new Random();
        return responses.get(rand.nextInt(responses.size()));
    }
}
