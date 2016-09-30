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
package com.stehno.gradle.site

import com.stehno.vanilla.test.PropertyRandomizer
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

import static com.stehno.gradle.site.ServerMonitor.sendStopMessage
import static com.stehno.vanilla.test.PropertyRandomizer.randomize
import static com.stehno.vanilla.test.Randomizers.forString

class PreviewTaskSpec extends Specification implements UsesGradleBuild {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()

    private static final PropertyRandomizer stringRando = randomize(String) {
        typeRandomizer String, forString(100..200)
    }

    @Ignore('This test is a bit flaky - it works in an installed project') // TODO: make this work
    def 'start/stop preview server'() {
        given:
        buildFile(extension: '''
            startPreview {
                port = 8080
            }
        ''')

        String content = stringRando.one() as String

        File siteDir = projectRoot.newFolder('src', 'site')
        new File(siteDir, 'index.html').text = content

        when: 'the start task is run'
        BuildResult result = gradleRunner(['startPreview', '--stacktrace']).build()

        then: 'the build is successful'
        totalSuccess result

        and: 'the content is accessible'
        'http://localhost:8080/index.html'.toURL().text == content

        when: 'the stop task is run'
        result = gradleRunner(['stopPreview']).build()

        then: 'the build is successful'
        totalSuccess result

        when:
        'http://localhost:8080/index.html'.toURL().text

        then:
        thrown()

        cleanup: 'make sure everything is shutdown'
        sendStopMessage 10101
    }

    @Override
    String getBuildTemplate() {
        '''
            plugins {
                id 'com.stehno.gradle.site'
                id 'java'
            }
            repositories {
                jcenter()
            }
            ${config.extension ?: ''}
        '''
    }
}
