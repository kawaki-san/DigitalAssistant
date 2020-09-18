package com.rtkay;

import com.rtkay.model.definition.EntryResult;
import com.rtkay.model.sentences.UseInSentence;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface OxfordAPI {

    @Headers({"Accept: application/json", "app_id: ", "app_key: "})
    @GET("entries/{source_lang}/{word_id}")
    Call<EntryResult> getDefinition(@Path("source_lang") String source_lang, @Path("word_id") String lowerCaseWord);

    @Headers({"Accept: application/json", "app_id: ", "app_key: "})
    @GET("entries/{source_lang}/{word_id}?strictMatch=false/examples")
    Call<UseInSentence> getSentence(@Path("source_lang") String source_lang, @Path("word_id") String lowerCaseWord);

}
