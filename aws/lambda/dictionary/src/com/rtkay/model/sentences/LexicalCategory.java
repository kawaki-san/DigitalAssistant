
package com.rtkay.model.sentences;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LexicalCategory {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("text")
    @Expose
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
