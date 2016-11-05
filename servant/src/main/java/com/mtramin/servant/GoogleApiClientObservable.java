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

import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Serves a {@link GoogleApiClient} as an Observable that will emit the Client in
 * {@link Emitter#onNext(Object)} when it is ready to be used.
 * <p>
 * The client will be disconnected once the returned {@link Observable} is disposed of.
 */
class GoogleApiClientObservable extends BaseClient
        implements ObservableOnSubscribe<GoogleApiClient> {

    private final GoogleApi googleApi;
    private ObservableEmitter<GoogleApiClient> emitter;

    private GoogleApiClientObservable(Context context, GoogleApi googleApi) {
        super(context);
        this.googleApi = googleApi;
    }

    static Observable<GoogleApiClient> create(Context context, GoogleApi googleApi) {
        return Observable.create(new GoogleApiClientObservable(context, googleApi));
    }

    @Override
    public void subscribe(ObservableEmitter<GoogleApiClient> emitter) throws Exception {
        this.emitter = emitter;

        buildClient(googleApi);
        connect();

        emitter.setCancellable(this::disconnect);
    }

    @Override
    void onClientConnected(GoogleApiClient googleApiClient) {
        emitter.onNext(googleApiClient);
    }

    @Override
    void onClientError(Throwable throwable) {
        emitter.onError(throwable);
    }
}
