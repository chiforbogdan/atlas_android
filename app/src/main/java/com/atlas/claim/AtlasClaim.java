package com.atlas.claim;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.atlas.R;
import com.atlas.utils.Converter;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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

    private void setSslContext(Context context) throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);

        InputStream is = context.getResources().openRawResource(R.raw.server);
        String certificate = Converter.convertStreamToString(is);

        InputStream stream = IOUtils.toInputStream(certificate, Charset.defaultCharset());

        // CertificateFactory
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // certificate
        Certificate ca;
        try {
            ca = cf.generateCertificate(stream);
        } finally {
            is.close();
        }

        ks.setCertificateEntry("my-ca", ca);

        /* TrustManagerFactory */
        String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        /* Create a TrustManager that trusts the CAs in our KeyStore */
        tmf.init(ks);

        /* Create a SSLContext with the certificate that uses tmf (TrustManager) */
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
    }

    public AtlasClaim(AtlasClaimJson json, Context context) throws Exception {

        this.context = context;
        this.claimJson = json;
        setSslContext(context);
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

        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        System.out.println(s);
    }
}
