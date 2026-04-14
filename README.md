# bike-va-ai
name: Build APK

on:
  push:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17

      - name: Grant permission
        run: chmod +x gradlew

      - name: Build APK
        run: ./gradlew assembleDebug

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: apk
          path: app/build/outputs/apk/debug/app-debug.apk
include ':app'
plugins {
    id 'com.android.application' version '8.2.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
}
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace "com.example.bikenav"
    compileSdk 34

    defaultConfig {
        applicationId "com.example.bikenav"
        minSdk 24
        targetSdk 34
        versionCode 1
    }
}

dependencies {
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.appcompat:appcompat:1.6.1"
}
package com.example.bikenav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val ai = AIEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val routes = listOf(
            RouteVector(10.0, 5.0, 1.0, 2, 1, 0.8, 1.0),
            RouteVector(8.0, 6.0, 2.0, 5, 3, 0.4, 2.0)
        )

        val best = ai.chooseBest(routes)
        println("BEST ROUTE: $best")
    }
}
package com.example.bikenav

data class RouteVector(
    val time: Double,
    val distance: Double,
    val elevation: Double,
    val lights: Int,
    val crossings: Int,
    val bikeRatio: Double,
    val stress: Double
)

class AIEngine {

    private var weights = mutableMapOf(
        "time" to -1.0,
        "distance" to -0.5,
        "elevation" to -1.2,
        "lights" to -3.0,
        "crossings" to -2.5,
        "bike" to 3.0,
        "stress" to -2.0
    )

    fun score(v: RouteVector): Double {
        return v.time * weights["time"]!! +
                v.distance * weights["distance"]!! +
                v.elevation * weights["elevation"]!! +
                v.lights * weights["lights"]!! +
                v.crossings * weights["crossings"]!! +
                v.bikeRatio * weights["bike"]!! +
                v.stress * weights["stress"]!!
    }

    fun chooseBest(routes: List<RouteVector>): Int {
        return routes.indices.minBy { score(routes[it]) }
    }
}
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:text="Bike Nav AI"
        android:textSize="24sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</LinearLayout>
