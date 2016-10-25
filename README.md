# Servant: Serving you GoogleApiClients so you don't have to worry about them

Servant will create and manage GoogleApiClient from Google Play Services for you so you can
focus on the important actions and requests you want to perform with them.

Servant is especially useful if you want to use the GoogleApiClients in a reactive manner with
RxJava.

## Set-up

To use Servant in your project, add the library as a dependency in your `build.gradle` file:
```groovy
dependencies {
    compile 'com.mtramin:servant:9.8.0.0'
}
```

To make it easier to recognize which version of Google Play Services is used Servants version
 number will always begin with the used Google Play Services version. It is recommended to always
 use the version of Servant that matches the version of the Google Play Services you use in your
 application.

### MinSdk

This libraries minSdkVersion is 15.

### Permissions

Depending on your usage of the GoogleApiClients you might also have to declare some permissions
 in your `AndroidManifest.xml`. Remember that from Android Marshmallow (API 23) you will have to
 support runtime permissions for those

## Using Servant

Servant serves you GoogleApiClients in multiple formats:
- With Actions as callbacks
- Observable
- Single
- Completable


With action Callbacks:
``` java
Servant.actions(this, LocationServices.API,
        googleApiClient -> Log.e("Servant", "have action client"),
        throwable -> Log.e("Servant", "error in action client", throwable)
);
```

Observable client:
``` java
Servant.observable(this, LocationServices.API)
        .subscribe(
                googleApiClient -> Log.e("Servant", "have observable client"),
                throwable -> Log.e("Servant", "error in observable client", throwable)
        );
```

To use Servant to serve you GoogleApiClients as Single or Completable, simply call
``` java
Servant.single(/* implement GoogleApiClientSingle */)
Servant.completable(/* implement GoogleApiClientCompletable */)
```

## Dependencies
[![Method count](https://img.shields.io/badge/Methods count-core: 130 | deps: 21733-e91e63.svg)](http://www.methodscount.com/?lib=com.mtramin%3Aservant%3A9.6.1.1)

Servant brings the following dependencies:

- RxJava (v1.x)
- Google Play Services (base) which provides GoogleApiClient

Due to the Google Play Services dependency also the method count of the library seems quite high.
However since you will include Google Play Services in your application either way this does not really affect your method count.
Servant only relies on the Google Play Services base package to have as little impact as possible.

In total Servant itself has a method count of below 150 methods.

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/mauin/Servant/issues).

## LICENSE

Copyright 2016 Marvin Ramin.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.