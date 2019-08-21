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

/**
 * A bundle of bindings for UniJ:
 * <ul>
 *   <li>unmodifiable-{@link java.util.Collection}-related JDK-10-based bindings</li>
 *   <li>miscellaneous JDK-11-based bindings</li>
 * </ul>
 *
 * @author Tomasz Linkowski
 */
@SuppressWarnings("JavaModuleNaming")
module pl.tlinkowski.unij.bundle.jdk11 {
  requires transitive pl.tlinkowski.unij.api;

  requires pl.tlinkowski.unij.service.collect.jdk10;
  requires pl.tlinkowski.unij.service.misc.jdk11;
}
