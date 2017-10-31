package org.panero.gateway.client.http.detail;

import com.google.common.primitives.Ints;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class HttpResponseValidator {
    public static final int[] VALID_RESPONSE_CODES = {
            HttpStatus.SC_OK,
            HttpStatus.SC_CREATED,
            HttpStatus.SC_ACCEPTED,
            HttpStatus.SC_NO_CONTENT
    };

    private static final HttpResponseValidator instance = new HttpResponseValidator();

    private HttpResponseValidator() {
    }

    public static HttpResponseValidator getInstance() {
        return instance;
    }

    public boolean validate(final HttpResponse response) {
        return response != null && Ints.contains(VALID_RESPONSE_CODES, response.getStatusLine().getStatusCode());
    }
}
