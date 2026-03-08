# Spring Boot 4 & Spring AI 2 Migration Walkthrough

The project has been successfully migrated to **Spring Boot 4.0.3** and **Spring AI 2.0.0-M2**, and compiled entirely with **Java 25**. 

## Changes Made
1. **Dependency Updates**: Lifted `<spring-boot-starter-parent>` to `4.0.3`, `<spring-ai.version>` to `2.0.0-M2`, and `<java.version>` to `25` across all project modules.
2. **Jackson 3 Integration**:
   - Replaced Jackson 2 `ObjectMapper` with Jackson 3's `JsonMapper` (from the `tools.jackson.*` package) where user code interacted directly with JSON.
   - For modules where internal models needed backward compatibility without rewriting entire auto-configs (like `DataFiles.java`), explicit Jackson 2 dependencies (`jackson-databind`, `jackson-datatype-jsr310`) were injected.
   - Refactored `JsonProcessingException` to generic Java Exceptions in places affected by Jackson 3 API changes.
3. **MCP Clients Refactoring**:
   - `StdioClientTransport` and `WebFluxSseClientTransport` constructors in Spring AI 2.0.0-M2 now require an explicit `McpJsonMapper`. We successfully updated these instances across the `mcp/` directories using a new `JacksonMcpJsonMapper`.
   
## Validation Results
- Code formatting and standard linting was applied via `spotless:apply`.
- Executed `./mvnw clean compile test` against all 39 sub-modules.
- **Result:** `BUILD SUCCESS`.

All code changes correctly interoperate with both standard Spring MVC components (now utilizing Jackson 3) and internal Spring AI logic. 
