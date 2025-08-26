## 🚀 Getting Started

### Prerequisites

- Recent Android Studio/IntelliJ IDEA to support modern versions of KMP
- Xcode (for iOS development)
- Node.js (for web development)

### local.properties

Some environment variables are needed for the project to function, create a file `local.properties`
in the project root and fill out the required details

```
# Dependencies
gpr.user=[GitHub Username]
gpr.token=[GitHub Personal Access Token with access to read:packages]

# AWS dev environment

region.dev=eu-central-1
clientid.dev=
hosturl.dev=[id].execute-api.eu-central-1.amazonaws.com
baseurl.dev=https://[id].execute-api.eu-central-1.amazonaws.com/dev/
x.api.key.dev=

# AWS prod environment

region.prod=eu-central-1
clientid.prod=
hosturl.prod=[id].execute-api.eu-central-1.amazonaws.com
baseurl.prod=https://[id].execute-api.eu-central-1.amazonaws.com/prod/
x.api.key.prod=
```

## Running the Android app

- Android Studio -> File -> Open -> root directory of the project
- Wait for gradle to sync (this will take a while and probably download the internet)
- In the top bar there should be build/run configs toward the right of the screen (with the "Play" button)
  - From the drop down, select Android
  - If there is no emulator, create one in the "Device Manager" in the right navigation rail
- Once there is an emulator or device connected, the "Play" button should be active, press it to build/start the app

## 📁 Project Structure

The project follows a modular architecture. The scaffold and core navigation is in `cmp-navigation`, it holds the navigation routes which call screen/feature specific business logic which are in `feature/*`. 

For example, when the app is started and there is no authentication data (which is resolved from the repository in `core/data`), the unauthenticated graph will compose `feature/auth/.../AuthScreen`. If the user is authenticated the authenticated graph will compose `feature/feed/.../FeedScreen`.

### Overview:

- **Platform Modules**: `cmp-android`, `cmp-ios` (temperamental), `cmp-desktop` (partially functional), `cmp-web` (untested)
- **Core Modules**: Common, reusable components shared across all features
- **Feature Modules**: Self-contained feature implementations
- **Build Logic**: Custom Gradle plugins and build configuration

### Noteworthy modules
- **core/designsystem**: has the theming logic and theme definitions
- **core/network**: has the authentication implementation, endpoint definitions and httpClient initialization
- **core/config**: contains the logic, flavors and tabs for creating an app for a specific artist. It may be possible to pull the resource up to here from `cmp-apps/cmp-android`. Each actual of `AppConfig.AppOnboardingConfig` has the implementation for fetching resources for the intro/login screens. it's platform specific so the Android resources can be fetched from the flavors in `cmp-android` as other platforms don't support "flavors" (yet)
- **cmp-navigation**: discussed above
- **cmp-shared**: the main entry point for multi platform code

## Artist apps

Each artist app requires:
 
 - Flavor definition in `build-logic/convention/.../convention/AppFlavor.kt
 - Flavor folder with resources in `cmp-apps/cmp-android/src` (e.g. `cmp-apps/cmp-android/src/enslaved`)
 - Tabs definition in `core/config/src/android[artistName]/res/raw` 

## Release process

 Section to be finalized once there is a place to store the secrets and keystores. 
 
 There is a fastlane script to build all the app flavors (`fastlane android assembleReleaseApks`) however it depends on secrets and keystores. They can be created by `keystore-manager.sh`, but:

 - they shouldn't be checked into change control
 - they can't be created every time we check out the code
 - there should probably be a keystore per artist app, so we need to 
   - adjust the build logic to be able to fetch the store for the flavour being built etc 
   - make some decisions about where to store the keys