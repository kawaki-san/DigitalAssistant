package rtkay.model;

import java.util.ArrayList;

public class JokeList {
    ArrayList<Joke> results;
    int total_jokes;
    int status;

    public ArrayList<Joke> getResults() {
        return results;
    }

    public void setResults(ArrayList<Joke> results) {
        this.results = results;
    }

    public int getTotal_jokes() {
        return total_jokes;
    }

    public void setTotal_jokes(int total_jokes) {
        this.total_jokes = total_jokes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
