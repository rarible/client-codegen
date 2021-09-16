package com.rarible.openapi.client;

import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.JavaClientCodegen;
import org.openapitools.codegen.languages.KotlinSpringServerCodegen;
import org.openapitools.codegen.templating.mustache.CamelCaseLambda;

import java.util.stream.Collectors;

public class RaribleClientGenerator extends JavaClientCodegen implements CodegenConfig {

  /**
   * Configures a friendly name for the generator.  This will be used by the generator
   * to select the library with the -g flag.
   * 
   * @return the friendly name for the generator
   */
  public String getName() {
    return "rarible-client";
  }

  public RaribleClientGenerator() {
    super();
    embeddedTemplateDir = templateDir = "rarible-client";
    setModelPackage("com.rarible.protocol.dto");
    setLibrary("webclient");
    setModelNameSuffix("Dto");
    setUseBeanValidation(false);
    setOpenApiNullable(false);
    additionalProperties().put("firstLetterUppercase", new CamelCaseLambda(false));
  }

  @Override
  public void processOpts() {
    super.processOpts();
    supportingFiles = supportingFiles.stream().filter(it -> !it.getTemplateFile().toLowerCase().contains("gradle")).collect(Collectors.toList());
    importMapping.put("Address", "scalether.domain.Address");
    importMapping.put("Word", "io.daonomic.rpc.domain.Word");
    importMapping.put("Binary", "io.daonomic.rpc.domain.Binary");
    importMapping.put("BigInteger", "java.math.BigInteger");
    importMapping.put("BigDecimal", "java.math.BigDecimal");
  }
}