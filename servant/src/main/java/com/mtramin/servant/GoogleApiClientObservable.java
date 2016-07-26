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

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

/**
 * Serves a {@link GoogleApiClient} as an Observable that will emit the Client in
 * {@link Subscriber#onNext(Object)} when it is ready to be used.
 * <p>
 * The client will be disconnected once the returned {@link Observable} is unsubscribed from.
 */
class GoogleApiClientObservable extends BaseClient implements Observable.OnSubscribe<GoogleApiClient> {

    private final Api api;
    private Subscriber<? super GoogleApiClient> subscriber;

    private GoogleApiClientObservable(Context context, Api api) {
        super(context);
        this.api = api;
    }

    static Observable<GoogleApiClient> create(Context context, Api api) {
        return Observable.create(new GoogleApiClientObservable(context, api));
    }

    @Override
    public void call(Subscriber<? super GoogleApiClient> subscriber) {
        this.subscriber = subscriber;

        buildClient(api);
        connect();

        subscriber.add(Subscriptions.create(this::disconnect));
    }

    @Override
    void onClientConnected(GoogleApiClient googleApiClient) {
        subscriber.onNext(googleApiClient);
    }

    @Override
    void onClientError(Throwable throwable) {
        subscriber.onError(throwable);
    }
}
