package rtkay;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtkay.lex.DialogAction;
import com.rtkay.lex.LexQuery;
import com.rtkay.lex.LexRespond;
import com.rtkay.lex.Message;
import com.rtkay.model.Joke;
import com.rtkay.model.JokeList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import static com.rtkay.lex.LexQueryFactory.createLexQuery;

public class GetDadJoke implements RequestHandler<Map<String, Object>, Object> {
    private static final String BASE_URL = "https://icanhazdadjoke.com/";
    private Message message = new Message("PlainText");

    @Override
    public Object handleRequest(Map<String, Object> stringObjectMap, Context context) {
        LexQuery lexQuery = createLexQuery(stringObjectMap);
        String output = "";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        JokeAPI service = retrofit.create(JokeAPI.class);
        String word = lexQuery.getSubject();
        if (word == null) {
            Call<Joke> call = service.getRandomJoke();
            try {
                Response<Joke> response = call.execute();
                if (response.isSuccessful()) {
                    Joke joke = response.body();
                    output = joke.getJoke();
                } else {
                    output = "I can't think of anything funny right now. Let's try again later?";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String searchTerm = word;
            Call<JokeList> call = service.getJokeBySubject(searchTerm, "1", 1);
            try {
                Response<JokeList> response = call.execute();
                if (response.isSuccessful()) {
                    JokeList myJokes = response.body();
                    int totalNumberOfJokes = myJokes.getTotal_jokes();
                    if (totalNumberOfJokes == 0) {
                        String newSearchTerm = Inflector.getInstance().singularize(searchTerm);
                        Call<JokeList> filter = service.getJokeBySubject(newSearchTerm, "1", 1);
                        Response<JokeList> newResponse = filter.execute();
                        if (newResponse.isSuccessful()) {
                            JokeList jokeList = newResponse.body();
                            int totalJokes = jokeList.getTotal_jokes();
                            if (totalJokes == 0) {
                                output = "I don't have anything funny to say about " + searchTerm + " right now.";
                            } else {
                                String index = Integer.toString(new Random().nextInt(totalJokes) + 1);
                                Call<JokeList> call1 = service.getJokeBySubject(newSearchTerm, index, 1);
                                Response<JokeList> response1 = call1.execute();
                                if (response1.isSuccessful()) {
                                    JokeList jokes = response1.body();
                                    output = jokes.getResults().get(0).getJoke();
                                } else {
                                    output = "I don't have anything funny to say about " + searchTerm + " right now.";
                                }
                            }
                        } else {
                            output = "I don't have anything funny to say about " + searchTerm + " right now.";
                        }
                    } else {
                        String index = Integer.toString(new Random().nextInt(totalNumberOfJokes) + 1);
                        Call<JokeList> call1 = service.getJokeBySubject(searchTerm, index, 1);
                        Response<JokeList> response1 = call1.execute();
                        if (response1.isSuccessful()) {
                            JokeList jokes = response1.body();
                            output = jokes.getResults().get(0).getJoke();
                        }
                    }
                } else {
                    output = "I don't have anything funny to say about " + searchTerm + " right now.";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        message.setContent(output);
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexRespond(dialogAction);
    }


}
