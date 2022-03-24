package com.devonfw.cobigen.templates.devon4j.test.templates;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.devonfw.cobigen.api.constants.ConfigurationConstants;
import com.devonfw.cobigen.maven.test.AbstractMavenTest;
import com.devonfw.cobigen.templates.devon4j.config.constant.MavenMetadata;

/**
 * Smoke tests of all templates.
 */
public class TemplatesGenerationTest extends AbstractMavenTest {

  /** Root of all test resources of this test suite */
  public static final String TEST_RESOURCES_ROOT = "src/test/resources/testdata/templatetest/";

  /** Temporary files rule to create temporary folders */
  @ClassRule
  public static TemporaryFolder tempFolder = new TemporaryFolder();

  /** The templates development folder */
  protected static Path templatesProject;

  /** The templates development folder */
  protected static Path templatesProjectTemporary;

  /**
   * @throws URISyntaxException if the path could not be created properly
   * @throws IOException if accessing a template directory directory fails
   */
  @BeforeClass
  public static void setupDevTemplates() throws URISyntaxException, IOException {

    templatesProject = new File(
        TemplatesGenerationTest.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile()
            .getParentFile().getParentFile().toPath();

    // create a temporary directory cobigen-templates/template-sets/adapted containing the template sets
    Path tempFolderPath = tempFolder.getRoot().toPath();
    Path cobigenTemplatePath = tempFolderPath.resolve("cobigen-templates");
    if (!Files.exists(cobigenTemplatePath)) {
      Files.createDirectory(cobigenTemplatePath);

      templatesProjectTemporary = cobigenTemplatePath.resolve(ConfigurationConstants.TEMPLATE_SETS_FOLDER);
      Path templateSetsDownloadedFolder = templatesProjectTemporary.resolve(ConfigurationConstants.DOWNLOADED_FOLDER);
      Files.createDirectory(templatesProjectTemporary);
      Files.createDirectory(templateSetsDownloadedFolder);

      try (Stream<Path> files = Files.list(templatesProject)) {
        files.forEach(path -> {
          if (Files.isDirectory(path) && Files.exists(path.resolve("target"))) {
            try (Stream<Path> targetFiles = Files.list(path.resolve("target"))) {
              targetFiles.forEach(targetFile -> {
                if (!Files.isDirectory(targetFile) && targetFile.toString().endsWith("jar")) {
                  try {
                    Files.copy(targetFile, templateSetsDownloadedFolder.resolve(targetFile.getFileName()));
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                }
              });
            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          }
        });
      }
    }
  }

  /**
   * Test successful generation of all templates based on an entity
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_EntityInput() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesEntityInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

  /**
   * Test successful generation of all templates based on an ETO
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_EtoInput() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesEtoInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

  /**
   * Test successful generation of all templates based on an ETO
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_OpenApiInput() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesOpenApiInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

  /**
   * Test successful generation of all templates based on a RestService
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_RestServiceInput() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesRestServiceInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

  /**
   * Test successful generation of all templates based on a TO
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_ToInput() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesToInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

  /**
   * Test successful generation of all templates based on a
   *
   * @throws Exception test fails
   */
  @Test
  public void testAllTemplatesGeneration_XML() throws Exception {

    File testProject = new File(TEST_RESOURCES_ROOT + "TestAllTemplatesXMLInput/");
    runMavenInvoker(testProject, templatesProjectTemporary.toFile(), MavenMetadata.LOCAL_REPO);
  }

}
