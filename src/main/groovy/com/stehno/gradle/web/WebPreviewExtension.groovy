/*
 * Copyright (C) 2016 Christopher J. Stehno <chris@stehno.com>
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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Gradle DSL extension used to configure the Web Preview plugin.
 */
class WebPreviewExtension {

    @Input @Optional int port = 0
    @Input @Optional int monitorPort = 10101
    @Input @Optional boolean runInBackground = true
    @Input File resourceDir
    @Input @Optional boolean copyUrl = true
}
