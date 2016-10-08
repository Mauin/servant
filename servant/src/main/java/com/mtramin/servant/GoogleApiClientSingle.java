/*
 *  Copyright 2016 Marvin Ramin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mtramin.servant;

import android.content.Context;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

import rx.Single;
import rx.SingleSubscriber;
import rx.subscriptions.Subscriptions;

/**
 * Provides an interface that serves a {@link GoogleApiClient} as a {@link Single}.
 * <p>
 * Will disconnect the Client once the Single is unsubscribed from. This is also the case if the
 * Single emits anything in {@link SingleSubscriber#onSuccess(Object)}.
 */
public abstract class GoogleApiClientSingle<T> extends BaseClient implements Single.OnSubscribe<T> {

    private final GoogleApi googleApi;
    private SingleSubscriber<? super T> singleSubscriber;

    protected GoogleApiClientSingle(Context context, Api api) {
        super(context);
        this.googleApi = new ApiDefinition(api);
    }

    protected GoogleApiClientSingle(Context context, Api api, Api.ApiOptions.HasOptions options) {
        super(context);
        this.googleApi = new ApiWithOptions(api, options);
    }

    @Override
    public void call(SingleSubscriber<? super T> singleSubscriber) {
        this.singleSubscriber = singleSubscriber;

        buildClient(googleApi);
        connect();

        singleSubscriber.add(Subscriptions.create(this::disconnect));
    }

    /**
     * Callback when the client is connected. Call either {@link #onSuccess(Object)} or
     * {@link #onError(Throwable)} to emit values.
     *
     * @param googleApiClient the connected client
     */
    protected abstract void onSingleClientConnected(GoogleApiClient googleApiClient);

    protected void onSuccess(T result) {
        if (!singleSubscriber.isUnsubscribed()) {
            singleSubscriber.onSuccess(result);
        }
    }

    protected void onError(Throwable throwable) {
        if (!singleSubscriber.isUnsubscribed()) {
            singleSubscriber.onError(throwable);
        }
    }

    @Override
    void onClientConnected(GoogleApiClient googleApiClient) {
        onSingleClientConnected(googleApiClient);
    }

    @Override
    void onClientError(Throwable throwable) {
        singleSubscriber.onError(throwable);
    }
}
