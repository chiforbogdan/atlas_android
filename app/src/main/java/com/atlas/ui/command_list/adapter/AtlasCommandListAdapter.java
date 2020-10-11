package com.atlas.ui.command_list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.atlas.R;
import com.atlas.databinding.CommandItemBinding;
import com.atlas.model.AtlasClientCommandEntity;

import java.util.List;

public class AtlasCommandListAdapter extends RecyclerView.Adapter<AtlasCommandListAdapter.CommandViewHolder> {

    private List<AtlasClientCommandEntity> commandList;

    public void setCommandList(final List<AtlasClientCommandEntity> commandList) {
        if (this.commandList == null) {
            this.commandList = commandList;
            notifyItemRangeInserted(0, commandList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return AtlasCommandListAdapter.this.commandList.size();
                }

                @Override
                public int getNewListSize() {
                    return commandList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return AtlasCommandListAdapter.this.commandList.get(oldItemPosition).getId()
                            .equals(commandList.get(newItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    AtlasClientCommandEntity newCommand = commandList.get(newItemPosition);
                    AtlasClientCommandEntity oldCommand = commandList.get(oldItemPosition);
                    return oldCommand.getSeqNo() == newCommand.getSeqNo()
                            && oldCommand.getId().equals(newCommand.getId());
                }
            });
            this.commandList = commandList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public AtlasCommandListAdapter.CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommandItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.command_item, parent, false);

        return new AtlasCommandListAdapter.CommandViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AtlasCommandListAdapter.CommandViewHolder holder, final int position) {
        final AtlasClientCommandEntity command = commandList.get(position);

        holder.binding.setCommand(command);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return commandList == null ? 0 : commandList.size();
    }


    public static class CommandViewHolder extends RecyclerView.ViewHolder {

        final CommandItemBinding binding;

        public CommandViewHolder(CommandItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}