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

import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static com.stehno.gradle.web.ServerMonitor.stopServer

class PreviewTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    def 'start/stop preview server'() {
        given:
        Random random = new Random()

        def portRange = 2048..20202

        int monitorPort = portRange[random.nextInt(portRange.size())]
        int serverPort = monitorPort + 1

        buildFile(extension: """
            webPreview {
                port = $serverPort
                monitorPort = $monitorPort
                resourceDir = file('src/site')
            }
        """)

        String content = 'This is some really cool web content!'

        File siteDir = projectRoot.newFolder('src', 'site')
        new File(siteDir, 'index.html').text = content

        when: 'the start task is run'
        BuildResult result = gradleRunner(['startPreview', '--stacktrace']).build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the content is accessible'
        "http://localhost:$serverPort/index.html".toURL().text == content

        when: 'the stop task is run'
        result = gradleRunner(['stopPreview']).build()

        // there is a bit of a lag between stop command and actual stop
        sleep 2000

        then: 'the build is successful'
        totalSuccess result

        when:
        String text = "http://localhost:$serverPort/index.html".toURL().text

        then:
        !text
        thrown(Exception)

        cleanup: 'make sure everything is shutdown'
        stopServer monitorPort
    }

    @Override
    String getBuildTemplate() {
        '''
            plugins {
                id 'com.stehno.gradle.webpreview'
            }
            repositories {
                jcenter()
            }
            ${config.extension ?: ''}
        '''
    }
}
