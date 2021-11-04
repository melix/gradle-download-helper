package gradle.helper.plugin;/*
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

import gradle.helper.model.DefaultDependenciesModel;
import gradle.helper.model.DependenciesModel;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.result.ResolvedComponentResult;
import org.gradle.api.artifacts.result.ResolvedDependencyResult;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependenciesModelPlugin implements Plugin<Project> {
    public static final String DEPENDENCY_PREFIX = "   Dependency: ";
    private final ToolingModelBuilderRegistry registry;

    @Inject
    public DependenciesModelPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void apply(Project target) {
        registry.register(new DependenciesModelBuilder());
    }

    private static class DependenciesModelBuilder implements ToolingModelBuilder {

        @Override
        public boolean canBuild(String modelName) {
            return DependenciesModel.class.getName().equals(modelName);
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            Map<String, Map<String, String>> model = new HashMap();
            project.getAllprojects().forEach(p -> {
                System.out.println("Analyzing " + p);
                Map<String, String> dependencies = new HashMap<>();
                Arrays.asList("compileClasspath", "runtimeClasspath").forEach(conf -> {
                    Configuration configuration = p.getConfigurations().findByName(conf);
                    if (configuration != null) {
                        ResolvedComponentResult root = configuration.getIncoming().getResolutionResult().getRoot();
                        dependencies.put(conf, convert(root));
                    } else {
                        System.out.println("  /!\\ Didn't find " + conf);
                    }
                });
                model.put(p.getPath(), dependencies);
            });
            return new DefaultDependenciesModel(model);
        }
    }

    static String convert(ResolvedComponentResult result) {
        StringBuilder sb = new StringBuilder();
        Set<ResolvedComponentResult> visited = new HashSet<>();
        ArrayDeque<ResolvedComponentResult> queue = new ArrayDeque<>();
        queue.add(result);
        while (!queue.isEmpty()) {
            ResolvedComponentResult e = queue.pop();
            if (visited.add(e)) {
                sb.append(DEPENDENCY_PREFIX).append(e.getModuleVersion()).append("\n");
                e.getDependencies().forEach(d -> {
                    if (d instanceof ResolvedDependencyResult) {
                        queue.add(((ResolvedDependencyResult) d).getSelected());
                    } else {
                        System.out.println("  /!\\ Unresolved dependency " + d);
                    }
                });
            }
        }
        return sb.toString();
    }
}
