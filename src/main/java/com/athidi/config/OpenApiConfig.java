package com.athidi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public OpenAPI athidiOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Athidi Backend API")
                                .description("REST APIs for Athidi Property Discovery Platform")
                                .version("v1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Athidi Backend Team")
                                                .email("support@athidi.com")
                                                .url("https://github.com/saipraveennaidu")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Athidi Project Documentation")
                                .url("https://github.com/saipraveennaidu")
                );
    }
}
