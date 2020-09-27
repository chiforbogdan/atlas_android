package com.atlas.ui.gateway_claim;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atlas.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class AtlasClaimView extends Fragment {

    private final String ATLAS_CLAIM_REQUEST_PROTOCOL = "https";

    private AtlasClaimViewModel viewModel;
    private String ipPortValue;
    private String shortCodeValue;
    private String aliasValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.claim_gateway, container, false);

        Button claimButton = view.findViewById(R.id.claimButton);
        final EditText serverPath = view.findViewById(R.id.gateway_ip_port_edit_text);
        final EditText shortCode = view.findViewById(R.id.gateway_short_code_edit_text);
        final EditText alias = view.findViewById(R.id.gateway_alias_edit_text);

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
                if (alias.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "Enter alias", Toast.LENGTH_SHORT).show();
                    return;
                }
                ipPortValue = serverPath.getText().toString();
                shortCodeValue = shortCode.getText().toString();
                aliasValue = alias.getText().toString();

                viewModel.validateAlias(aliasValue);
            }
        });

        viewModel = new ViewModelProvider(this).get(AtlasClaimViewModel.class);
        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        viewModel.getAliasLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aliasValid) {
                Log.d(AtlasClaimView.class.getName(), "Alias value changed from view model");

//                if (aliasValid) {
//                    Log.d(AtlasGatewayClaimView.class.getName(), "Alias value is valid. Claiming the gateway now...");
//                    viewModel.claimGateway(ipPortValue, shortCodeValue, aliasValue);
//                } else {
//                    Log.d(AtlasGatewayClaimView.class.getName(), "Alias value is not valid");
//                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Connect gateway");
    }
}