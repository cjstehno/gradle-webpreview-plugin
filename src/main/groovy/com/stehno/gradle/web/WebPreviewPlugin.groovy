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

import groovy.transform.TypeChecked
import org.gradle.api.Plugin
import org.gradle.api.Project

import static com.stehno.gradle.web.StartPreviewTask.START_PREVIEW
import static com.stehno.gradle.web.StopPreviewTask.STOP_PREVIEW

/**
 * TODO: document
 */
@TypeChecked
class WebPreviewPlugin implements Plugin<Project> {

    @Override void apply(final Project project) {
        project.extensions.create('webPreview', WebPreviewExtension)

        project.task START_PREVIEW, type: StartPreviewTask, group: 'Documentation', description: 'Starts a simple web preview server to serve local content.'
        project.task STOP_PREVIEW, type: StopPreviewTask, group: 'Documentation', description: 'Stops the preview server, if it is running.'
    }
}
