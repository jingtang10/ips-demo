// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath("com.android.tools.build:gradle:8.7.2") // Update to the latest version
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Update to the latest version
  }
}

allprojects {
  repositories {
    // google()
    // mavenCentral()
  }
}

plugins {
  // id("com.google.gms.google-services' version '4.3.10'")
}
