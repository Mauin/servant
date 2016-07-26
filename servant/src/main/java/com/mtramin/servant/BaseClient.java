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
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Implements the basic setup of a {@link GoogleApiClient} with a given {@link Api}
 */
abstract class BaseClient {

    private final Context context;
    private GoogleApiClient googleApiClient;

    BaseClient(Context context) {
        this.context = context;
    }

    GoogleApiClient buildClient(Api api) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(api)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        onClientConnected(googleApiClient);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        onClientError(new ClientException("Client connection was suspended."));
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> onClientError(new ClientException(connectionResult.getErrorMessage())))
                .build();

        return googleApiClient;
    }

    void connect() {
        googleApiClient.connect();
    }

    protected void disconnect() {
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    abstract void onClientConnected(GoogleApiClient googleApiClient);

    abstract void onClientError(Throwable throwable);
}
