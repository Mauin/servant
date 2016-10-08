package com.mtramin.servant;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Definition of a Google API that does not provide/need any options
 */
class ApiDefinition implements GoogleApi {

    private com.google.android.gms.common.api.Api api;

    ApiDefinition(com.google.android.gms.common.api.Api api) {
        this.api = api;
    }

    @Override
    @NonNull
    public com.google.android.gms.common.api.Api api() {
        return api;
    }

    @Override
    @Nullable
    public com.google.android.gms.common.api.Api.ApiOptions.HasOptions options() {
        return null;
    }
}
