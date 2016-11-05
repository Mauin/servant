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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mtramin.servant.GoogleApiClientCompletable;
import com.mtramin.servant.GoogleApiClientSingle;
import com.mtramin.servant.Servant;
import com.mtramin.servant_sampler.R;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private Disposable observableClientDisposable;
    private Disposable singleClientDisposable;
    private Disposable completableClientDisposable;
    private Disposable singleClintWithOptionsDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serveActionClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        observableClientDisposable = serveObservableClient();
        singleClientDisposable = serveSingleClient();
        singleClintWithOptionsDisposable = serveSingleClientWithOptions();
        completableClientDisposable = serveCompletableClient();
    }

    @Override
    protected void onStop() {
        observableClientDisposable.dispose();
        singleClientDisposable.dispose();
        singleClintWithOptionsDisposable.dispose();
        completableClientDisposable.dispose();
        super.onStop();
    }

    private void serveActionClient() {
        Servant.actions(this, LocationServices.API,
                googleApiClient -> Log.e("Servant", "have action client"),
                throwable -> Log.e("Servant", "error in action client", throwable)
        );
    }

    private Disposable serveObservableClient() {
        return Servant.observable(this, LocationServices.API)
                .subscribe(
                        googleApiClient -> Log.e("Servant", "have observable client"),
                        throwable -> Log.e("Servant", "error in observable client", throwable)
                );
    }

    private Disposable serveSingleClient() {
        return Servant.single(new GoogleApiClientSingle<Boolean>(this, LocationServices.API) {
            @Override
            protected void onSingleClientConnected(GoogleApiClient googleApiClient) {
                // Do something with the client and call onSuccess / onError
                Log.e("Servant", "have single client");
                onSuccess(true);
            }
        })
                .subscribe(
                        result -> Log.e("Servant", "single result"),
                        throwable -> Log.e("Servant", "single error", throwable)
                );
    }

    private Disposable serveSingleClientWithOptions() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        return Servant.single(new GoogleApiClientSingle<Intent>(this,
                Auth.GOOGLE_SIGN_IN_API,
                googleSignInOptions) {
            @Override
            protected void onSingleClientConnected(GoogleApiClient googleApiClient) {
                // Do something with the client and call onSuccess / onError
                Log.e("Servant", "have single sign-in client");
                onSuccess(Auth.GoogleSignInApi.getSignInIntent(googleApiClient));
            }
        })
                .subscribe(
                        result -> {
                            Log.e("Servant", "single sign-in result");
                            startActivityForResult(result, 10293);
                        },
                        throwable -> Log.e("Servant", "single sign-in error", throwable)
                );
    }

    private Disposable serveCompletableClient() {
        return Servant.completable(new GoogleApiClientCompletable(this, LocationServices.API) {
            @Override
            protected void onCompletableClientConnected(GoogleApiClient googleApiClient) {
                // Do something with the client and call onComplete / onError
                Log.e("Servant", "have completable client");
                onCompleted();
            }
        })
                .subscribe(
                        () -> Log.e("Servant", "completed"),
                        throwable -> Log.e("Servant", "completable error", throwable)
                );
    }
}
