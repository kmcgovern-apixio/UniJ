/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2019 Tomasz Linkowski.
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
package pl.tlinkowski.unij.core;

import java.util.*;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import pl.tlinkowski.unij.exception.UniJException;

/**
 * Loads UniJ services (used internally by {@link UniJ}).
 *
 * @author Tomasz Linkowski
 */
@UtilityClass
@Slf4j
final class UniJLoader {

  static <T> T load(Class<T> serviceClass) {
    List<T> services = new ArrayList<>(4);
    ServiceLoader.load(serviceClass).forEach(services::add);
    validateLoadedServices(services, serviceClass);
    return selectService(services, serviceClass);
  }

  private static <T> void validateLoadedServices(Collection<T> services, Class<T> serviceClass) {
    if (services.isEmpty()) {
      throw new UniJException(String.format(
              "No %s service found. Ensure proper unij-* module is on the classpath/modulepath", serviceClass.getName()
      ));
    }
    log.debug("{} service: found {}", serviceClass.getName(), services);
  }

  private static <T> T selectService(List<T> services, Class<T> serviceClass) {
    T loadedService = services.get(0); // TODO: https://github.com/tlinkowski/UniJ/issues/21
    log.info("{} service: selected {}", serviceClass.getName(), loadedService.getClass().getName());
    return loadedService;
  }
}