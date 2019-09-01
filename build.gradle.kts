/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2018-2019 Tomasz Linkowski.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import me.champeau.gradle.JMHPlugin
import me.champeau.gradle.JMHPluginExtension

plugins {
  // https://github.com/tlinkowski/tlinkowski-superpom
  id("pl.tlinkowski.gradle.my.superpom")

  // https://github.com/melix/jmh-gradle-plugin
  id("me.champeau.gradle.jmh") version "0.4.8" apply false
}

config {
  info {
    name = "UniJ"
    description = "Universal cross-JDK API provider for immutable collections"
    tags = listOf("java", "facade", "collection", "collector", "immutability")
    inceptionYear = "2018"

    links {
      website = "https://github.com/tlinkowski/UniJ"
      issueTracker = "$website/issues"
      scm = "$website.git"
    }
  }
}

subprojects {
  apply {
    // https://docs.gradle.org/current/userguide/java_library_plugin.html
    plugin(JavaLibraryPlugin::class)

    // https://github.com/melix/jmh-gradle-plugin
    plugin(JMHPlugin::class)
  }

  //region CONFIGURATION PROPERTIES
  val api by configurations
  val implementation by configurations
  val compileOnly by configurations
  val annotationProcessor by configurations

  val testImplementation by configurations
  //endregion

  dependencies {
    val basicAnnotationsVersion: String by project // https://github.com/tlinkowski/basic-annotations
    val autoServiceVersion: String by project // https://github.com/google/auto/tree/master/service

    compileOnly(group = "pl.tlinkowski.annotation", name = "pl.tlinkowski.annotation.basic", version = basicAnnotationsVersion)
    compileOnly(group = "com.google.auto.service", name = "auto-service-annotations", version = autoServiceVersion)

    annotationProcessor(group = "com.google.auto.service", name = "auto-service", version = autoServiceVersion)
  }

  config {
    bintray.enabled = true

    javadoc.autoLinks {
      excludes.add("lombok-.*") // https://github.com/tlinkowski/UniJ/issues/38
      excludes.add("pl\\.tlinkowski\\.unij\\..*") // https://github.com/aalmiray/kordamp-gradle-plugins/issues/169
    }
  }

  //region PER-SUBPROJECT-TYPE CONFIGURATION
  if (name.contains(".bundle.")) {
    dependencies {
      api(project(":pl.tlinkowski.unij.api"))
      testImplementation(project(":pl.tlinkowski.unij.test"))
    }
  }
  if (name.contains(".service.collect.") || name.contains(".service.misc.")) {
    dependencies {
      implementation(project(":pl.tlinkowski.unij.service.api"))
      testImplementation(project(":pl.tlinkowski.unij.test"))
    }
  }
  //endregion

  /**
   * BENCHMARKS
   * http://openjdk.java.net/projects/code-tools/jmh/
   */
  configure<JMHPluginExtension> {
    /*
     * Note that when using "separateClasspathJAR" option and running the benchmark on Windows,
     * the following four resources must be on the same drive (otherwise, "'other' has different root" is thrown):
     * 1) this project
     * 2) JDK
     * 3) Gradle user home dir (can be set using GRADLE_USER_HOME environment variable)
     * 4) TEMP dir (can be set using System properties through "java.io.tmpdir")
     */
    jvmArgsAppend = listOf("-Djmh.separateClasspathJAR=true") // https://bugs.openjdk.java.net/browse/CODETOOLS-7902106
  }
}

//region TEMPORARY workaround for: https://github.com/tlinkowski/UniJ/issues/40 (TODO: remove this)
val namesOfYetEmptyProjects = listOf(
        "pl.tlinkowski.unij.bundle.eclipse_jdk8",
        "pl.tlinkowski.unij.bundle.guava_jdk8",
        "pl.tlinkowski.unij.bundle.jdk8",
        "pl.tlinkowski.unij.bundle.jdk11"
)
namesOfYetEmptyProjects.forEach {
  project(":$it") {
    tasks {
      "javadoc" {
        enabled = false
      }
    }
  }
}
//endregion
