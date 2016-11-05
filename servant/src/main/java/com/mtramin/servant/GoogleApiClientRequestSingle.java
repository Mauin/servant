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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;

/**
 * Provides interface that can be extended to use a {@link GoogleApiClient} as a {@link io.reactivex.Single} that
 * automatically performs the given request.
 * <p>
 * Will disconnect the client once it has emitted it's value in {@link io.reactivex.SingleEmitter#onSuccess(Object)}
 * or once the recipient disconnects from the Single.
 */
public abstract class GoogleApiClientRequestSingle<T, R extends Result>
        extends GoogleApiClientSingle<T> {

    protected GoogleApiClientRequestSingle(Context context, Api api) {
        super(context.getApplicationContext(), api);
    }

    protected GoogleApiClientRequestSingle(Context context,
                                           Api api,
                                           Api.ApiOptions.HasOptions options) {
        super(context.getApplicationContext(), api, options);
    }

    @Override
    protected void onSingleClientConnected(GoogleApiClient googleApiClient) {
        createRequest(googleApiClient)
                .setResultCallback(result -> {
                    if (!result.getStatus().isSuccess()) {
                        onError(new ClientException("Error in client request. " + result.getStatus()
                                .getStatusMessage()));
                        return;
                    }

                    onSuccess(unwrap((R) result));
                });
    }

    /**
     * Unwrap the result of the request that was sent to the {@link GoogleApiClient}. The given result will
     * already be checked that it was successful, otherwise a {@link ClientException} will be thrown in
     * {@link io.reactivex.SingleEmitter#onError(Throwable)}.
     *
     * @param result result that was received
     * @return result you want to return
     */
    protected abstract T unwrap(R result);

    /**
     * Create the request you want to send with the {@link GoogleApiClient}.
     * E.g. Awareness.SnapshotApi.getWeather(googleApiClient)
     *
     * @param googleApiClient connected client to use for the request
     * @return the pending result returned by the request
     */
    protected abstract PendingResult<? super R> createRequest(GoogleApiClient googleApiClient);

}
