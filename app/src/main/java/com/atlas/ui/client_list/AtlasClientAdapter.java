package com.atlas.ui.client_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.atlas.R;
import com.atlas.databinding.ClientItemBinding;
import com.atlas.model.AtlasClientEntity;

import java.util.List;

public class AtlasClientAdapter extends RecyclerView.Adapter<AtlasClientAdapter.ClientViewHolder> {


    private List<AtlasClientEntity> clientList;

    @Nullable
    private final ClientClickCallback clientClickCallback;

    public AtlasClientAdapter(@Nullable ClientClickCallback clientClickCallback) {
        this.clientClickCallback = clientClickCallback;
    }

    public void setClientList(final List<AtlasClientEntity> clientList) {
        if (this.clientList == null) {
            this.clientList = clientList;
            notifyItemRangeInserted(0, clientList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AtlasClientAdapter.this.clientList.size();
                }

                @Override
                public int getNewListSize() {
                    return clientList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AtlasClientAdapter.this.clientList.get(oldItemPosition).getIdentity()
                            .matches(clientList.get(newItemPosition).getIdentity());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AtlasClientEntity newClient = clientList.get(newItemPosition);
                    AtlasClientEntity oldClient = clientList.get(oldItemPosition);
                    return oldClient.getIdentity().matches(newClient.getIdentity())
                            && oldClient.getId().equals(newClient.getId());
                }
            });
            this.clientList = clientList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public AtlasClientAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClientItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.client_item, parent, false);
        binding.setCallback(clientClickCallback);

        return new AtlasClientAdapter.ClientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AtlasClientAdapter.ClientViewHolder holder, final int position) {
        final AtlasClientEntity client = clientList.get(position);

        holder.binding.setClient(client);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return clientList == null ? 0 : clientList.size();
    }


    public static class ClientViewHolder extends RecyclerView.ViewHolder {

        final ClientItemBinding binding;

        public ClientViewHolder(ClientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
