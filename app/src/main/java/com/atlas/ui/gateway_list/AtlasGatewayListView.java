package com.atlas.ui.gateway_list;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atlas.R;
import com.atlas.databinding.FragmentListGatewaysBinding;
import com.atlas.model.AtlasGatewayEntity;

import java.util.List;

public class AtlasGatewayListView extends Fragment {

    private AtlasGatewayAdapter atlasGatewayAdapter;
    private FragmentListGatewaysBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_gateways, container, false);

        atlasGatewayAdapter = new AtlasGatewayAdapter(gatewayClickCallback);

        /* Set divider between items */
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.gatewaysView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        binding.gatewaysView.addItemDecoration(horizontalDecoration);

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

        final AtlasGatewayListViewModel viewModel = new ViewModelProvider(this).get(AtlasGatewayListViewModel.class);
        observeListViewModel(viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Atlas Gateways");
    }

    private void observeListViewModel(AtlasGatewayListViewModel viewModel) {

        viewModel.getGatewayList().observe(this, new Observer<List<AtlasGatewayEntity>>() {
            @Override
            public void onChanged(List<AtlasGatewayEntity> atlasGateways) {
                Log.w(this.getClass().toString(), "Gateway list changed!");
                if (atlasGateways != null) {
                    binding.setIsLoading(false);
                    atlasGatewayAdapter.setGatewayList(atlasGateways);
                }
            }
        });
    }

    private final GatewayClickCallback gatewayClickCallback = new GatewayClickCallback() {
        @Override
        public void onCLick(AtlasGatewayEntity gateway) {
            Log.w(this.getClass().toString(),"Click on gateway element");
        }
    };
}
