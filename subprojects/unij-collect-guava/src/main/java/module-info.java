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

import pl.tlinkowski.unij.service.api.collect.UnmodifiableListFactory;
import pl.tlinkowski.unij.service.api.collect.UnmodifiableSetFactory;
import pl.tlinkowski.unij.service.collect.guava.GuavaUnmodifiableListFactory;
import pl.tlinkowski.unij.service.collect.guava.GuavaUnmodifiableSetFactory;

/**
 * Unmodifiable-{@link java.util.Collection}-related <a href="https://github.com/google/guava">Guava</a>-based bindings
 * for UniJ.
 *
 * @author Tomasz Linkowski
 */
module pl.tlinkowski.unij.service.collect.guava {
  requires com.google.common;

  requires pl.tlinkowski.unij.service.api;
  requires static pl.tlinkowski.annotation.basic;
  requires static auto.service.annotations;

  provides UnmodifiableListFactory with GuavaUnmodifiableListFactory;
  provides UnmodifiableSetFactory with GuavaUnmodifiableSetFactory;
}
