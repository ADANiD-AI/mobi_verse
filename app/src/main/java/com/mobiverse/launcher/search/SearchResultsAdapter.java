package com.mobiverse.launcher.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiverse.launcher.R;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ResultViewHolder> {

    private Context context;
    private List<SearchProvider.SearchResult> results;

    public SearchResultsAdapter(Context context, List<SearchProvider.SearchResult> results) {
        this.context = context;
        this.results = results;
    }

    public void updateResults(List<SearchProvider.SearchResult> newResults) {
        this.results.clear();
        this.results.addAll(newResults);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        SearchProvider.SearchResult result = results.get(position);
        holder.title.setText(result.title);
        holder.description.setText(result.description);
        holder.icon.setImageDrawable(result.icon);

        holder.itemView.setOnClickListener(v -> {
            if (result.intent != null) {
                context.startActivity(result.intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView description;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_result_icon);
            title = itemView.findViewById(R.id.tv_result_title);
            description = itemView.findViewById(R.id.tv_result_description);
        }
    }
}