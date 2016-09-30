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
import groovy.util.logging.Slf4j
import org.eclipse.jetty.server.Server

/**
 * Monitor thread used to store a reference to an active preview server.
 */
@TypeChecked @Slf4j
class ServerMonitor extends Thread {

    static final byte MAGIC_BYTE = (byte) 42
    private final Server server
    private ServerSocket serverSocket

    ServerMonitor(final int port, final Server server) {
        this.server = server

        daemon = true
        setName('PreviewServerMonitor')

        serverSocket = new ServerSocket(port, 1, InetAddress.getByName('127.0.0.1'))
        serverSocket.reuseAddress = true
    }

    public void run() {
        while (serverSocket) {
            Socket socket = null
            try {
                socket = serverSocket.accept()
                socket.setSoLinger(false, 0)

                socket.inputStream.withStream { instream ->
                    DataInputStream input = new DataInputStream(instream)

                    if (input.readByte() == MAGIC_BYTE) {
                        try {
                            socket.close()
                        } catch (Exception e) {
                            e.printStackTrace()
                        }
                        try {
                            socket.close()
                        } catch (Exception e) {
                            e.printStackTrace()
                        }
                        try {
                            serverSocket.close()
                        } catch (Exception e) {
                            e.printStackTrace()
                        }

                        serverSocket = null

                        try {
                            server.stop()
                        } catch (Exception e) {
                            // nothing?
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                if (socket != null) {
                    try {
                        socket.close()
                    } catch (Exception e) {
                        // nothing?
                    }
                }
                socket = null;
            }
        }
    }

    static void sendStopMessage(final int monitorPort) {
        try {
            Socket socket = new Socket(InetAddress.getByName('127.0.0.1'), monitorPort)
            socket.setSoLinger(false, 0)

            socket.outputStream.withStream { out ->
                out.write(MAGIC_BYTE)
                out.flush()
            }
            socket.close()

        } catch (ConnectException e) {
            log.info 'Preview server is not running.'
        } catch (Exception e) {
            log.warn 'Problem stopping preview server: {}', e.message
        }
    }
}
