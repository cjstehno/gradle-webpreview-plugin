/*
 * Copyright (C) 2017 Christopher J. Stehno <chris@stehno.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.gradle.web

/**
 * Gradle DSL extension used to configure the Web Preview plugin.
 */
class WebPreviewExtension {

    /**
     * The web server port where content is to be served. The default is 0, which will choose a random available port. This port
     * will be displayed in the output logs on startup.
     */
    int port = 0

    /**
     * Specifies the directory where the web resources are to be served from. This property is required.
     */
    File resourceDir

    /**
     * Whether or not the server URL is copied to the local clipboard on startup.
     */
    boolean copyUrl = true

    /**
     * Specifies the optional root context path.
     */
    String contextPath
}
