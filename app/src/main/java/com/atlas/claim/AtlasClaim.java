package com.atlas.claim;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mklimek.sslutilsandroid.SslUtils;

import java.net.URL;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AtlasClaim extends AsyncTask<String, Void, String> {

    private OkHttpClient client;
    private final MediaType jsonType = MediaType.get("application/json; charset=utf-8");
    private AtlasClaimJson claimJson;

    public AtlasClaim(AtlasClaimJson json, Context context) {

        this.claimJson = json;

        /*certificate*/
        SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "myCert.cer");
        client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory()).build();
      
    }

    @Override
    protected String doInBackground(String... urls) {

        RequestBody body = RequestBody.create(claimJson.getJson(), jsonType);

        Request.Builder builder = new Request.Builder();
        builder.url(urls[0]);
        builder.post(body);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);

        System.out.println(s);
    }
}
