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

import groovy.text.GStringTemplateEngine
import groovy.text.TemplateEngine
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.rules.TemporaryFolder

/**
 * Created by cjstehno on 9/23/16.
 */
trait UsesGradleBuild {

    String getBuildTemplate() { '' }

    abstract TemporaryFolder getProjectRoot()

    private final TemplateEngine templateEngine = new GStringTemplateEngine()

    void buildFile(final Map<String, Object> config = [:]) {
        File buildFile = projectRoot.newFile('build.gradle')
        buildFile.text = (templateEngine.createTemplate(buildTemplate).make(config: config) as String).stripIndent()
    }

    GradleRunner gradleRunner(final List<String> args) {
        GradleRunner.create().withPluginClasspath().withDebug(true).withProjectDir(projectRoot.root).withArguments(args)
    }

    static boolean totalSuccess(final BuildResult result) {
        result.tasks.every { BuildTask task ->
            task.outcome == TaskOutcome.SUCCESS || task.outcome == TaskOutcome.UP_TO_DATE
        }
    }

    static boolean textContainsLines(final String text, final Collection<String> lines, final boolean trimmed = true) {
        lines.every { String line ->
            text.contains(trimmed ? line.trim() : line)
        }
    }

    static boolean textDoesNotContainLines(final String text, final Collection<String> lines, final boolean trimmed = true) {
        lines.every { String line ->
            !text.contains(trimmed ? line.trim() : line)
        }
    }
}
