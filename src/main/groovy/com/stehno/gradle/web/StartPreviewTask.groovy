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

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.awt.*
import java.awt.datatransfer.StringSelection

/**
 * Task used to start a local preview web server.
 */
class StartPreviewTask extends DefaultTask {

    static final String START_PREVIEW = 'startPreview'

    @TaskAction @SuppressWarnings('GroovyUnusedDeclaration') void start() {
        WebPreviewExtension extension = project.extensions.getByType(WebPreviewExtension)

        assert extension.resourceDir, 'No resourceDir configuration was provided.'

        def server = new Server(extension.port)

        def handler = new ServletContextHandler(ServletContextHandler.SESSIONS)
        handler.contextPath = '/'
        handler.resourceBase = '.'
        handler.addServlet(DefaultServlet, '/').setInitParameter('resourceBase', extension.resourceDir as String)

        server.handler = handler
        server.start()

        String serverUrl = "http://localhost:${server.connectors[0]._localPort}"

        logger.lifecycle 'Started preview server ({}) for {}', serverUrl, extension.resourceDir

        if (extension.copyUrl) {
            Toolkit.defaultToolkit?.systemClipboard?.setContents(new StringSelection(serverUrl), null)
            logger.lifecycle 'Url copied to clipboard.'
        }

        if (extension.runInBackground) {
            logger.lifecycle 'Running in background (to stop run: stopPreview)'
            new ServerMonitor(extension.monitorPort, server).start()
        } else {
            logger.lifecycle('Use CTRL+C to stop.')
            server.join()
        }
    }
}
