package com.atlas.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atlas.R;
import com.atlas.claim.AtlasClaim;
import com.atlas.claim.AtlasClaimJson;
import com.atlas.model.dto.AtlasGatewayClaimReq;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class ClaimGateway extends Fragment {

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

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                        new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                Log.d(ClaimGateway.class.getName(), "Claim gateway");
                                try {
                                    if (!task.isSuccessful()) {
                                        Log.e(ClaimGateway.class.getName(), "Failed to get token from firebase", task.getException());
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    /* Owner ID */
                                    String ownerID = Objects.requireNonNull(task.getResult()).getId();
                                    Log.w(ClaimGateway.class.getName(), "Owner ID is " + ownerID);

                                    AtlasGatewayClaimReq claimReq = new AtlasGatewayClaimReq(shortCode.getText().toString(), "key", ownerID);
                                    AtlasClaim atlasClaim = new AtlasClaim(claimReq, getContext());
                                    String urlClaim = ATLAS_CLAIM_REQUEST_PROTOCOL + "://" + serverPath.getText().toString() + "/";
                                    atlasClaim.execute(urlClaim);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
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