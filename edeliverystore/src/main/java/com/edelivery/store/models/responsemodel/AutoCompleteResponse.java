package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.AutocompleteAddress1;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoCompleteResponse {

    @SerializedName("suggestions")
    private List<AutocompleteAddress1> suggestions;

    public List<AutocompleteAddress1> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<AutocompleteAddress1> suggestions) {
        this.suggestions = suggestions;
    }
}
