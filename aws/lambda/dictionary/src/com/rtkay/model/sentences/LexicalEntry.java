
package com.rtkay.model.sentences;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LexicalEntry {

    @SerializedName("derivatives")
    @Expose
    private List<Derivative> derivatives = null;
    @SerializedName("entries")
    @Expose
    private List<Entry> entries = null;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("lexicalCategory")
    @Expose
    private LexicalCategory lexicalCategory;
    @SerializedName("phrases")
    @Expose
    private List<Phrase> phrases = null;
    @SerializedName("text")
    @Expose
    private String text;

    public List<Derivative> getDerivatives() {
        return derivatives;
    }

    public void setDerivatives(List<Derivative> derivatives) {
        this.derivatives = derivatives;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LexicalCategory getLexicalCategory() {
        return lexicalCategory;
    }

    public void setLexicalCategory(LexicalCategory lexicalCategory) {
        this.lexicalCategory = lexicalCategory;
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
