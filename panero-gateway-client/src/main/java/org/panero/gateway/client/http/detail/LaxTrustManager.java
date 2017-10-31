package org.panero.gateway.client.http.detail;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class LaxTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] certificates, final String s) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] certificates, final String s) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
