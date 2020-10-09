package com.atlas.ui.client_list.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atlas.R;
import com.atlas.databinding.FragmentListClientsBinding;
import com.atlas.model.AtlasClientEntity;
import com.atlas.ui.client_list.adapter.AtlasClientAdapter;
import com.atlas.ui.client_list.viewmodel.AtlasClientListViewModel;
import com.atlas.ui.client_list.callback.ClientClickCallback;
import com.atlas.ui.gateway_list.view.BackStackFragment;
import com.atlas.ui.main.MainActivity;
;

import java.util.List;

public class AtlasClientListView extends BackStackFragment {

    private AtlasClientAdapter atlasClientAdapter;
    private FragmentListClientsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_clients, container, false);

        atlasClientAdapter = new AtlasClientAdapter(clientClickCallback);

        ((SimpleItemAnimator) binding.clientsView.getItemAnimator()).setSupportsChangeAnimations(false);
        binding.clientsView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        binding.clientsView.setHasFixedSize(true);

        binding.clientsView.setAdapter(atlasClientAdapter);
        binding.setIsLoading(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AtlasClientListViewModel.Factory factory = new AtlasClientListViewModel
                .Factory(getActivity()
                .getApplication(), getArguments().getString("gateway_identity"), getArguments().getString("owner_identity"));

        final AtlasClientListViewModel viewModel = new ViewModelProvider(this, factory)
                .get(AtlasClientListViewModel.class);

        binding.setIsLoading(true);

        observeListViewModel(viewModel);
    }

    private void observeListViewModel(AtlasClientListViewModel viewModel) {

        viewModel.getClientList().observe(this, new Observer<List<AtlasClientEntity>>() {
            @Override
            public void onChanged(List<AtlasClientEntity> atlasClients) {
                Log.w(AtlasClientListView.class.getName(), "Client list changed!");
                if (atlasClients != null) {
                    binding.setIsLoading(false);
                    atlasClientAdapter.setClientList(atlasClients);
                }
            }
        });
    }

    public static AtlasClientListView getInstance(String gatewayIdentity, String ownerIdentity) {

        Log.w(AtlasClientListView.class.getName(), "Get client list fragment for gateway:" + gatewayIdentity + " and owner " + ownerIdentity);
        AtlasClientListView fragment = new AtlasClientListView();
        Bundle args = new Bundle();
        args.putString("gateway_identity", gatewayIdentity);
        args.putString("owner_identity", ownerIdentity);
        fragment.setArguments(args);

        return fragment;
    }

    public void replaceFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(((ViewGroup) getView()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    private final ClientClickCallback clientClickCallback = new ClientClickCallback() {
        @Override
        public void onCLick(AtlasClientEntity client) {
            Log.w(this.getClass().toString(), "Click on client with identity " + client.getIdentity());
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).openAtlasClientCommandListFragment(client);
            }
        }
    };
}
