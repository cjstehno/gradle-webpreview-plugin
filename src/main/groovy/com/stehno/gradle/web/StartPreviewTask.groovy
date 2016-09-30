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
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

import java.awt.*
import java.awt.datatransfer.StringSelection

/**
 * Task used to start a local preview web server.
 */
@TypeChecked
class StartPreviewTask extends DefaultTask {

    static final String START_PREVIEW = 'startPreview'

    @Input @Optional int port = 0
    @Input @Optional int monitorPort = 10101
    @Input @Optional boolean runInBackground = true
    @Input File resourceDir
    @Input @Optional boolean copyUrl = true

    @TaskAction void start() {
        def server = new Server(port)

        def handler = new ServletContextHandler(ServletContextHandler.SESSIONS)
        handler.contextPath = '/'
        handler.resourceBase = '.'
        handler.addServlet(DefaultServlet, '/').setInitParameter('resourceBase', resourceDir as String)

        server.handler = handler
        server.start()

        int actualPort = server.connectors[0].localPort
        logger.lifecycle 'Started preview server (http://localhost:{})...', actualPort

        if (copyUrl) {
            Toolkit.defaultToolkit?.systemClipboard?.setContents(new StringSelection("http://localhost:$actualPort"), null)
        }

        if (runInBackground) {
            new ServerMonitor(monitorPort, server).start()
        } else {
            server.join()
        }
    }
}
