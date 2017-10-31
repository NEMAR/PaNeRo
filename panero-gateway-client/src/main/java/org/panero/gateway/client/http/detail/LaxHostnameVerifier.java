package org.panero.gateway.client.http.detail;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class LaxHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(final String s, final SSLSession session) {
        return true;
    }
}
