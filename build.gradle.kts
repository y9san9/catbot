plugins {
    // k-mpp, k-js, k-jvm, k-android-app or k-android-library
    id(`k-jvm`)
}

group = AppInfo.PACKAGE
version = AppInfo.VERSION

allprojects {
    repositories {
        mavenCentral()
    }
}
