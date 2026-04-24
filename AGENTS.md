# AGENTS.md - StudyRoom Project Guidelines

This document provides guidelines for agentic coding agents working in this repository.

## Project Overview

- **Type**: Java 17 Spring Boot 3.2.5 Multi-Module Maven Project
- **Modules**: study-common, study-pojo, study-server
- **Package**: com.study
- **Database**: MySQL with Druid connection pool
- **ORM**: MyBatis 3.0.3

## Build Commands

```bash
cd /home/hhwf/code/java/studyRoom

# Build all modules
mvn clean install

# Build single module (with dependencies)
mvn clean install -pl study-server -am

# Run application
mvn spring-boot:run -pl study-server

# Run specific test class
mvn test -pl study-server -Dtest=UserServiceTest

# Run specific test method
mvn test -pl study-server -Dtest=UserServiceTest#testUserLogin

# Run all tests in module
mvn test -pl study-server

# Skip tests during build
mvn clean install -DskipTests
```

## Project Structure

```
studyRoom/
├── study-common/     # Shared: Result, constants, utils, exception
├── study-pojo/      # DTOs, Entities, VOs
└── study-server/    # Controllers, Services, Mappers
```

## Code Style

### General Formatting
- Indent: 4 spaces (no tabs)
- Line length: 120 chars max
- Open brace on same line: `if (condition) {`
- One blank line between methods
- No trailing whitespace

### Naming Conventions
- **Classes**: PascalCase (e.g., `UserController`, `LoginServiceImpl`)
- **Methods**: camelCase (e.g., `userLogin`, `addRoom`)
- **Variables**: camelCase (e.g., `userName`, `pageResult`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `STATUS_NORMAL`)
- **Packages**: lowercase (e.g., `com.study.controller.admin`)
- **Interfaces**: Add `Impl` suffix for implementation (e.g., `LoginService` + `LoginServiceImpl`)

### Import Organization
Order imports (IDE should auto-organize):
1. Java/JDK (java.*, javax.*)
2. Spring (org.springframework.*)
3. Third-party (lombok, mybatis, swagger, etc.)
4. Project (com.study.*)

### Entity Classes
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private LocalDateTime createTime;
}
```
- Use Lombok: `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`
- Implement `Serializable` with `serialVersionUID`

### Controller Layer
```java
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员接口")
@Slf4j
public class AdminController extends BaseController {
    @Autowired private AdminService adminService;

    @GetMapping("/user/list")
    @Operation(summary = "查询用户列表")
    public Result<PageResult> getUserList(
            @RequestHeader(value = "token", required = false) String token,
            UserQueryDTO queryDTO) {
        requireRole(token, StatuConstant.ROLE_ADMIN);
        log.info("查询用户列表, 条件: {}", queryDTO);
        return Result.success(adminService.getUserList(queryDTO));
    }
}
```
- Use `@RestController`, `@RequestMapping`
- Use `@Tag` + `@Operation` for Knife4j API docs
- Use `@Slf4j` for logging
- Always return `Result<T>`
- Extend `BaseController` for auth utilities
- Inject services via `@Autowired`
- Use `@RequestHeader` for token extraction

### Service Layer
```java
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired private LoginMapper loginMapper;

    @Override
    public String userLogin(UserLoginDTO loginDTO) {
        User user = loginMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return JwtUtil.createToken(user.getUsername(), user.getUsername());
    }
}
```
- Use interface-based design with `@Service` implementation
- Use `@Autowired` for DI
- Business logic + logging

### Mapper Layer
- Use `@Mapper` annotation or XML mappers
- XML mappers: `src/main/resources/mapper/`
- Follow MyBatis conventions

### DTO/VO Classes
- DTOs: request objects in `study-pojo/src/main/java/com/study/dto/`
- VOs: response objects in `study-pojo/src/main/java/com/study/vo/`
- Use Lombok annotations

### Types Usage
- Use primitive types when possible (`int` vs `Integer`)
- Use `String` for text, avoid `char`
- Use `LocalDateTime` for timestamps
- Use `BigDecimal` for money/precision
- Use `List` over `ArrayList` in interfaces
- Avoid raw types: use `List<String>` not `List`

### Error Handling
- Throw `BusinessException` (extends RuntimeException) for business errors
- Use `Result.success(data)` or `Result.error(msg)` for responses
- Log before operations: `log.info("...")` or `log.error("...", e)`
- Global exception handler in `BaseController`

### Validation
- Use `@Valid` on request DTOs in controller parameters
- Use `@NotNull`, `@NotBlank`, `@Size` etc. from jakarta.validation
- Add validation messages in DTO fields

### Transactions
- Use `@Transactional` on service methods for DB operations
- Default: rollback on RuntimeException
- Use `@Transactional(readOnly = true)` for read-only queries

### Logging
- Use Lombok's `@Slf4j`
- Log entry: `log.info("methodName: action")`
- Log params: `log.info("action, param: {}", param)`
- Log errors: `log.error("action failed", e)`

## API Documentation

- Uses Knife4j (OpenAPI 3)
- Access at `/doc.html` when running
- Annotate with `@Tag` and `@Operation`

## Configuration

- `src/main/resources/application.yml`
- Environment: `application-dev.yml`, `application-prod.yml`
- MyBatis mappers: `src/main/resources/mapper/`

## Key Dependencies

| Library | Version |
|---------|---------|
| Spring Boot | 3.2.5 |
| MyBatis | 3.0.3 |
| Lombok | 1.18.38 |
| JWT (jjwt) | 0.12.5 |
| Druid | 1.2.23 |
| PageHelper | 2.1.0 |
| Knife4j | 4.4.0 |

## Testing

No tests currently exist. When adding tests:
- Place in `src/test/java/` matching package structure
- Use JUnit 5 with Spring Boot Test
- Naming: `*Test.java` or `*Tests.java`
- Follow naming: `testMethodName_Scenario_ExpectedBehavior`