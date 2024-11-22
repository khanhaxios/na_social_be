package com.NA.social.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        servers = @Server(
                description = "LOCAL ENV",
                url = "http://127.0.0.1:8088/api"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth token required",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class SwggerConfig {
}
