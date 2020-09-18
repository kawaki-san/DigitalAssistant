package com.rtkay.lex;

public class LexQuery {
    private String botName;
    private String intentName;
    private String word;

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word.toLowerCase();
    }
}
