# Gradle Web Preview Plugin

[![Build Status](https://travis-ci.org/cjstehno/gradle-webpreview-plugin.svg?branch=master)](https://travis-ci.org/cjstehno/gradle-webpreview-plugin) [![Coverage Status](https://coveralls.io/repos/github/cjstehno/gradle-webpreview-plugin/badge.svg?branch=master)](https://coveralls.io/github/cjstehno/gradle-webpreview-plugin?branch=master)

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
        classpath "gradle.plugin.com.stehno.gradle:webpreview:0.3.0"
      }
    }
    
    apply plugin: "com.stehno.gradle.webpreview"
    
Or the newer `plugins` block:

    plugins {
      id "com.stehno.gradle.webpreview" version "0.3.0"
    }
    
## Usage

## DSL Extension

To configure the Web Preview plugin, the `webPreview` extension is provided:

    webPreview {
        port = 0
        copyUrl = true
        resourceDir = file('build/web')
        contextPath = '/'
    }

The `port` property is the web server port where content is to be served. The default is `0`, which will choose a random available port. This port 
will be displayed in the output logs on startup.

The `resourceUrl` property is used to specify the directory where the web resources are to be served from. This property is required and the server
will fail to start without it.

The `copyUrl` property determines whether or not the server URL is copied to the local clipboard on startup. This is handy when running locally 
with the random port setting.

The `contextPath` is an optional property used to define the root context path to be used as a prefix for all content. If not specified the server 
root "/" will be used.

### Tasks

* `startPreview` - used to start the preview server with the configuration provided in the `webPreview` extension.
* `previewStatus` - used to display the status of the preview server. 
* `stopPreview` - used to stop the preview server when it is running in background mode.

## Building

You can build the project with Gradle:

    ./gradlew clean build

> Note: if you have the preview server running during a build/test of the plugin, you may get test failures. Shut down the preview server and try 
building again.
