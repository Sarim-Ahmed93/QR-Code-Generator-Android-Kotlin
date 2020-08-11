# QR-Code-Generator-Android-Kotlin
**Simple Android application for generating QR code in Kotlin usin Kenglxn Library.**

**app-level build.gradle:**
```
android
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
}
```

**project-level build.gradle:**

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

![Test Image 1](https://github.com/Sarim-Ahmed93/QR-Code-Generator-Android-Kotlin/blob/master/device-2020-08-10-022305.png)
![Test Image 1](https://github.com/Sarim-Ahmed93/QR-Code-Generator-Android-Kotlin/blob/master/device-2020-08-10-022336.png)
