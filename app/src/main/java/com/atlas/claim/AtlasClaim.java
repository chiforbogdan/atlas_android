package com.atlas.claim;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.atlas.R;
import com.atlas.utils.Converter;

import org.apache.commons.io.IOUtils;
import org.conscrypt.Conscrypt;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AtlasClaim extends AsyncTask<String, Void, String> {

    private OkHttpClient client;
    private final MediaType jsonType = MediaType.get("application/json; charset=utf-8");
    private AtlasClaimJson claimJson;
    private Context context;

    private OkHttpClient createOkHttpClient () throws Exception {
        TrustManager[] trustAllCerts       = new TrustManager [] { trustManager () };

        SSLContext sslContext               = SSLContext.getInstance ("SSL");
        sslContext.init (null, trustAllCerts, new SecureRandom ());

        SSLSocketFactory sslSocketFactory   = sslContext.getSocketFactory ();

        OkHttpClient.Builder builder        = new OkHttpClient.Builder ();
        builder.sslSocketFactory (sslSocketFactory, (X509TrustManager)trustAllCerts [0]);
        builder.hostnameVerifier (hostnameVerifier ());

        return builder.build ();
    }

    private static TrustManager trustManager () {
        return new X509TrustManager () {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted (X509Certificate[] chain, String authType) throws CertificateException {  }

            @Override
            public X509Certificate [] getAcceptedIssuers () {
                return new X509Certificate [] {  };
            }
        };
    }

    private static HostnameVerifier hostnameVerifier () {
        return new HostnameVerifier () {

            @Override
            public boolean verify (String hostname, SSLSession session) {
                return true;
            }
        };
    }

    public AtlasClaim(AtlasClaimJson json, Context context) throws Exception {
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        this.context = context;
        this.claimJson = json;
        this.client = createOkHttpClient();
    }

    @Override
    protected String doInBackground(String... urls) {

        Log.d(AtlasClaim.class.getName(), "Execute claim request to URL1111:" + urls[0]);
        RequestBody body = RequestBody.create(claimJson.getJson(), jsonType);

        Request.Builder builder = new Request.Builder();
        builder.url(urls[0]);
        builder.post(body);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            String responseValue = response.body().string();
            Log.d(AtlasClaim.class.getName(), "Claim response is: " + responseValue);
            return responseValue;
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
