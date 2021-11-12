/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.helper;

import gradle.helper.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Seeder {
    public static void main(String[] args) {
        List<String> allArgs = new ArrayList<>();
        allArgs.addAll(Arrays.asList(
                "-b", "src/mn-project",
                "-o", "build/dependencies.txt",
                "-w", "build/repo"
        ));
        allArgs.addAll(Arrays.asList(args));
        App.main(allArgs.toArray(new  String[0]));
    }
}