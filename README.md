# Gradle Web Preview Plugin

A Gradle plugin to help you preview site documentation or other web content locally.

## Installation

You can apply the plugin using the `buildscript` block:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "gradle.plugin.com.stehno.gradle:webpreview:0.1.0"
      }
    }
    
    apply plugin: "com.stehno.gradle.webpreview"
    
Or the newer `plugins` block:

    plugins {
      id "com.stehno.gradle.webpreview" version "0.1.0"
    }
    
## Tasks

### `startPreview`

> TODO: document

### `stopPreview`

> TODO: document