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
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import static com.stehno.gradle.site.ServerMonitor.sendStopMessage

/**
 * Task used to shutdown the preview server.
 */
@TypeChecked
class StopPreviewTask extends DefaultTask {

    static final String STOP_PREVIEW = 'stopPreview'

    private Integer _monitorPort

    @Input @Optional
    int getMonitorPort() {
        _monitorPort == null ? (project.tasks.findByName('startPreview') as StartPreviewTask).getMonitorPort() : _monitorPort
    }

    void setMonitorPort(int port) {
        this._monitorPort = port;
    }

    @TaskAction void stop() {
        sendStopMessage monitorPort
    }
}
