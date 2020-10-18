package com.atlas.ui.home.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.R;
import com.atlas.ui.home.viewmodel.HomeViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import static com.atlas.utils.AtlasConstants.ATLAS_CLIENT_COMMANDS_BROADCAST;

public class Home extends Fragment {

    private HomeViewModel viewModel;
    private TextView pendingCommands;
    private TextView pendingCommandsLabel;
    private BroadcastReceiver commandsReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        pendingCommands = view.findViewById(R.id.total_pending_commands_text_view);
        pendingCommandsLabel = view.findViewById(R.id.total_pending_commands_label_text_view);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        observeListViewModel();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        if (viewModel != null) {
            viewModel.fetchTotalPendingCommands();
        }

        if (commandsReceiver == null) {
            commandsReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase(ATLAS_CLIENT_COMMANDS_BROADCAST) && viewModel != null) {
                        viewModel.fetchTotalPendingCommands();
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

    private void observeListViewModel() {
        viewModel.getTotalPendingCommands().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long totalPendingCommands) {
                if (totalPendingCommands != null) {
                    pendingCommands.setText(totalPendingCommands.toString());
                    if (totalPendingCommands == 1) {
                        pendingCommandsLabel.setText(R.string.home_total_command_waiting);
                    } else {
                        pendingCommandsLabel.setText(R.string.home_total_commands_waiting);
                    }
                }
            }
        });
    }
}