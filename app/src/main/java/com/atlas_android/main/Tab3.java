package com.atlas_android.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlas_android.R;
import com.atlas_android.gateway.AtlasGateway;
import com.atlas_android.gateway.AtlasGatewayAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class Tab3 extends Fragment {

    private AtlasGatewayAdapter atlasGatewayAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3, container, false);
        recyclerView = view.findViewById(R.id.gateways_view);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.line_divider);
        horizontalDecoration.setDrawable(horizontalDivider);
        recyclerView.addItemDecoration(horizontalDecoration);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setHasFixedSize(true);

        List<AtlasGateway> gatewayList = new ArrayList<>();
        gatewayList.add(new AtlasGateway("Gateway 1", "82862241-e261-41e8-b627-869892197894", "Jx6EP9rRdngzMvsr4sZE4FjWVninAM3JAlqiJHZcwtM=", 3, "21.04.2020", "22.09.2020"));
        gatewayList.add(new AtlasGateway("Gateway 2", "82862241-e261-41e8-b627-869892197894", "Jx6EP9rRdngzMvsr4sZE4FjWVninAM3JAlqiJHZcwtM=", 3, "21.04.2020", "11.04.1995"));
        gatewayList.get(0).setRegistered(true);

        atlasGatewayAdapter = new AtlasGatewayAdapter(gatewayList);
        recyclerView.setAdapter(atlasGatewayAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Atlas Gateways");
    }
}