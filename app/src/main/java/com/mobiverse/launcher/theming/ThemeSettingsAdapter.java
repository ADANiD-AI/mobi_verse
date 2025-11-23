package com.mobiverse.launcher.theming;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiverse.launcher.R;

import java.util.List;

public class ThemeSettingsAdapter extends RecyclerView.Adapter<ThemeSettingsAdapter.ThemeViewHolder> {

    private final Context context;
    private final List<LauncherTheme> themes;
    private final ThemeManager themeManager;
    private int selectedThemePosition;

    public interface OnThemeSelectedListener {
        void onThemeSelected(LauncherTheme theme);
    }

    private OnThemeSelectedListener listener;

    public ThemeSettingsAdapter(Context context, List<LauncherTheme> themes, OnThemeSelectedListener listener) {
        this.context = context;
        this.themes = themes;
        this.themeManager = ThemeManager.getInstance(context);
        this.listener = listener;
        this.selectedThemePosition = themes.indexOf(themeManager.getCurrentTheme());
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_theme, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        LauncherTheme theme = themes.get(position);
        holder.themeName.setText(theme.getName());
        holder.radioButton.setChecked(position == selectedThemePosition);

        // You can set a preview image for the theme here if you have one
        // holder.themePreview.setImageResource(R.drawable.some_preview);

        holder.itemView.setOnClickListener(v -> {
            selectedThemePosition = holder.getAdapterPosition();
            if (listener != null) {
                listener.onThemeSelected(themes.get(selectedThemePosition));
            }
            notifyDataSetChanged();
        });

        holder.radioButton.setOnClickListener(v -> {
            selectedThemePosition = holder.getAdapterPosition();
            if (listener != null) {
                listener.onThemeSelected(themes.get(selectedThemePosition));
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    static class ThemeViewHolder extends RecyclerView.ViewHolder {
        ImageView themePreview;
        TextView themeName;
        RadioButton radioButton;

        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            themePreview = itemView.findViewById(R.id.theme_preview_image);
            themeName = itemView.findViewById(R.id.theme_name_text);
            radioButton = itemView.findViewById(R.id.theme_radio_button);
        }
    }
}
