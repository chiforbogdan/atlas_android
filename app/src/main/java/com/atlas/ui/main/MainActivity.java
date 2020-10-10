package com.atlas.ui.main;

import android.os.Bundle;
import android.util.Log;

import com.atlas.R;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.ui.Tab1;
import com.atlas.ui.client_list.view.AtlasClientListView;
import com.atlas.ui.gateway_claim.AtlasClaimView;
import com.atlas.ui.gateway_list.view.AtlasGatewayListView;
import com.atlas.ui.gateway_list.view.BackStackFragment;
import com.atlas.utils.AtlasSharedPreferences;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Generate owner ID if necessary */
        String ownerID = AtlasSharedPreferences.getInstance(getApplication()).getOwnerID();
        if (ownerID == null) {
            Log.i(MainActivity.class.getName(), "Generating application owner UUID");
            ownerID = UUID.randomUUID().toString();
            AtlasSharedPreferences.getInstance(getApplication()).saveOwnerID(UUID.randomUUID().toString());
            Log.i(MainActivity.class.getName(), "Owner UUID is: " + ownerID);
        }

        // TODO upload firebase token to the cloud

        Log.i(MainActivity.class.getName(), "Owner UUID is: " + ownerID);

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_edit_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_ballot_24);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "Home");
        adapter.addFragment(new AtlasClaimView(), "Claim");
        adapter.addFragment(new AtlasGatewayListView(), "Gateways");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!BackStackFragment.handleBackPressed(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }

    public void openAtlasClientListFragment(AtlasGatewayEntity gateway) {
        AtlasGatewayListView gatewayListFragment = (AtlasGatewayListView) adapter.getItem(viewPager.getCurrentItem());
        gatewayListFragment.replaceFragment(AtlasClientListView.getInstance(gateway.getIdentity()));
    }
}