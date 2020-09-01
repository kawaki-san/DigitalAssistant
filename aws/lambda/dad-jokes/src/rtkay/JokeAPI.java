package rtkay;

import rtkay.model.Joke;
import rtkay.model.JokeList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface JokeAPI {
    String BASE_URL = "https://icanhazdadjoke.com/";
    @Headers({"Accept: application/json","User-Agent: javafx-aws-bot (https://github.com/dykstrom/javafx-dad-joke)"})
    @GET("/")
    Call<Joke> getRandomJoke();

    @Headers({"Accept: application/json","User-Agent: javafx-aws-bot (https://github.com/dykstrom/javafx-dad-joke)"})
    @GET("search")
    Call<JokeList> getJokeBySubject(@Query("term")String term, @Query("page") String page,
                                    @Query("limit") int limit);
}
