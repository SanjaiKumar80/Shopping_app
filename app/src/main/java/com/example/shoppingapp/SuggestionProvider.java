package com.example.shoppingapp;

import android.content.SearchRecentSuggestionsProvider;
import android.util.Log;

public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.shoppingapp" ;
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public SuggestionProvider() {
        Log.d("TAG", "SuggestionProvider: "+AUTHORITY);
        Log.d("TAG", "SuggestionProviderMODE: "+MODE);
        setupSuggestions(AUTHORITY, MODE);
    }
}
