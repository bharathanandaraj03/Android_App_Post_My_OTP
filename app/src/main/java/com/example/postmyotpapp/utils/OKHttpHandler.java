package com.example.postmyotpapp.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class OKHttpHandler {

    private OkHttpClient client;

    public OKHttpHandler() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(final String hostname, final SSLSession session) {
                        return true;
                    }
                })
                .build();
    }

    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.

        String entrustRootCertificateAuthority = "-----BEGIN CERTIFICATE-----\n" +
                "MIIELTCCAxWgAwIBAgIUGT0TNCU4JznhbywDOFDCd2+TCkowDQYJKoZIhvcNAQEF\n" +
                "BQAwgaUxCzAJBgNVBAYTAlVLMREwDwYDVQQIDAhjb3ZlbnRyeTERMA8GA1UEBwwI\n" +
                "TWlkbGFuZHMxGDAWBgNVBAoMD0JBIFRlY2hub2xvZ2llczEMMAoGA1UECwwDQkFU\n" +
                "MRowGAYDVQQDDBFCaGFyYXRoIEFuYW5kYXJhajEsMCoGCSqGSIb3DQEJARYdYmhh\n" +
                "cmF0aC5hbmFuZGFyYWowM0BnbWFpbC5jb20wHhcNMjAwNTIyMTgxMjI4WhcNMjMw\n" +
                "MjE1MTgxMjI4WjCBpTELMAkGA1UEBhMCVUsxETAPBgNVBAgMCGNvdmVudHJ5MREw\n" +
                "DwYDVQQHDAhNaWRsYW5kczEYMBYGA1UECgwPQkEgVGVjaG5vbG9naWVzMQwwCgYD\n" +
                "VQQLDANCQVQxGjAYBgNVBAMMEUJoYXJhdGggQW5hbmRhcmFqMSwwKgYJKoZIhvcN\n" +
                "AQkBFh1iaGFyYXRoLmFuYW5kYXJhajAzQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcN\n" +
                "AQEBBQADggEPADCCAQoCggEBALVpXu8vr+rntnV+TIlVBXpYjv6sz0K7Hpt+A4xr\n" +
                "EJvH4s77i3tAJmVZMzscO6cSRRUitLR+q2xO915ctJhA8h2rdp7CPVnFaQGJ21oL\n" +
                "zoi0q6wk0dxtGCoIbLl7pGxkqPL3RxmgbcZjm5/uYVEauRu7pAxGjJtBjmQpUYDt\n" +
                "LvfygbzCdcgGMsAvDie+Ym/F2wIZV4NqJS9JxrV9gT0pXnbUM/DzITbSRx4bW4jZ\n" +
                "bnZdhMrHwKKm8NFb+avnNGgIIkRkOB9OSfH8zvAajrRgoXgYvNGDzs5+GTQOalMT\n" +
                "JhHZnvgWWLc21wM2LhccFxbSvEp5RVSlNWd3SgUCMReEDD8CAwEAAaNTMFEwHQYD\n" +
                "VR0OBBYEFHb39jlZsx/Gsfcep/9mA2dUoLncMB8GA1UdIwQYMBaAFHb39jlZsx/G\n" +
                "sfcep/9mA2dUoLncMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQEFBQADggEB\n" +
                "AIwULrhwW1Dm6HGkdy1oLrNrYUWt9/TNN1PPwJUWszbotKFeBQ9pxdMPs21xZ+AM\n" +
                "oWrH8la7VdB4LpGg5E1qp6b83H043Z/5V7lg/oafkFyrYtTl78reNH8qdCAuVIC3\n" +
                "pQIeUNsHBcMO281O2HS/eCiA3HlBE3hi6IIQ95rDCd0X5A42MbFXe08/5ip1ec+K\n" +
                "qLaIw1caypfH4KmT0fPQ8MjvzNxdB1mHpW/G+ht99c6fIDIyTOsje1h5yXA/uAXu\n" +
                "RNQ90yKFJwel/e8z9rspomHeGf1Akyfdb9Sd5Iv8UoYL+jygqqFE/PNEmQ0k4DYE\n" +
                "Mnf75UDpRQPb50uCYSPSjRI=\n" +
                "-----END CERTIFICATE-----";
        return new Buffer()
                .writeUtf8(entrustRootCertificateAuthority)
                .inputStream();
    }

    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    public void getHttpResponse(String url) throws IOException {

       // OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        Log.e("TAG_Get_Reponse", response.body().string());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e("TAGReponse", mMessage);
            }
        });
    }

    public void postRequestFileBody(String url,String body) throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");

       // OkHttpClient client = new OkHttpClient();
       // client = new OkHttpClient();
        /*Create upload file content mime type.
        MediaType fileContentType = MediaType.parse("File/*");

        // Create file object.
        File file = new File(fileName);

        // Create request body.
        RequestBody requestBody = RequestBody.create(file,fileContentType);
*/
        RequestBody requestBody = RequestBody.create(body,MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                Log.e("POSTAPIResponse", mMessage);
            }
        });
    }
}
