package com.rarible.openapi.client;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.openapitools.codegen.languages.TypeScriptFetchClientCodegen;

import java.util.HashMap;
import java.util.Map;

public class RaribleTsClientGenerator extends TypeScriptFetchClientCodegen {

    private static final String X_CONTROLLER = "x-controller";

    /**
     * Configures a friendly name for the generator.  This will be used by the generator
     * to select the library with the -g flag.
     *
     * @return the friendly name for the generator
     */
    @Override
    public String getName() {
        return "rarible-typescript-fetch";
    }

    private final Map<String, String> tagMapping = new HashMap<>();

    @Override
    public void preprocessOpenAPI(OpenAPI openAPI) {
        for (PathItem path : openAPI.getPaths().values()) {
            for (Operation operation : path.readOperations()) {
                Map<String, Object> extensions = operation.getExtensions();
                if (extensions == null || !extensions.containsKey(X_CONTROLLER)) {
                    continue;
                }
                String controller = extensions.get(X_CONTROLLER).toString();
                for (String tag : operation.getTags()) {
                    String exists = tagMapping.get(tag);
                    if (exists != null && !controller.equals(exists)) {
                        throw new IllegalArgumentException("'x-controller' value for operation "
                                + operation.getOperationId() + " with tag " + tag + " set as '" + operation + "' "
                                + "but this tag already mapped to controller with name '" + exists + "'");
                    }
                    tagMapping.put(tag, controller);
                }
            }
        }
    }

    @Override
    public String sanitizeTag(String tag) {
        String controller = tagMapping.get(tag);
        if (controller != null) {
            return controller;
        }
        return super.sanitizeTag(tag);
    }
}
