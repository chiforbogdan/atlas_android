package com.atlas.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atlas.R;
import com.atlas.claim.AtlasClaim;
import com.atlas.claim.AtlasClaimJson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Tab2 extends Fragment {

    private final String ATLAS_CLAIM_REQUEST_PROTOCOL = "https";
    //private final String ATLAS_CLAIM_REQUEST_SERVER = "192.168.100.9";
    private final String ATLAS_CLAIM_REQUEST_PATH = "/gateway/claim";
    // private final Integer ATLAS_CLAIM_REQUEST_PORT = 8085;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);

        Button claimButton = view.findViewById(R.id.claimButton);
        final EditText serverPath = view.findViewById(R.id.serverIp);
        final EditText shortCode = view.findViewById(R.id.shortCode);

        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serverPath.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Enter server path", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (shortCode.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Enter short code", Toast.LENGTH_SHORT).show();
                    return;
                }

                AtlasClaim atlasClaim = null;
                try {
                    atlasClaim = new AtlasClaim(new AtlasClaimJson(shortCode.getText().toString(), "key", "identity"), getContext());

                    String urlClaim = ATLAS_CLAIM_REQUEST_PROTOCOL + "://" + serverPath.getText().toString() + ATLAS_CLAIM_REQUEST_PATH;

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