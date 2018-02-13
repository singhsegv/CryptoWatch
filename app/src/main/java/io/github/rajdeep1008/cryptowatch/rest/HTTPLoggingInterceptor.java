package io.github.rajdeep1008.cryptowatch.rest;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by rajdeep1008 on 03/01/18.
 */

public class HTTPLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s", request.url());
        if (request.method().equalsIgnoreCase("post")) {
            requestLog = requestLog + "\n" + bodyToString(request);
        }

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String responseLog = String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());
        String bodyString = response.body().string();

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    private static String bodyToString(Request request) {
        try {
            Request temp = request.newBuilder().build();
            Buffer buffer = new Buffer();
            temp.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
            return "did not work";
        }
    }
}
