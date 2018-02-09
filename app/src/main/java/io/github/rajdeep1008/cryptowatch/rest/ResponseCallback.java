package io.github.rajdeep1008.cryptowatch.rest;

/**
 * Created by rajdeep1008 on 03/01/18.
 */

public interface ResponseCallback<T> {
    void success(T t);

    void failure(T t);
}
