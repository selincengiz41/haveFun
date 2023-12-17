# <p align="center"> HaveFun  😂 </p>


This application is developed to enable individuals to track and participate in events on the map.

<!-- Screenshots -->
## 📸 Screenshots
<p align="center">
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/1e5d3ef1-6cc3-4f0a-a4d0-c745523b2dcb" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/2b3cd975-05fe-4552-978f-a782efa948da" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/9bb3d172-ea76-4e5b-9f0e-4281ef3a3010" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/aca593df-ef32-4ef8-aa77-11c67b43e8ef" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/7bd04936-b724-4e50-8ca4-c4557d3c714a" width="130" height="300"/> 
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/ebbdd713-af5c-430d-9d9f-3bcbeb92c0e8" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/fd5bd76b-e831-468a-a054-04b009ea93b4" width="130" height="300"/>
  <img src="https://github.com/selincengiz41/haveFun/assets/60012262/3fc3f13b-67d5-4b5b-89ac-63ffc90f6746" width="130" height="300"/> 
  
 


</p>




## :point_down: Structures 
- MVVM
- Firebase 
- Navigation
- Hilt
- Coroutines
- Retrofit
- Data Binding 
- Glide
- SDP/SSP Library
- Chucker
- Leku
- Animationed Drawer


## :pencil2: Dependency
```
    dependencies {
  // SSP-SDP library
    implementation 'com.intuit.ssp:ssp-android:1.1.0'
    implementation 'com.intuit.sdp:sdp-android:1.1.0'

    // Navigation
    implementation 'androidx.navigation:navigation-fragment-ktx:2.6.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.6.0'

    //Retrofit
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"

    //Glide
    implementation "com.github.bumptech.glide:glide:4.15.1"


    //Roundable Layout
    implementation 'com.github.zladnrms:RoundableLayout:1.1.4'

    //Lottie
    implementation 'com.airbnb.android:lottie:6.1.0'

    //Hilt
    implementation 'com.google.dagger:hilt-android:2.47'
    kapt 'com.google.dagger:hilt-compiler:2.47'

    // Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation platform('com.google.firebase:firebase-bom:32.2.2')
    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth-ktx")
    //   // Declare the dependency for the Cloud Firestore library
    implementation("com.google.firebase:firebase-firestore-ktx")

    //Location
    implementation 'com.google.android.gms:play-services-location:21.0.1'


    //Leku
    implementation('com.adevinta.android:leku:11.0.0') {
        exclude group: 'com.google.android.gms'
        exclude group: 'androidx.appcompat'
    }

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    //Drawer
    implementation 'com.github.mindinventory:minavdrawer:1.2.1'

    //Chucker
    implementation("com.github.chuckerteam.chucker:library:4.0.0")



}
```

app build.gradle:

```
plugins {
 id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
    id 'kotlin-parcelize'
    id 'androidx.navigation.safeargs'
    //For the annotations
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

buildFeatures {
      dataBinding = true
 }
```
project build.gradle:

```
plugins {
      id 'com.android.application' version '8.0.2' apply false
    id 'com.android.library' version '8.0.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.8.20' apply false
    id 'com.google.gms.google-services' version '4.3.15' apply false
    id 'androidx.navigation.safeargs.kotlin' version '2.5.1' apply false
    id 'com.google.dagger.hilt.android' version "2.44" apply false
}
```
