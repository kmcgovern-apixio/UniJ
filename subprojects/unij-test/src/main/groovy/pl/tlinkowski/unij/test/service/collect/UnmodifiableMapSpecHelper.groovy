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

package pl.tlinkowski.unij.test.service.collect

import groovy.transform.PackageScope

import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Collector

/**
 * Helper class for the {@link Map} spec.
 *
 * Includes data pipes: http://spockframework.org/spock/docs/1.3/data_driven_testing.html#_data_pipes
 *
 * @author Tomasz Linkowski
 */
@PackageScope
class UnmodifiableMapSpecHelper {

  //region DATA PIPES
  static List<Map<String, Integer>> maps() {
    [
            [:],
            [a: 1],
            [a: 1, b: 2],
            [a: 1, b: 2, c: 3]
    ]
  }

  static List<Map<String, Integer>> mapsWithNull() {
    [
            // null value
            [a: null],
            [a: 1, b: null],
            [a: 1, b: 2, c: null],
            // null key
            [(null): 1],
            [a: 1, (null): 2],
            [a: 1, b: 2, (null): 3]
    ]
  }
  //endregion

  //region ENTRIES
  static List<Map.Entry<String, Integer>> entries(int size) {
    size == 0 ? [] : (1..size).collect { Map.entry(String.valueOf(it), it) }
  }

  static List<Map.Entry<String, Integer>> entriesWithNull(int size, int nullIndex, boolean nullifyKey) {
    def entries = entries(size)
    def key = nullifyKey ? null : entries[nullIndex].key
    def value = nullifyKey ? entries[nullIndex].value : null
    entries[nullIndex] = new AbstractMap.SimpleImmutableEntry(key, value)
    entries
  }

  static def k(Map.Entry<String, Integer> entry) {
    entry.key
  }

  static def v(Map.Entry<String, Integer> entry) {
    entry.value
  }

  static def combinationsWithNull(int size) {
    [0..(size - 1), [false, true]].combinations()
  }
  //endregion

  //region COLLECT
  static <K, V> Map<K, V> collect(Collector<Map.Entry<K, V>, ?, Map<K, V>> collector, Map<K, V> map) {
    map.entrySet().stream().collect(collector)
  }

  static <K, V> Map<K, V> collect(Collector<Map.Entry<K, V>, ?, Map<K, V>> collector, List<Map<K, V>> maps) {
    maps.stream().flatMap { it.entrySet().stream() }.collect(collector)
  }

  static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> collector2(
          CollectorProvider2<Map.Entry<K, V>, K, V> provider2) {
    provider2.collector({ it.getKey() }, { it.getValue() })
  }

  static <K> Collector<Map.Entry<K, Integer>, ?, Map<K, Integer>> collector3(
          CollectorProvider3<Map.Entry<K, Integer>, K, Integer> provider3) {
    provider3.collector({ it.getKey() }, { it.getValue() }, { l, r -> l + r })
  }

  @FunctionalInterface
  interface CollectorProvider2<T, K, V> {

    /**
     * @see java.util.stream.Collectors#toUnmodifiableMap(Function, Function)
     */
    Collector<T, ?, Map<K, V>> collector(Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper);
  }

  @FunctionalInterface
  interface CollectorProvider3<T, K, V> {

    /**
     * @see java.util.stream.Collectors#toUnmodifiableMap(Function, Function, BinaryOperator)
     */
    Collector<T, ?, Map<K, V>> collector(Function<? super T, ? extends K> keyMapper,
            Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction);
  }
  //endregion
}
