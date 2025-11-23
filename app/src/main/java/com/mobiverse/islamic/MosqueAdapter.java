package com.mobiverse.islamic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.MobiVerse.R;
import java.util.List;

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.MosqueViewHolder> {

    private final List<MosqueFinderActivity.Mosque> mosqueList;
    private final OnMosqueAction onMosqueNavigate;
    private final OnMosqueAction onMosqueFavorite;

    public interface OnMosqueAction {
        void onAction(MosqueFinderActivity.Mosque mosque);
    }

    public MosqueAdapter(List<MosqueFinderActivity.Mosque> mosqueList, OnMosqueAction onMosqueNavigate, OnMosqueAction onMosqueFavorite) {
        this.mosqueList = mosqueList;
        this.onMosqueNavigate = onMosqueNavigate;
        this.onMosqueFavorite = onMosqueFavorite;
    }

    @NonNull
    @Override
    public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mosque, parent, false);
        return new MosqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
        MosqueFinderActivity.Mosque mosque = mosqueList.get(position);
        holder.tvMosqueName.setText(mosque.getName());
        holder.tvMosqueAddress.setText(mosque.getAddress());
        holder.tvDistance.setText(String.format("%.1f km", mosque.getDistanceKm()));
        holder.tvWalkingTime.setText(String.format("• %d min walk", mosque.getWalkingMinutes()));

        holder.btnNavigate.setOnClickListener(v -> onMosqueNavigate.onAction(mosque));
        holder.btnFavorite.setOnClickListener(v -> {
            onMosqueFavorite.onAction(mosque);
            // TODO: Update favorite button UI state (e.g., change icon)
        });
    }

    @Override
    public int getItemCount() {
        return mosqueList.size();
    }

    static class MosqueViewHolder extends RecyclerView.ViewHolder {
        TextView tvMosqueName, tvMosqueAddress, tvDistance, tvWalkingTime;
        Button btnNavigate, btnFavorite;

        public MosqueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMosqueName = itemView.findViewById(R.id.tv_mosque_name);
            tvMosqueAddress = itemView.findViewById(R.id.tv_mosque_address);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvWalkingTime = itemView.findViewById(R.id.tv_walking_time);
            btnNavigate = itemView.findViewById(R.id.btn_navigate);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}
