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

class PreviewTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    def 'start/stop preview server'() {
        given:
        Random random = new Random()

        def portRange = 10101..20202
        int serverPort = portRange[random.nextInt(portRange.size())]

        buildFile(extension: """
            webPreview {
                port = $serverPort
                resourceDir = file('src/site')
                copyUrl = false
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

        when: 'the status is requested'
        result = gradleRunner(['previewStatus']).build()

        then: 'the status is returned'
        totalSuccess result
        result.output.contains "The preview server is running at http://localhost:$serverPort."

        when: 'the stop task is run'
        result = gradleRunner(['stopPreview']).build()

        then: 'the build is successful'
        totalSuccess result

        when: 'the status is requested after shutdown'
        result = gradleRunner(['previewStatus']).build()

        then: 'the status is returned'
        totalSuccess result
        result.output.contains "The preview server is not running."

        when:
        String text = "http://localhost:$serverPort/index.html".toURL().text

        then:
        !text
        thrown(Exception)

        cleanup: 'make sure everything is shutdown'
        PreviewServer.instance.stop()
    }

    def 'preview server with context path'(){
        setup:
        Random random = new Random()

        def portRange = 10101..20202
        int serverPort = portRange[random.nextInt(portRange.size())]

        buildFile(extension: """
            webPreview {
                port = $serverPort
                resourceDir = file('src/site')
                copyUrl = false
                contextPath = 'foo'
            }
        """)

        String content = 'This is some really cool web content!'

        File siteDir = projectRoot.newFolder('src', 'site')
        new File(siteDir, 'index.html').text = content

        when:
        BuildResult result = gradleRunner(['startPreview']).build()

        then:
        totalSuccess result

        and:
        "http://localhost:$serverPort/foo/index.html".toURL().text == content

        when:
        "http://localhost:$serverPort/index.html".toURL().text != content

        then:
        thrown(FileNotFoundException)

        cleanup:
        PreviewServer.instance.stop()
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
