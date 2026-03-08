# Update to Spring Boot 4 and Spring AI 2

Migrate the codebase from Spring Boot 3.5 to Spring Boot 4.0.3 and Spring AI 1.x to 2.0.0-M2. This involves changing Java versions, updating dependencies across all `pom.xml` files, and migrating from Jackson 2 to Jackson 3.

## User Review Required
> [!IMPORTANT]
> - `ObjectMapper` is being replaced by `JsonMapper` due to the Jackson 3 update in Spring Boot 4. All `com.fasterxml.jackson.*` imports (except `jackson-annotations`) are migrating to `tools.jackson.*`.
> - Java version is being bumped to Java 25 as requested. Please verify your local environment and CI/CD pipelines support Java 25.
> - Please review the proposed changes below.

## Proposed Changes

### POM Updates
All `pom.xml` files will be reviewed. The root `pom.xml` will be updated, and any submodules defining explicit versions or outdated Jackson dependencies will be adjusted.

#### [MODIFY] [pom.xml](file:///Users/odedia/tanzu/spring/spring-ai/spring-ai-zero-to-hero/pom.xml)
- Change `spring-boot-starter-parent` version to `4.0.3`
- Change `java.version` to `25`
- Change `spring-ai.version` to `2.0.0-M2`

*(Other internal `pom.xml`s will be scanned and updated if they duplicate these version properties or explicitly define Java versions.)*

### Jackson 3 Migration
Spring Boot 4 uses Jackson 3. We will replace `ObjectMapper` with `JsonMapper` and update the package imports.

Affected Files:
- `applications/gateway/src/main/java/com/example/log/OpenAiAuditor.java`
- `mcp/05-mcp-capabilities/src/main/java/mcp/capabilities/WeatherService.java`
- `agentic-system/01-inner-monologue/inner-monologue-cli/src/main/java/com/example/JsonUtils.java`
- `components/data/src/main/java/com/example/data/DataFiles.java`
- Model classes in `components/data/src/main/java/com/example/model/` (Annotations such as `@JsonProperty`, `@JsonIgnoreProperties` remain in `com.fasterxml.jackson.annotation` as they are compatible, but will verify.)
- `agentic-system/02-model-directed-loop/model-directed-loop-cli/src/main/java/com/example/JsonUtils.java`
- `mcp/04-dynamic-tool-calling/server/src/main/java/org/springframework/ai/mcp/sample/server/WeatherService.java`
- `components/patterns/02-retrieval-augmented-generation/src/main/java/com/example/JsonReader2.java`
- `components/patterns/02-retrieval-augmented-generation/src/main/java/com/example/rag/bikes/BikesController.java`

**Changes in affected files:**
- Switch `import com.fasterxml.jackson.databind.ObjectMapper;` to `import tools.jackson.databind.json.JsonMapper;`
- Switch `new ObjectMapper()` to `JsonMapper.builder().build()` (or similar Jackson 3 standard).
- Switch `import com.fasterxml.jackson.core.*` to `import tools.jackson.core.*`
- Update `ObjectWriter` appropriately.

### Spring AI 2 Migration
Verify if any `ChatClient` breaking changes (like `FunctionCallback` -> `ToolCallback` or default model settings) affect the codebase. If so, update the usage to the new API.

## Verification Plan
### Automated Tests
Run Maven build to ensure all modules compile successfully with the new dependencies:
```bash
./mvnw clean install -DskipTests
```
And then run tests:
```bash
./mvnw test
```

### Manual Verification
Review application startup for critical services (e.g., `applications/gateway`) to ensure no class-validation or injection errors surface at runtime.
