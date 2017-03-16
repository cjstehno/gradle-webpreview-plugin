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
import groovy.util.logging.Slf4j
import org.eclipse.jetty.server.Server

/**
 * Monitor thread used to store a reference to an active preview server.
 */
@TypeChecked @Slf4j
class ServerMonitor extends Thread {

    private static final byte SHUTDOWN_BYTE = (byte) 42
    private final Server server
    private ServerSocket serverSocket

    ServerMonitor(final int port, final Server server) {
        this.server = server

        daemon = true
        setName('PreviewServerMonitor')

        serverSocket = new ServerSocket(port, 1, InetAddress.getByName('0.0.0.0'))
        serverSocket.reuseAddress = true
    }

    public void run() {
        while (serverSocket) {
            try {
                serverSocket.accept().withCloseable { Socket socket ->
                    socket.setSoLinger(false, 0)

                    socket.inputStream.withStream { instream ->
                        DataInputStream input = new DataInputStream(instream)

                        if (input.readByte() == SHUTDOWN_BYTE) {
                            socket.close()
                            serverSocket.close()
                            serverSocket = null
                            server.stop()
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace()
            }
        }
    }

    static void stopServer(final int monitorPort = 10101) {
        Socket socket = null
        try {
            socket = new Socket(InetAddress.getLocalHost(), monitorPort)
            socket.setSoLinger(false, 0)

            socket.outputStream.withStream { out ->
                out.write(SHUTDOWN_BYTE)
                out.flush()
            }

        } catch (Exception ex) {
            // nothing
        } finally {
            socket?.close()
        }
    }
}
