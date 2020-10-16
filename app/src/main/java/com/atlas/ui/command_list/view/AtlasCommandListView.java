package com.atlas.ui.command_list.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.atlas.model.database.AtlasCommand;
import com.atlas.ui.command_list.callback.CommandApproveCallback;
import com.atlas.ui.command_list.viewmodel.AtlasCommandListViewModel;
import com.atlas.ui.command_list.adapter.AtlasCommandListAdapter;

import java.util.List;

import static com.atlas.utils.AtlasConstants.ATLAS_CLIENT_COMMANDS_BROADCAST;

public class AtlasCommandListView extends Fragment {

    private AtlasCommandListAdapter atlasCommandListAdapter;
    private FragmentListCommandsBinding binding;
    private AtlasCommandListViewModel viewModel;
    private BroadcastReceiver commandsReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_commands, container, false);

        atlasCommandListAdapter = new AtlasCommandListAdapter(commandApproveCallback);

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
                .Factory(getActivity().getApplication(), getArguments().getString("client_identity"));

        viewModel = new ViewModelProvider(this, factory)
                .get(AtlasCommandListViewModel.class);

        binding.setIsLoading(true);

        observeListViewModel(viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (viewModel != null) {
            viewModel.fetchCommands();
        }

        if (commandsReceiver == null) {
            commandsReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase(ATLAS_CLIENT_COMMANDS_BROADCAST) && viewModel != null) {
                        viewModel.fetchCommands();
                    }
                }
            };
            getContext().registerReceiver(commandsReceiver, new IntentFilter(ATLAS_CLIENT_COMMANDS_BROADCAST));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (commandsReceiver != null) {
            getContext().unregisterReceiver(commandsReceiver);
            commandsReceiver = null;
        }
    }

    private void observeListViewModel(AtlasCommandListViewModel viewModel) {

        viewModel.getCommandList().observe(getViewLifecycleOwner(), new Observer<List<AtlasCommand>>() {
            @Override
            public void onChanged(List<AtlasCommand> atlasCommands) {
                Log.w(AtlasCommandListView.class.getName(), "Command list changed!");
                if (atlasCommands != null) {

                    /* Set approve&reject buttons visible for the first command */
                    if (!atlasCommands.isEmpty())
                        atlasCommands.get(0).setActionButtonDisplayed(true);

                    binding.setIsLoading(false);
                    atlasCommandListAdapter.setCommandList(atlasCommands);
                }
            }
        });
    }

    public static AtlasCommandListView getInstance(String clientIdentity) {

        Log.w(AtlasCommandListView.class.getName(), "Get command list fragment for client:" + clientIdentity);
        AtlasCommandListView fragment = new AtlasCommandListView();
        Bundle args = new Bundle();
        args.putString("client_identity", clientIdentity);
        fragment.setArguments(args);

        return fragment;
    }

    private final CommandApproveCallback commandApproveCallback = new CommandApproveCallback() {
        @Override
        public void onApproveButtonClick(AtlasCommand command) {
            try {
                Log.w(AtlasCommandListView.class.getName(), "Command with seq. nr. " + command.getSeqNo().toString() + " is being approved!");

                Toast toast;
                if (viewModel.approveCommand(command))
                    toast = Toast.makeText(getActivity(), "Command has been APPROVED successfully!", Toast.LENGTH_SHORT);
                else
                    toast = Toast.makeText(getActivity(), "An error occurred while trying to approve command!", Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRejectButtonClick(AtlasCommand command) {
            try {
                Log.w(AtlasCommandListView.class.getName(), "Command with seq. nr. " + command.getSeqNo().toString() + " is being rejected!");

                Toast toast;
                if (viewModel.rejectCommand(command))
                    toast = Toast.makeText(getActivity(), "Command has been REJECTED successfully!", Toast.LENGTH_SHORT);
                else
                    toast = Toast.makeText(getActivity(), "An error occurred while trying to reject command!", Toast.LENGTH_SHORT);

                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
