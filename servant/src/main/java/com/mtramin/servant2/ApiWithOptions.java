package com.mtramin.servant2;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.Api;

/**
 * Definition of a Google API that provides more options
 */
class ApiWithOptions implements GoogleApi {
    private final Api api;
    private final Api.ApiOptions.HasOptions options;

    ApiWithOptions(Api api, Api.ApiOptions.HasOptions options) {
        this.api = api;
        this.options = options;
    }

    @Override
    @NonNull
    public Api api() {
        return api;
    }

    @Override
    @Nullable
    public Api.ApiOptions.HasOptions options() {
        return options;
    }
}
