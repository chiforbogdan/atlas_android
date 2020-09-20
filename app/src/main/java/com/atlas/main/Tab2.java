package com.atlas.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.atlas.R;
import com.atlas.claim.AtlasClaim;
import com.atlas.claim.AtlasClaimJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Tab2 extends Fragment {

    private final String ATLAS_CLAIM_REQUEST_PROTOCOL = "https";
    private final String ATLAS_CLAIM_REQUEST_SERVER = "192.168.100.9";
    private final String ATLAS_CLAIM_REQUEST_PATH = "/gateway/claim";
    private final Integer ATLAS_CLAIM_REQUEST_PORT = 8085;

    private Button claimButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);

        claimButton = view.findViewById(R.id.claimButton);
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtlasClaim atlasClaim = new AtlasClaim(new AtlasClaimJson("034sd", "key", "identity"), getContext());

                String urlClaim = ATLAS_CLAIM_REQUEST_PROTOCOL + "://" + ATLAS_CLAIM_REQUEST_SERVER + ":" + ATLAS_CLAIM_REQUEST_PORT + ATLAS_CLAIM_REQUEST_PATH;
                System.out.println(urlClaim);
                try {
                    atlasClaim.execute(urlClaim);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Connect gateway");
    }
}