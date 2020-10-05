package com.atlas.ui.gateway_list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.atlas.R;
import com.atlas.databinding.GatewayItemBinding;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.ui.gateway_list.callback.GatewayClickCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AtlasGatewayAdapter extends RecyclerView.Adapter<AtlasGatewayAdapter.GatewayViewHolder> {

    private List<AtlasGatewayEntity> gatewayList;

    @Nullable
    private final GatewayClickCallback gatewayClickCallback;

    public AtlasGatewayAdapter(@Nullable GatewayClickCallback gatewayClickCallback) {
        this.gatewayClickCallback = gatewayClickCallback;
    }

    public void setGatewayList(final List<AtlasGatewayEntity> gatewayList) {
        if (this.gatewayList == null) {
            this.gatewayList = gatewayList;
            notifyItemRangeInserted(0, gatewayList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AtlasGatewayAdapter.this.gatewayList.size();
                }

                @Override
                public int getNewListSize() {
                    return gatewayList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AtlasGatewayAdapter.this.gatewayList.get(oldItemPosition).getIdentity()
                            .matches(gatewayList.get(newItemPosition).getIdentity());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AtlasGatewayEntity newGateway = gatewayList.get(newItemPosition);
                    AtlasGatewayEntity oldGateway = gatewayList.get(oldItemPosition);
                    return oldGateway.getIdentity().matches(newGateway.getIdentity())
                            && oldGateway.getAlias().matches(newGateway.getAlias());
                }
            });
            this.gatewayList = gatewayList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public AtlasGatewayAdapter.GatewayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GatewayItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gateway_item, parent, false);
        binding.setCallback(gatewayClickCallback);

        return new GatewayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AtlasGatewayAdapter.GatewayViewHolder holder, final int position) {
        final AtlasGatewayEntity gateway = gatewayList.get(position);

        holder.binding.setGateway(gateway);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return gatewayList == null ? 0 : gatewayList.size();
    }

    public static class GatewayViewHolder extends RecyclerView.ViewHolder {

        final GatewayItemBinding binding;

        public GatewayViewHolder(GatewayItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
