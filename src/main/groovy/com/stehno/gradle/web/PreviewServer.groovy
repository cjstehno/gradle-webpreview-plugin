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

import groovy.transform.TypeChecked
import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.PathHandler
import io.undertow.server.handlers.resource.FileResourceManager
import io.undertow.server.handlers.resource.ResourceHandler

/**
 * Manager for a singleton-base preview server.
 */
@TypeChecked @Singleton
class PreviewServer {

    private Undertow server
    private String path

    /**
     * Starts the server instance on the specified port and resource directory, if it is not already running.
     *
     * @param port the server port
     * @param directory the resource directory to be served
     * @param contextPath the optional root context path
     */
    void start(final int port, final File directory, final String contextPath = null) {
        if (!server) {
            this.path = contextPath

            HttpHandler handler = new ResourceHandler(new FileResourceManager(directory, 1))

            if (contextPath) {
                PathHandler pathHandler = new PathHandler()
                pathHandler.addPrefixPath(contextPath, handler)
                handler = pathHandler
            }

            server = Undertow.builder().addHttpListener(port, '0.0.0.0').setHandler(handler).build()
            server.start()
        }
    }

    int getPort() {
        server ? (server.listenerInfo[0].address as InetSocketAddress).port : -1
    }

    String getUrl() {
        server ? "http://localhost:$port$contextPath" : null
    }

    String getContextPath() {
        return path ? (path.startsWith('/') ? path : "/$path") : ''
    }

    void stop() {
        server?.stop()
        server = null
    }
}
