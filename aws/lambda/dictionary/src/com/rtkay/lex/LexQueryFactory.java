package com.rtkay.lex;

import java.util.Map;

public class LexQueryFactory {
    public static LexQuery createLexQuery(Map<String, Object> stringObjectMap) {
        Map<String,Object> botMap = (Map<String, Object>) stringObjectMap.get("bot");
        String botName =(String)  botMap.get("name");
        LexQuery lexQuery = new LexQuery();
        lexQuery.setBotName(botName);
        Map<String,Object> currentIntent = (Map<String, Object>) stringObjectMap.get("currentIntent");
        lexQuery.setIntentName((String)currentIntent.get("name"));
        Map<String,Object> slots = (Map<String, Object>) currentIntent.get("slots");
        lexQuery.setWord((String)slots.get("word"));
        return lexQuery;
    }
}
