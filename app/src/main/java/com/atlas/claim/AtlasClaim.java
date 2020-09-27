package com.atlas.claim;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.atlas.database.AtlasDatabase;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.model.dto.AtlasGatewayClaimReq;
import com.atlas.model.dto.AtlasGatewayClaimResp;
import com.atlas.networking.AtlasGatewayClaimAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;

import java.util.List;

import retrofit2.Response;

public class AtlasClaim extends AsyncTask<String, Void, String> {

    private AtlasGatewayClaimReq claimReq;
    private Context context;


    public AtlasClaim(AtlasGatewayClaimReq req, Context context) throws Exception {
        this.context = context;
        this.claimReq = req;
    }

    @Override
    protected String doInBackground(String... urls) {

        Log.d(AtlasClaim.class.getName(), "Execute claim request to URL1111:" + urls[0]);
        try {
            AtlasGatewayClaimAPI gatewayClaimAPI = AtlasNetworkAPIFactory.createGatewayClaimAPI(urls[0]);
            Response<AtlasGatewayClaimResp> claimResp = gatewayClaimAPI.claimGateway(claimReq).execute();
            Log.e(AtlasClaim.class.getName(), "Resp: " + claimResp.code() + " gg: " + claimResp.body());
            AtlasGatewayEntity gateway = new AtlasGatewayEntity();
            gateway.setIdentity(claimResp.body().getIdentity());
            gateway.setAlias("alias");
            AtlasDatabase.getInstance(context).gatewayEntityDao().insertGateway(gateway);

            List<AtlasGatewayEntity> gateways = AtlasDatabase.getInstance(context).gatewayEntityDao().selectAll();
            gateways.forEach(gw -> Log.d(AtlasClaim.class.getName(), "GW:" + gw.getIdentity()));
        } catch (Exception e) {
            Log.e(AtlasClaim.class.getName(), "Claim request exception: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        System.out.println(s);
    }
}
