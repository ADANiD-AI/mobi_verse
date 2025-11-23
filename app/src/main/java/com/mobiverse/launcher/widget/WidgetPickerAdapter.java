package com.mobiverse.launcher.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.launcher.R;

import java.util.List;

public class WidgetPickerAdapter extends RecyclerView.Adapter<WidgetPickerAdapter.WidgetViewHolder> {

    private Context context;
    private List<WidgetInfo> widgets;
    private OnWidgetAddListener listener;

    public interface OnWidgetAddListener {
        void onWidgetAdded(WidgetInfo widgetInfo);
    }

    public WidgetPickerAdapter(Context context, List<WidgetInfo> widgets, OnWidgetAddListener listener) {
        this.context = context;
        this.widgets = widgets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WidgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_widget_picker, parent, false);
        return new WidgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetViewHolder holder, int position) {
        WidgetInfo widget = widgets.get(position);
        holder.widgetName.setText(getWidgetName(widget.getType()));
        holder.widgetDescription.setText(getWidgetDescription(widget.getType()));
        holder.widgetPreview.setImageResource(getWidgetPreview(widget.getType()));

        holder.addButton.setOnClickListener(v -> listener.onWidgetAdded(widget));
    }

    @Override
    public int getItemCount() {
        return widgets.size();
    }

    class WidgetViewHolder extends RecyclerView.ViewHolder {
        ImageView widgetPreview;
        TextView widgetName, widgetDescription;
        Button addButton;

        public WidgetViewHolder(@NonNull View itemView) {
            super(itemView);
            widgetPreview = itemView.findViewById(R.id.iv_widget_preview);
            widgetName = itemView.findViewById(R.id.tv_widget_name);
            widgetDescription = itemView.findViewById(R.id.tv_widget_description);
            addButton = itemView.findViewById(R.id.btn_add_widget);
        }
    }

    // Helper methods to get display data for each widget type
    private String getWidgetName(WidgetInfo.WidgetType type) {
        switch (type) {
            case CLOCK: return "Clock";
            case WEATHER: return "Weather";
            case SEARCH_BAR: return "Search Bar";
            case ISLAMIC_PRAYER_TIMES: return "Prayer Times";
            case QURAN_VERSE_OF_THE_DAY: return "Verse of the Day";
            case QUICK_SETTINGS: return "Quick Settings";
            default: return "Unknown Widget";
        }
    }

    private String getWidgetDescription(WidgetInfo.WidgetType type) {
        switch (type) {
            case CLOCK: return "A customizable analog or digital clock.";
            case WEATHER: return "Live weather forecast for your location.";
            case SEARCH_BAR: return "Quickly search the web.";
            case ISLAMIC_PRAYER_TIMES: return "Shows daily prayer times.";
            case QURAN_VERSE_OF_THE_DAY: return "An inspiring verse from the Holy Quran.";
            case QUICK_SETTINGS: return "Toggle WiFi, Bluetooth, and more.";
            default: return "";
        }
    }

    private int getWidgetPreview(WidgetInfo.WidgetType type) {
        // You would have different drawable resources for each widget preview
        // For now, returning a placeholder
        switch (type) {
            // case CLOCK: return R.drawable.preview_clock;
            // case WEATHER: return R.drawable.preview_weather;
            default: return R.drawable.ic_widget_placeholder;
        }
    }
}