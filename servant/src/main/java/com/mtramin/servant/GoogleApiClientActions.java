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

import rx.functions.Action1;

/**
 * Implementation serving a {@link GoogleApiClient} that is usable with Actions.
 * <p>
 * Will provide the created {@link GoogleApiClient} via the action
 * {@link #onClientConnected(GoogleApiClient)} and will disconnect the client immediately afterwards.
 */
class GoogleApiClientActions extends BaseClient {

    private Action1<GoogleApiClient> onClientConnected;
    private Action1<Throwable> onError;

    private GoogleApiClientActions(Context context,
                                   Action1<GoogleApiClient> onClientConnected,
                                   Action1<Throwable> onError,
                                   GoogleApi googleApi) {
        super(context);
        this.onClientConnected = onClientConnected;
        this.onError = onError;

        buildClient(googleApi);
        connect();
    }

    static void create(Context context,
                       GoogleApi googleApi,
                       Action1<GoogleApiClient> onClientConnected,
                       Action1<Throwable> onError) {
        new GoogleApiClientActions(context, onClientConnected, onError, googleApi);
    }

    @Override
    void onClientConnected(GoogleApiClient googleApiClient) {
        onClientConnected.call(googleApiClient);
        disconnect();
    }

    @Override
    void onClientError(Throwable throwable) {
        onError.call(throwable);
    }
}
