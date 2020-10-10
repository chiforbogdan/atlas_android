package com.atlas.ui.command_list.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atlas.R;
import com.atlas.databinding.FragmentListCommandsBinding;
import com.atlas.model.AtlasClientCommandEntity;
import com.atlas.ui.command_list.viewmodel.AtlasCommandListViewModel;
import com.atlas.ui.command_list.adapter.AtlasCommandListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AtlasCommandListView extends Fragment {

    private AtlasCommandListAdapter atlasCommandListAdapter;
    private FragmentListCommandsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_commands, container, false);

        atlasCommandListAdapter = new AtlasCommandListAdapter();

        ((SimpleItemAnimator) binding.commandsView.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.commandsView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        binding.commandsView.setHasFixedSize(true);

        binding.commandsView.setAdapter(atlasCommandListAdapter);
        binding.setIsLoading(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AtlasCommandListViewModel.Factory factory = new AtlasCommandListViewModel
                .Factory(getActivity()
                .getApplication(), getArguments().getString("client_identity"), getArguments().getParcelableArrayList("command_list"));

        final AtlasCommandListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(AtlasCommandListViewModel.class);

        binding.setIsLoading(true);

        observeListViewModel(viewModel);
    }

    private void observeListViewModel(AtlasCommandListViewModel viewModel) {

        viewModel.getCommandList().observe(this, new Observer<List<AtlasClientCommandEntity>>() {
            @Override
            public void onChanged(List<AtlasClientCommandEntity> atlasCommands) {
                Log.w(AtlasCommandListView.class.getName(), "Command list changed!");
                if (atlasCommands != null) {
                    binding.setIsLoading(false);
                    atlasCommandListAdapter.setCommandList(atlasCommands);
                }
            }
        });
    }

    public static AtlasCommandListView getInstance(String clientIdentity, List<AtlasClientCommandEntity> commandList) {

        Log.w(AtlasCommandListView.class.getName(), "Get command list fragment for client:" + clientIdentity);
        AtlasCommandListView fragment = new AtlasCommandListView();
        Bundle args = new Bundle();
        args.putString("client_identity", clientIdentity);
        args.putParcelableArrayList("command_list", new ArrayList<AtlasClientCommandEntity>(commandList));
        fragment.setArguments(args);

        return fragment;
    }
}
