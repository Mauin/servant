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

package com.mtramin.servant2;

import android.content.Context;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

/**
 * Provides an interface that serves a {@link GoogleApiClient} as a {@link io.reactivex.Completable}.
 * <p>
 * Will disconnect the Client once the Completable is disposed of. This is also the case once
 * the Completable calls {@link CompletableEmitter#onComplete()}.
 */
public abstract class GoogleApiClientCompletable extends BaseClient
        implements CompletableOnSubscribe {

    private final GoogleApi googleApi;
    private CompletableEmitter completableEmitter;

    protected GoogleApiClientCompletable(Context context, Api api) {
        super(context);
        this.googleApi = new ApiDefinition(api);
    }

    protected GoogleApiClientCompletable(Context context, Api api, HasOptions options) {
        super(context);
        this.googleApi = new ApiWithOptions(api, options);
    }

    @Override
    public void subscribe(CompletableEmitter emitter) throws Exception {
        this.completableEmitter = emitter;

        buildClient(googleApi);
        connect();

        emitter.setCancellable(this::disconnect);
    }

    /**
     * The action to perform once the {@link GoogleApiClient} is connected.
     *
     * @param googleApiClient the connected client
     */
    protected abstract void onCompletableClientConnected(GoogleApiClient googleApiClient);

    /**
     * Call when the Completable GoogleApiClient should complete.
     */
    protected void onCompleted() {
        completableEmitter.onComplete();
    }

    /**
     * Call when the Completable should error.
     *
     * @param throwable throwable to emit in onError.
     */
    protected void onError(Throwable throwable) {
        completableEmitter.onError(throwable);
    }

    @Override
    void onClientConnected(GoogleApiClient googleApiClient) {
        onCompletableClientConnected(googleApiClient);
    }

    @Override
    void onClientError(Throwable throwable) {
        completableEmitter.onError(throwable);
    }
}
