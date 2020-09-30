package com.atlas.ui;

import android.os.Bundle;

import com.atlas.R;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.ui.client_list.AtlasClientListView;
import com.atlas.ui.gateway_claim.AtlasClaimView;
import com.atlas.ui.gateway_list.AtlasGatewayListView;
import com.atlas.ui.gateway_list.BackStackFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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