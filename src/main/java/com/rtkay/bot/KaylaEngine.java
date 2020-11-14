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

public class KaylaEngine {
    private final AmazonLexRuntime client = AmazonLexRuntimeClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
    public final PostTextRequest textRequest = new PostTextRequest();
    public  final PostContentRequest contentRequest = new PostContentRequest();
    private  PostTextResult textResult;
    private  PostContentResult contentResult;
    private static final int TEXT_INPUT =0;
    private static final int VOICE_INPUT =1;

    public KaylaEngine() {
        textRequest.setBotName(botName());
        textRequest.setBotAlias(botAlias());
        textRequest.setUserId(userID());
        contentRequest.setBotAlias(botAlias());
        contentRequest.setBotName(botName());
        contentRequest.setUserId(userID());
        contentRequest.setContentType(contentType());
    }

    public  PostTextResult getTextResult() {
        return textResult;
    }

    public  PostContentResult getContentResult() {
        return contentResult;
    }



    private  String contentType() {
        return "audio/x-l16; sample-rate=16000";
    }

    private  String botName() {
        return "Kayla";
    }

    private  String botAlias() {
        return "kayla";
    }

    private  String userID() {
        return "fromIDE";
    }

    public  void sendText(String requestText) {
        textRequest.setInputText(requestText);
        textResult = client.postText(textRequest);
        displayResult(TEXT_INPUT);
    }

    public void sendVoice(InputStream streamFromMic) {
        contentRequest.setInputStream(streamFromMic);
        System.out.println("posting......................................");
        contentResult = client.postContent(contentRequest);
        System.out.println("POSTED......................................");
        displayResult(VOICE_INPUT);
    }

    private void displayResult(int contentType) {
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
