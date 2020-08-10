# QR-Code-Generator-Android-Kotlin
**Simple Android application for generating QR code in Kotlin usin QRGen Library.**

**app-level build.gradle:

`android
{
compileOptions
            {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }
}
dependencies 
{
    implementation 'com.github.kenglxn.qrgen:android:2.6.0'
}`

**project-level build.gradle:

`allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}`
