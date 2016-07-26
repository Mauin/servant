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

package com.mtramin.servant_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mtramin.servant.GoogleApiClientCompletable;
import com.mtramin.servant.GoogleApiClientSingle;
import com.mtramin.servant.Servant;
import com.mtramin.servant_sampler.R;

import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private Subscription observableClientSubscription;
    private Subscription singleClientSubscription;
    private Subscription completableClientSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serveActionClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        observableClientSubscription = serveObservableClient();
        singleClientSubscription = serveSingleClient();
        completableClientSubscription = serveCompletableClient();
    }

    @Override
    protected void onStop() {
        observableClientSubscription.unsubscribe();
        singleClientSubscription.unsubscribe();
        completableClientSubscription.unsubscribe();
        super.onStop();
    }

    private void serveActionClient() {
        Servant.actions(this, LocationServices.API,
                googleApiClient -> Log.e("Servant", "have action client"),
                throwable -> Log.e("Servant", "error in action client", throwable)
        );
    }

    private Subscription serveObservableClient() {
        return Servant.observable(this, LocationServices.API)
                .subscribe(
                        googleApiClient -> Log.e("Servant", "have observable client"),
                        throwable -> Log.e("Servant", "error in observable client", throwable)
                );
    }

    private Subscription serveSingleClient() {
        return Servant.single(new GoogleApiClientSingle<Boolean>(this) {
            @Override
            protected void onSingleClientConnected(GoogleApiClient googleApiClient) {
                // Do something with the client and call onSuccess / onError
                Log.e("Servant", "have single client");
                onSuccess(true);
            }

            @Override
            protected Api getApi() {
                return LocationServices.API;
            }
        })
                .subscribe(
                        result -> Log.e("Servant", "single result"),
                        throwable -> Log.e("Servant", "single error", throwable)
                );
    }

    private Subscription serveCompletableClient() {
        return Servant.completable(new GoogleApiClientCompletable(this) {
            @Override
            protected void onCompletableClientConnected(GoogleApiClient googleApiClient) {
                // Do something with the client and call onCompleted / onError
                Log.e("Servant", "have completable client");
                onCompleted();
            }

            @Override
            protected Api getApi() {
                return LocationServices.API;
            }
        })
                .subscribe(
                        () -> Log.e("Servant", "completed"),
                        throwable -> Log.e("Servant", "completable error", throwable)
                );
    }
}
