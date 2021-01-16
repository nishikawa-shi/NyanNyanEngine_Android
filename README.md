# NyanNyanEngine Android

<p align="center"><img src="https://user-images.githubusercontent.com/38374045/104811855-c4a1f900-5841-11eb-985e-b0ae59e09bc8.png" width="180"></p>

## Concepts

Meow:)

The funny Twitter client android app. Every tweet you post and see are converted into CAT Language (irreversible), meow. So, you no longer have to be nervous about what to say. Only you have to do is to be cuuuute, Nyaaaaan.

## Designs

<p align="center">
	<img width="180 alt="image" src="https://user-images.githubusercontent.com/38374045/104811999-940e8f00-5842-11eb-889c-3c758e5e080c.png">
	<img width="180" alt="image" src="https://user-images.githubusercontent.com/38374045/104812008-a38dd800-5842-11eb-9f0a-7066c4977b70.png">
</p>

## Architecture

Mainly conform to MVVM architectures using [Android Architecture Components](https://developer.android.com/topic/libraries/architecture). For further information, see [app/build.gradle](app/build.gradle).

### Environments

- Android Studio 4.1.1

### Structure

Each classes in the same layer are independent.

```
└── app/src
    ├── main/.../nyannyanengine_android
    │   ├── ApplicationBindsModule.kt -> knowing how to make instances.
    │   ├── ...
    │   ├── model
    │   │   ├── dao -> knowing how to access data.
    │   │   ├── repository -> depends on dao. knowing how to save/load data.
    │   │   ├── usecase -> depends on repository. knowing which datas are required for any events.
    │   │   ├── config
    │   │   └── entity
    │   └── ui
    │       ├── main
    │       │   ├── MainViewModel.kt -> depends on usecase. knowing which actions to be seen.
    │       │   ├── MainFragment.kt -> depends on view model. knowing how to display actions. 
    │       │   └── ...
    │       ├── post_nekogo
    │       └── ...
    └── test/.../nyannyanengine_android/ -> unit tests for view models to daos.
```

### Test execution

```sh
./gradlew check
```

### Data source

#### Global

[Twitter APIs](https://developer.twitter.com/en/docs) are loaded directry using HMAC-SHA1 Auth. The endpoints used here are shown below.

- [POST oauth/request_token](https://developer.twitter.com/en/docs/authentication/api-reference/request_token)
- [POST oauth/access_token](https://developer.twitter.com/en/docs/authentication/api-reference/access_token)
- [POST oauth/invalidate_token](https://developer.twitter.com/en/docs/authentication/api-reference/invalidate_access_token)
- [GET account/verify_credentials](https://developer.twitter.com/en/docs/twitter-api/v1/accounts-and-users/manage-account-settings/api-reference/get-account-verify_credentials)
- [GET statuses/home_timeline](https://developer.twitter.com/en/docs/twitter-api/v1/tweets/timelines/api-reference/get-statuses-home_timeline)
- [POST statuses/update](https://developer.twitter.com/en/docs/twitter-api/v1/tweets/post-and-engage/api-reference/post-statuses-update)

#### Individual

[Firebase](https://firebase.google.com) is used. For authentication, Firebase Auth. For data store, Cloud Firestore.

### CI/CD

[Azure Pipelines](https://azure.microsoft.com/en-us/services/devops/pipelines/) is used. Some secrets file is managed using the Library feature.

### CAT Language (Nekogo) conversion

Every tweets is converted with one-way hash function (MD5). Then, some parts of number are picked and replaced with cat meowing.
