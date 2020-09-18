
package com.rtkay.model.definition;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sense {

    @SerializedName("definitions")
    @Expose
    private List<String> definitions = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subsenses")
    @Expose
    private List<Subsense> subsenses = null;

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Subsense> getSubsenses() {
        return subsenses;
    }

    public void setSubsenses(List<Subsense> subsenses) {
        this.subsenses = subsenses;
    }

}
