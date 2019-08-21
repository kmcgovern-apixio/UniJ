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

package pl.tlinkowski.unij.test.service.misc

import spock.lang.Shared
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

import pl.tlinkowski.unij.service.api.misc.MiscellaneousApiProvider

/**
 * Base specification for {@link MiscellaneousApiProvider}.
 *
 * @author Tomasz Linkowski
 */
abstract class AbstractMiscellaneousApiProviderSpec extends Specification {

  @Shared
  protected MiscellaneousApiProvider provider

  def "flatMappingCollector"(List<Integer> list, List<Integer> expected) {
    when:
      def collector = provider.flatMappingCollector({ Stream.of(it, -it) }, Collectors.toList())
    then:
      list.stream().collect(collector) == expected
    where:
      list      | expected
      []        | []
      [1]       | [1, -1]
      [-1]      | [-1, 1]
      [1, 2]    | [1, -1, 2, -2]
      [1, 2, 3] | [1, -1, 2, -2, 3, -3]
  }

  def "filteringCollector"(List<Integer> list, List<Integer> expected) {
    when:
      def collector = provider.filteringCollector({ it % 2 != 0 }, Collectors.toList())
    then:
      list.stream().collect(collector) == expected
    where:
      list      | expected
      []        | []
      [1]       | [1]
      [2]       | []
      [1, 2]    | [1]
      [1, 2, 3] | [1, 3]
  }
}