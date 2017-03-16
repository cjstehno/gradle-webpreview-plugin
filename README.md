# Gradle Web Preview Plugin

A Gradle plugin to help you preview site documentation or other web content locally. When run, this plugin starts an embedded Jetty server against
the configured local content so that you can preview local web content, whether it is documentation or a project web site.

This plugin is NOT intended for use as a general web server, if that is what you are looking for, please check out one of the following:

* [Gradle Jetty Plugin](https://docs.gradle.org/current/userguide/jetty_plugin.html)
* [Gradle Tomcat Plugin](https://plugins.gradle.org/plugin/com.bmuschko.tomcat)

## Installation

You can apply the plugin using the `buildscript` block:

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "gradle.plugin.com.stehno.gradle:webpreview:0.1.2"
      }
    }
    
    apply plugin: "com.stehno.gradle.webpreview"
    
Or the newer `plugins` block:

    plugins {
      id "com.stehno.gradle.webpreview" version "0.1.2"
    }
    
## Usage

## DSL Extension

To configure the Web Preview plugin, the `webPreview` extension is provided:

    webPreview {
        port = 0
        monitorPort = 10101
        runInBackground = true
        copyUrl = true
        resourceDir = file('build/web')
    }

The `port` property is the web server port where content is to be served. The default is `0`, which will choose a random available port. This port 
will be displayed in the output logs on startup.

The `monitorPort` property is the port used by the background monitor. Defaults to `10101`.

The 'runInBackground' property specifies whether or not the server should be run in the background or foreground. Defaults to `true`.

The `resourceUrl` property is used to specify the directory where the web resources are to be served from. This property is required and the server
will fail to start without it.

The `copyUrl` property determines whether or not the server URL is copied to the local clipboard on startup. This is handy when running locally 
with the random port setting.

### `startPreview`

The `startPreview` task is used to start the preview server with the configuration provided in the `webPreview` extension. If the server is started
in background mode, the `stopPreview` task will need to be executed in order to stop the server, otherwise, the build will run until `CTRL+C` is 
executed.

### `stopPreview`

The `stopPreview` task is used to stop the preview server when it is running in background mode.

## Building

You can build the project with Gradle:

    ./gradlew clean build

Note that if you are using this plugin in other builds, some of the tests may fail (and kill your running preview servers).
