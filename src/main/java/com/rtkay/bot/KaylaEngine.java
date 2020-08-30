package com.rtkay.bot;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lexruntime.AmazonLexRuntime;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;
import com.amazonaws.services.lexruntime.model.PostContentRequest;
import com.amazonaws.services.lexruntime.model.PostContentResult;
import com.amazonaws.services.lexruntime.model.PostTextRequest;
import com.amazonaws.services.lexruntime.model.PostTextResult;
import com.rtkay.audio.AudioPlayer;
import java.io.InputStream;

public abstract class KaylaEngine {
    private static final AmazonLexRuntime client = AmazonLexRuntimeClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
    public static final PostTextRequest textRequest = new PostTextRequest();
    public static final PostContentRequest contentRequest = new PostContentRequest();
    private static PostTextResult textResult;
    private static PostContentResult contentResult;
    private static final int TEXT_INPUT =0;
    private static final int VOICE_INPUT =1;
    public static boolean SESSION = true;


    public static PostTextResult getTextResult() {
        return textResult;
    }

    public static PostContentResult getContentResult() {
        return contentResult;
    }


    private static void initBot() {
        textRequest.setBotName(botName());
        textRequest.setBotAlias(botAlias());
        textRequest.setUserId(userID());

        contentRequest.setBotAlias(botAlias());
        contentRequest.setBotName(botName());
        contentRequest.setUserId(userID());
        contentRequest.setContentType(contentType());
    }
    private static String contentType() {
        return "audio/x-l16; sample-rate=16000";
    }

    private static String botName() {
        return "Kayla";
    }

    private static String botAlias() {
        return "kayla";
    }

    private static String userID() {
        return "fromIDE";
    }

    public static void BuildKayla() {
        initBot();
    }
    public static void sendText(String requestText) {
        textRequest.setInputText(requestText);
        textResult = client.postText(textRequest);
        displayResult(TEXT_INPUT);
    }

    public static void sendVoice(InputStream streamFromMic) {
        contentRequest.setInputStream(streamFromMic);
        System.out.println("posting......................................");
        contentResult = client.postContent(contentRequest);
        System.out.println("POSTED......................................");
        displayResult(VOICE_INPUT);
    }

    private static void displayResult(int contentType) {
        switch (contentType) {
            case TEXT_INPUT:
                System.out.println(textResult.getMessage());
                break;
            case VOICE_INPUT:
                System.out.println(contentResult.getMessage());
                AudioPlayer.output(contentResult.getAudioStream());
                break;
        }
    }
}
