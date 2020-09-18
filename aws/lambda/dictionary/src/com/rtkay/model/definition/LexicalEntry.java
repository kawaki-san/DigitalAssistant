
package com.rtkay.model.definition;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LexicalEntry {

    @SerializedName("entries")
    @Expose
    private List<Entry> entries = null;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("lexicalCategory")
    @Expose
    private LexicalCategory lexicalCategory;
    @SerializedName("text")
    @Expose
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
