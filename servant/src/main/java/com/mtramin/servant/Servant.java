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

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Action1;

/**
 * Provides multiple ways to easily use the Google Play Services {@link GoogleApiClient}.
 * <p>
 * To create a client and use it callback=style, use {@link #actions(Context, Action1, Action1)}
 * <p>
 * To retrieve a client as an {@link Observable}, use {@link #observable(Context)}.
 * <p>
 * To use a client as a Single, call {@link #single(GoogleApiClientSingle)}
 * <p>
 * To use a client as a Completable, call {@link #completable(GoogleApiClientCompletable)}
 */
public class Servant {

    /**
     * Serve a GoogleApiClient with callback actions
     *
     * @param context           context to use for the client
     * @param api               api to use for the client
     * @param onClientConnected action to perform when the client was connected
     * @param onError           action to perform if the client throws an error
     * @see GoogleApiClientActions
     */
    public static void actions(Context context, Api api, Action1<GoogleApiClient> onClientConnected, Action1<Throwable> onError) {
        GoogleApiClientActions.create(context.getApplicationContext(), api, onClientConnected, onError);
    }

    /**
     * Serve an Observable GoogleApiClient
     *
     * @param context context to use for the client
     * @param api     api to use for the client
     * @return Observable that will emit the client once it was successfully connected
     * @see GoogleApiClientObservable
     */
    public static Observable<GoogleApiClient> observable(Context context, Api api) {
        return GoogleApiClientObservable.create(context.getApplicationContext(), api);
    }

    /**
     * Serve a Single GoogleApiClient
     *
     * @param single single implementation
     * @param <T>    type of return value
     * @return Single that will provide the client
     */
    public static <T> Single<T> single(GoogleApiClientSingle<T> single) {
        return Single.create(single);
    }

    /**
     * Serve a Completable GoogleApiClient
     *
     * @param completable completable implementation
     * @return Completable providing the client
     */
    public static Completable completable(GoogleApiClientCompletable completable) {
        return Completable.create(completable);
    }
}
