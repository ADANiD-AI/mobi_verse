package com.mobiverse.launcher.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiverse.aikeyboard.AITextProcessor;
import com.mobiverse.launcher.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchManager searchManager;
    private SearchResultsAdapter adapter;
    private AITextProcessor aiTextProcessor;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchManager = new SearchManager(this);
        aiTextProcessor = new AITextProcessor(this);

        searchInput = findViewById(R.id.et_search_input);
        RecyclerView recyclerView = findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchResultsAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                String correctedQuery = aiTextProcessor.correctText(query);
                String suggestion = aiTextProcessor.getSuggestion(query);

                searchManager.performSearch(query, correctedQuery, results -> {
                    if (suggestion != null) {
                        // Add suggestion to the top of the results
                        SearchProvider.SearchResult suggestionResult = new SearchProvider.SearchResult(
                            "suggestion",
                            "Suggestion: " + suggestion,
                            null,
                            0.0
                        );
                        results.add(0, suggestionResult);
                    }
                    adapter.updateResults(results);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        searchInput.addTextChangedListener(textWatcher);

        // Get query from intent and perform search
        String query = getIntent().getStringExtra("query");
        if (query != null) {
            searchInput.setText(query);
            textWatcher.onTextChanged(query, 0, 0, query.length());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aiTextProcessor.cleanup();
    }
}