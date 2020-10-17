package com.atlas.ui.gateway_list.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atlas.R;
import com.atlas.databinding.FragmentListGatewaysBinding;
import com.atlas.model.database.AtlasGateway;
import com.atlas.ui.main.MainActivity;
import com.atlas.ui.gateway_list.viewmodel.AtlasGatewayListViewModel;

import java.util.List;

import static com.atlas.utils.AtlasConstants.ATLAS_CLIENT_COMMANDS_BROADCAST;

public class AtlasGatewayListView extends BackStackFragment {

    private AtlasGatewayAdapter atlasGatewayAdapter;
    private FragmentListGatewaysBinding binding;
    private AtlasGatewayListViewModel viewModel;
    private BroadcastReceiver gatewaysReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_gateways, container, false);

        atlasGatewayAdapter = new AtlasGatewayAdapter(gatewayClickCallback);

        ((SimpleItemAnimator) binding.gatewaysView.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.gatewaysView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        binding.gatewaysView.setHasFixedSize(true);

        binding.gatewaysView.setAdapter(atlasGatewayAdapter);
        binding.setIsLoading(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AtlasGatewayListViewModel.class);
        observeListViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Atlas Gateways");

        if (viewModel != null) {
            viewModel.fetchGatewayList();
        }

        if (gatewaysReceiver == null) {
            gatewaysReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase(ATLAS_CLIENT_COMMANDS_BROADCAST) && viewModel != null) {
                        viewModel.fetchGatewayList();
                    }
                }
            };
            getContext().registerReceiver(gatewaysReceiver, new IntentFilter(ATLAS_CLIENT_COMMANDS_BROADCAST));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (gatewaysReceiver != null) {
            getContext().unregisterReceiver(gatewaysReceiver);
            gatewaysReceiver = null;
        }
    }

    private void observeListViewModel() {

        viewModel.getGatewayList().observe(getViewLifecycleOwner(), new Observer<List<AtlasGateway>>() {
            @Override
            public void onChanged(List<AtlasGateway> atlasGateways) {
                Log.d(this.getClass().toString(), "Gateway list changed!");
                if (atlasGateways != null) {
                    binding.setIsLoading(false);
                    atlasGatewayAdapter.setGatewayList(atlasGateways);
                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(((ViewGroup) getView()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    private final GatewayClickCallback gatewayClickCallback = new GatewayClickCallback() {
        @Override
        public void onCLick(AtlasGateway gateway) {
            Log.w(this.getClass().toString(), "Click on gateway element");
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).openAtlasClientListFragment(gateway);
            }
        }
    };
}
