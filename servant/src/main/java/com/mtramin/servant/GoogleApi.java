package com.mtramin.servant;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Api;

/**
 * Definition of a Google API
 */
interface GoogleApi {

    /**
     * @return requested API
     */
    @NonNull
    Api api();

    /**
     * @return Options for the API, might be {@code null}
     */
    @Nullable
    Api.ApiOptions.HasOptions options();
}
