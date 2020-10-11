package com.atlas.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.atlas.R;
import com.atlas.database.AtlasDatabase;
import com.atlas.firebase.AtlasFirebaseUtils;
import com.atlas.model.AtlasClientEntity;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.model.dto.AtlasOwnerFirebase;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.ui.Tab1;
import com.atlas.ui.client_list.view.AtlasClientListView;
import com.atlas.ui.command_list.view.AtlasCommandListView;
import com.atlas.ui.gateway_claim.AtlasClaimView;
import com.atlas.ui.gateway_claim.AtlasClaimViewModel;
import com.atlas.ui.gateway_list.view.AtlasGatewayListView;
import com.atlas.ui.gateway_list.view.BackStackFragment;
import com.atlas.utils.AtlasSharedPreferences;
import com.atlas.worker.AtlasFirebaseWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.time.Duration;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;

import static com.atlas.utils.AtlasConstants.ATLAS_CLOUD_BASE_URL;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executeUtils();

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_edit_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_outline_ballot_24);
    }

    private void executeUtils() {
        /* Generate owner ID if necessary */
        String ownerID = AtlasSharedPreferences.getInstance(getApplication()).getOwnerID();
        if (ownerID == null) {
            Log.i(MainActivity.class.getName(), "Generating application owner UUID");
            ownerID = UUID.randomUUID().toString();
            AtlasSharedPreferences.getInstance(getApplication()).saveOwnerID(UUID.randomUUID().toString());
            Log.i(MainActivity.class.getName(), "Owner UUID is: " + ownerID);
        }
        Log.i(MainActivity.class.getName(), "Owner UUID is: " + ownerID);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(AtlasFirebaseUtils.class.getName(), "Firebase token obtained");
                            AtlasSharedPreferences.getInstance(getApplicationContext()).saveFirebaseToken(task.getResult().getToken().toString());
                            /* Update firebase token to cloud */
                            OneTimeWorkRequest firebaseUpdateWorker = new OneTimeWorkRequest.Builder(AtlasFirebaseWorker.class)
                                    .setInitialDelay(Duration.ZERO)
                                    .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofMinutes(1))
                                    .build();
                            WorkManager.getInstance().enqueue(firebaseUpdateWorker);
                        } else {
                            Log.e(AtlasFirebaseUtils.class.getName(), "Firebase token error", task.getException());
                        }
                    }
                });
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

    public void openAtlasClientCommandListFragment(AtlasClientEntity client) {
        AtlasGatewayListView gatewayListFragment = (AtlasGatewayListView) adapter.getItem(viewPager.getCurrentItem());
        gatewayListFragment.replaceFragment(AtlasCommandListView.getInstance(client.getIdentity(), client.getCommands()));
    }
}