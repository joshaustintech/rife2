/*
 * Copyright 2001-2023 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.bld.operations;

import org.junit.jupiter.api.Test;
import rife.tools.FileUtils;
import rife.tools.exceptions.FileUtilsErrorException;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateRife2Operation {
    @Test
    void testInstantiation() {
        var operation = new CreateRife2Operation();
        assertNotNull(operation.workDirectory());
        assertTrue(operation.workDirectory().exists());
        assertTrue(operation.workDirectory().isDirectory());
        assertTrue(operation.workDirectory().canWrite());
        assertFalse(operation.downloadDependencies());
        assertNull(operation.packageName());
        assertNull(operation.projectName());
    }

    @Test
    void testExecute()
    throws Exception {
        var tmp = Files.createTempDirectory("test").toFile();
        try {
            var create_operation = new CreateRife2Operation()
                .workDirectory(tmp)
                .packageName("com.example")
                .projectName("myapp")
                .downloadDependencies(true);
            create_operation.execute();

            assertEquals("""
                    /myapp
                    /myapp/.gitignore
                    /myapp/.idea
                    /myapp/.idea/app.iml
                    /myapp/.idea/libraries
                    /myapp/.idea/libraries/compile.xml
                    /myapp/.idea/libraries/project.xml
                    /myapp/.idea/libraries/runtime.xml
                    /myapp/.idea/libraries/standalone.xml
                    /myapp/.idea/libraries/test.xml
                    /myapp/.idea/misc.xml
                    /myapp/.idea/modules.xml
                    /myapp/.idea/project.iml
                    /myapp/.idea/runConfigurations
                    /myapp/.idea/runConfigurations/Run Main.xml
                    /myapp/.idea/runConfigurations/Run Tests.xml
                    /myapp/bld.sh
                    /myapp/lib
                    /myapp/lib/compile
                    /myapp/lib/compile/rife2-1.5.0-20230313.213352-8.jar
                    /myapp/lib/project
                    /myapp/lib/runtime
                    /myapp/lib/standalone
                    /myapp/lib/standalone/jetty-http-11.0.14.jar
                    /myapp/lib/standalone/jetty-io-11.0.14.jar
                    /myapp/lib/standalone/jetty-jakarta-servlet-api-5.0.2.jar
                    /myapp/lib/standalone/jetty-security-11.0.14.jar
                    /myapp/lib/standalone/jetty-server-11.0.14.jar
                    /myapp/lib/standalone/jetty-servlet-11.0.14.jar
                    /myapp/lib/standalone/jetty-util-11.0.14.jar
                    /myapp/lib/standalone/slf4j-api-2.0.5.jar
                    /myapp/lib/standalone/slf4j-simple-2.0.5.jar
                    /myapp/lib/test
                    /myapp/lib/test/apiguardian-api-1.1.2.jar
                    /myapp/lib/test/jsoup-1.15.4.jar
                    /myapp/lib/test/junit-jupiter-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-api-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-engine-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-params-5.9.2.jar
                    /myapp/lib/test/junit-platform-commons-1.9.2.jar
                    /myapp/lib/test/junit-platform-console-standalone-1.9.2.jar
                    /myapp/lib/test/junit-platform-engine-1.9.2.jar
                    /myapp/lib/test/opentest4j-1.2.0.jar
                    /myapp/src
                    /myapp/src/main
                    /myapp/src/main/java
                    /myapp/src/main/java/com
                    /myapp/src/main/java/com/example
                    /myapp/src/main/java/com/example/MyappSite.java
                    /myapp/src/main/java/com/example/MyappSiteUber.java
                    /myapp/src/main/resources
                    /myapp/src/main/resources/templates
                    /myapp/src/main/resources/templates/hello.html
                    /myapp/src/main/webapp
                    /myapp/src/main/webapp/WEB-INF
                    /myapp/src/main/webapp/WEB-INF/web.xml
                    /myapp/src/main/webapp/css
                    /myapp/src/main/webapp/css/style.css
                    /myapp/src/project
                    /myapp/src/project/java
                    /myapp/src/project/java/com
                    /myapp/src/project/java/com/example
                    /myapp/src/project/java/com/example/MyappBuild.java
                    /myapp/src/test
                    /myapp/src/test/java
                    /myapp/src/test/java/com
                    /myapp/src/test/java/com/example
                    /myapp/src/test/java/com/example/MyappTest.java""",
                Files.walk(Path.of(tmp.getAbsolutePath()))
                    .map(path -> path.toAbsolutePath().toString().substring(tmp.getAbsolutePath().length()))
                    .filter(s -> !s.isEmpty())
                    .sorted()
                    .collect(Collectors.joining("\n")));

            var compile_operation = new CompileOperation().fromProject(create_operation.project());
            compile_operation.execute();
            assertTrue(compile_operation.diagnostics().isEmpty());
            assertEquals("""
                    /myapp
                    /myapp/.gitignore
                    /myapp/.idea
                    /myapp/.idea/app.iml
                    /myapp/.idea/libraries
                    /myapp/.idea/libraries/compile.xml
                    /myapp/.idea/libraries/project.xml
                    /myapp/.idea/libraries/runtime.xml
                    /myapp/.idea/libraries/standalone.xml
                    /myapp/.idea/libraries/test.xml
                    /myapp/.idea/misc.xml
                    /myapp/.idea/modules.xml
                    /myapp/.idea/project.iml
                    /myapp/.idea/runConfigurations
                    /myapp/.idea/runConfigurations/Run Main.xml
                    /myapp/.idea/runConfigurations/Run Tests.xml
                    /myapp/bld.sh
                    /myapp/build
                    /myapp/build/main
                    /myapp/build/main/com
                    /myapp/build/main/com/example
                    /myapp/build/main/com/example/MyappSite.class
                    /myapp/build/main/com/example/MyappSiteUber.class
                    /myapp/build/test
                    /myapp/build/test/com
                    /myapp/build/test/com/example
                    /myapp/build/test/com/example/MyappTest.class
                    /myapp/lib
                    /myapp/lib/compile
                    /myapp/lib/compile/rife2-1.5.0-20230313.213352-8.jar
                    /myapp/lib/project
                    /myapp/lib/runtime
                    /myapp/lib/standalone
                    /myapp/lib/standalone/jetty-http-11.0.14.jar
                    /myapp/lib/standalone/jetty-io-11.0.14.jar
                    /myapp/lib/standalone/jetty-jakarta-servlet-api-5.0.2.jar
                    /myapp/lib/standalone/jetty-security-11.0.14.jar
                    /myapp/lib/standalone/jetty-server-11.0.14.jar
                    /myapp/lib/standalone/jetty-servlet-11.0.14.jar
                    /myapp/lib/standalone/jetty-util-11.0.14.jar
                    /myapp/lib/standalone/slf4j-api-2.0.5.jar
                    /myapp/lib/standalone/slf4j-simple-2.0.5.jar
                    /myapp/lib/test
                    /myapp/lib/test/apiguardian-api-1.1.2.jar
                    /myapp/lib/test/jsoup-1.15.4.jar
                    /myapp/lib/test/junit-jupiter-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-api-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-engine-5.9.2.jar
                    /myapp/lib/test/junit-jupiter-params-5.9.2.jar
                    /myapp/lib/test/junit-platform-commons-1.9.2.jar
                    /myapp/lib/test/junit-platform-console-standalone-1.9.2.jar
                    /myapp/lib/test/junit-platform-engine-1.9.2.jar
                    /myapp/lib/test/opentest4j-1.2.0.jar
                    /myapp/src
                    /myapp/src/main
                    /myapp/src/main/java
                    /myapp/src/main/java/com
                    /myapp/src/main/java/com/example
                    /myapp/src/main/java/com/example/MyappSite.java
                    /myapp/src/main/java/com/example/MyappSiteUber.java
                    /myapp/src/main/resources
                    /myapp/src/main/resources/templates
                    /myapp/src/main/resources/templates/hello.html
                    /myapp/src/main/webapp
                    /myapp/src/main/webapp/WEB-INF
                    /myapp/src/main/webapp/WEB-INF/web.xml
                    /myapp/src/main/webapp/css
                    /myapp/src/main/webapp/css/style.css
                    /myapp/src/project
                    /myapp/src/project/java
                    /myapp/src/project/java/com
                    /myapp/src/project/java/com/example
                    /myapp/src/project/java/com/example/MyappBuild.java
                    /myapp/src/test
                    /myapp/src/test/java
                    /myapp/src/test/java/com
                    /myapp/src/test/java/com/example
                    /myapp/src/test/java/com/example/MyappTest.java""",
                Files.walk(Path.of(tmp.getAbsolutePath()))
                    .map(path -> path.toAbsolutePath().toString().substring(tmp.getAbsolutePath().length()))
                    .filter(s -> !s.isEmpty())
                    .sorted()
                    .collect(Collectors.joining("\n")));

            final var run_operation = new RunOperation().fromProject(create_operation.project());
            final var executor = Executors.newSingleThreadScheduledExecutor();
            final var checked_url = new URL("http://localhost:8080");
            final String[] check_result = new String[1];
            executor.schedule(() -> {
                try {
                    check_result[0] = FileUtils.readString(checked_url);
                } catch (FileUtilsErrorException e) {
                    throw new RuntimeException(e);
                }
            }, 500, TimeUnit.MILLISECONDS);
            executor.schedule(() -> run_operation.process().destroy(), 1, TimeUnit.SECONDS);
            run_operation.execute();

            assertTrue(check_result[0].contains("<p>Hello World Myapp</p>"));
        } finally {
            FileUtils.deleteDirectory(tmp);
        }
    }

    @Test
    void testExecuteNoDownload()
    throws Exception {
        var tmp = Files.createTempDirectory("test").toFile();
        try {
            var create_operation = new CreateRife2Operation()
                .workDirectory(tmp)
                .packageName("org.stuff")
                .projectName("yourthing");
            create_operation.execute();

            assertEquals("""
                    /yourthing
                    /yourthing/.gitignore
                    /yourthing/.idea
                    /yourthing/.idea/app.iml
                    /yourthing/.idea/libraries
                    /yourthing/.idea/libraries/compile.xml
                    /yourthing/.idea/libraries/project.xml
                    /yourthing/.idea/libraries/runtime.xml
                    /yourthing/.idea/libraries/standalone.xml
                    /yourthing/.idea/libraries/test.xml
                    /yourthing/.idea/misc.xml
                    /yourthing/.idea/modules.xml
                    /yourthing/.idea/project.iml
                    /yourthing/.idea/runConfigurations
                    /yourthing/.idea/runConfigurations/Run Main.xml
                    /yourthing/.idea/runConfigurations/Run Tests.xml
                    /yourthing/bld.sh
                    /yourthing/lib
                    /yourthing/lib/compile
                    /yourthing/lib/project
                    /yourthing/lib/runtime
                    /yourthing/lib/standalone
                    /yourthing/lib/test
                    /yourthing/src
                    /yourthing/src/main
                    /yourthing/src/main/java
                    /yourthing/src/main/java/org
                    /yourthing/src/main/java/org/stuff
                    /yourthing/src/main/java/org/stuff/YourthingSite.java
                    /yourthing/src/main/java/org/stuff/YourthingSiteUber.java
                    /yourthing/src/main/resources
                    /yourthing/src/main/resources/templates
                    /yourthing/src/main/resources/templates/hello.html
                    /yourthing/src/main/webapp
                    /yourthing/src/main/webapp/WEB-INF
                    /yourthing/src/main/webapp/WEB-INF/web.xml
                    /yourthing/src/main/webapp/css
                    /yourthing/src/main/webapp/css/style.css
                    /yourthing/src/project
                    /yourthing/src/project/java
                    /yourthing/src/project/java/org
                    /yourthing/src/project/java/org/stuff
                    /yourthing/src/project/java/org/stuff/YourthingBuild.java
                    /yourthing/src/test
                    /yourthing/src/test/java
                    /yourthing/src/test/java/org
                    /yourthing/src/test/java/org/stuff
                    /yourthing/src/test/java/org/stuff/YourthingTest.java""",
                Files.walk(Path.of(tmp.getAbsolutePath()))
                    .map(path -> path.toAbsolutePath().toString().substring(tmp.getAbsolutePath().length()))
                    .filter(s -> !s.isEmpty())
                    .sorted()
                    .collect(Collectors.joining("\n")));

            var compile_operation = new CompileOperation() {
                public void executeOutputDiagnostics(DiagnosticCollector<JavaFileObject> diagnostics) {
                    // don't output errors
                }
            };
            compile_operation.fromProject(create_operation.project());
            compile_operation.execute();
            var diagnostics = compile_operation.diagnostics();
            assertEquals(9, diagnostics.size());
            for (var diagnostic : diagnostics) {
                assertEquals("/yourthing/src/test/java/org/stuff/YourthingTest.java", diagnostic.getSource().toUri().getPath().substring(tmp.getAbsolutePath().length()));
            }
        } finally {
            FileUtils.deleteDirectory(tmp);
        }
    }
}