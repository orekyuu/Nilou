package net.orekyuu.nilou.testing;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import net.orekyuu.nilou.UriBuilderAnnotationProcessor;
import org.junit.jupiter.api.Assertions;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.google.testing.compile.Compiler.javac;
import static java.nio.file.Paths.get;

public abstract class AnnotationProcessorTesting {

  public void compile(String testCaseDir) {
    try {
      URL resource = getClass().getClassLoader().getResource(testCaseDir);
      Path testCaseDirPath = get(resource.toURI());

      Path expectedDir = testCaseDirPath.resolve("expected");
      Path actualDir = testCaseDirPath.resolve("actual");

      List<JavaFileObject> files = files(actualDir);

      Compilation compilation = javac()
              .withProcessors(new UriBuilderAnnotationProcessor())
              .compile(files);

      CompilationSubject subject = CompilationSubject.assertThat(compilation);

      List<JavaFileObject> expectedFiles = files(expectedDir);
      for (JavaFileObject expectedFile : expectedFiles) {
        var relativePath = expectedDir.relativize(new File(expectedFile.getName()).toPath()).toString();
        String className = relativePath.substring(0, relativePath.length() - ".java".length()).replace(File.separatorChar, '.');
        subject.generatedSourceFile(className).hasSourceEquivalentTo(expectedFile);
      }
    } catch (URISyntaxException | IOException e) {
      Assertions.fail(e);
    }
  }

  private List<JavaFileObject> files(Path dir) throws IOException {
    List<Path> paths = Files.walk(dir).toList();
    List<JavaFileObject> javaFiles = new ArrayList<>();
    for (Path path : paths) {
      URL url = path.toUri().toURL();
      if (Files.isRegularFile(path)) {
        javaFiles.add(JavaFileObjects.forResource(url));
      }
    }
    return javaFiles;
  }
}
