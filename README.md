<p align="center">
  <img src="https://media.onesignal.com/cms/Website%20Layout/logo-red.svg"/>
</p>

## Setup

Install clang-format (21.1.3) e.g. `brew install clang-format`.

### OneSignal Cordova Push Notification Plugin

[![npm version](https://img.shields.io/npm/v/onesignal-cordova-plugin.svg)](https://www.npmjs.com/package/onesignal-cordova-plugin) [![npm downloads](https://img.shields.io/npm/dm/onesignal-cordova-plugin.svg)](https://www.npmjs.com/package/onesignal-cordova-plugin)

---

#### ⚠️ Migration Advisory for current OneSignal customers

Our new [user-centric APIs and v5.x.x SDKs](https://onesignal.com/blog/unify-your-users-across-channels-and-devices/) offer an improved user and data management experience. However, they may not be at 1:1 feature parity with our previous versions yet.

If you are migrating an existing app, we suggest using iOS and Android’s Phased Rollout capabilities to ensure that there are no unexpected issues or edge cases. Here is the documentation for each:

- [iOS Phased Rollout](https://developer.apple.com/help/app-store-connect/update-your-app/release-a-version-update-in-phases/)
- [Google Play Staged Rollouts](https://support.google.com/googleplay/android-developer/answer/6346149?hl=en)

If you run into any challenges or have concerns, please contact our support team at support@onesignal.com

---

[OneSignal](https://onesignal.com/) is a free email, sms, push notification, and in-app message service for mobile apps. This plugin makes it easy to integrate your [Cordova](http://cordova.apache.org/) based (e.g. [Ionic](http://ionicframework.com/), [PhoneGap](https://phonegap.com/), and PhoneGap Build app with OneSignal.

<p align="center"><img src="https://app.onesignal.com/images/android_and_ios_notification_image.gif" width="500" alt="Cordova Notification"></p>

#### Installation and Setup

See the [Documentation](https://documentation.onesignal.com/docs) for installation and setup instructions:

- Cordova: https://documentation.onesignal.com/docs/cordova-sdk-setup
- Ionic: https://documentation.onesignal.com/docs/ionic-sdk-setup

##### Cordova

Install this package with the Cordova CLI and prepare the native platforms as usual. The existing Cordova `plugin.xml` integration remains supported and continues to expose the public JavaScript API through `window.cordova.exec`.

##### Capacitor without Cordova

Capacitor apps can install this package directly without adding Cordova platform packages:

1. Install `onesignal-cordova-plugin` in the Capacitor app.
2. Run the Capacitor sync command for the target platforms, for example `npx cap sync ios` and/or `npx cap sync android`.
3. Keep using the same JavaScript API, such as `OneSignal.initialize(...)` and `OneSignal.Notifications.addEventListener(...)`.

At runtime the SDK uses Cordova when `window.cordova.exec` exists. Otherwise, it detects the registered Capacitor `OneSignalPush` native plugin and routes calls through Capacitor. If neither native bridge is available, the SDK throws a clear setup error.

#### API

See OneSignal's [Client SDK Reference](https://documentation.onesignal.com/docs/sdk-reference) page for a list of all available methods.

#### Change Log

See this repository's [release tags](https://github.com/OneSignal/OneSignal-Cordova-SDK/releases) for a complete change log of every released version.

#### Support

Please visit this repository's [Github issue tracker](https://github.com/OneSignal/OneSignal-Cordova-SDK/issues) for feature requests and bug reports related specificly to the SDK.
For account issues and support please contact OneSignal support from the [OneSignal.com](https://onesignal.com) dashboard.

#### Demo Projects

To make things easier, we have published an Ionic Capacitor React demo app in the `/example` folder of this repository.

Legacy (Player Model) demo projects:

- [Cordova](https://github.com/OneSignal/OneSignal-Cordova-Example)
- [Ionic](https://github.com/OneSignal/OneSignal-Ionic-Example)

#### Supports:

- Cordova, Ionic, Ionic Capacitor, and Phonegap
- Android 4.1 (API Level 16) through 12 (API Level 31), and Amazon FireOS
- iOS 9 - 15
