# AGENTS.md - StudyRoom Project Guidelines

This document provides guidelines for agentic coding agents working in this repository.

## Project Overview

- **Type**: Java Spring Boot 3.2.5 Multi-Module Maven Project
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

# Run single test
mvn test -pl study-server -Dtest=TestClassName#testMethodName

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

## Naming Conventions

- **Classes**: PascalCase (e.g., `UserController`, `LoginServiceImpl`)
- **Methods**: camelCase (e.g., `userLogin`, `addRoom`)
- **Variables**: camelCase (e.g., `userName`, `pageResult`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `STATUS_NORMAL`)
- **Packages**: lowercase (e.g., `com.study.controller.admin`)

## Code Style

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
- Use `@RestController`, `@RequestMapping`
- Use `@Tag` + `@Operation` for Knife4j API docs
- Use `@Slf4j` for logging
- Always return `Result<T>`
- Inject services via `@Autowired`

```java
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员接口")
public class AdminController {
    @Autowired private AdminService adminService;

    @GetMapping("/user/list")
    @Operation(summary = "查询用户列表")
    public Result<PageResult> getUserList(UserQueryDTO queryDTO) {
        log.info("查询用户列表, 条件: {}", queryDTO);
        return Result.success(adminService.getUserList(queryDTO));
    }
}
```

### Service Layer
- Use `@Service` with interface-based design
- Use `@Autowired` for DI
- Business logic + logging

```java
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired private LoginMapper loginMapper;

    @Override
    public String userLogin(UserLoginDTO loginDTO) {
        User user = loginMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return JwtUtil.createToken(user.getUsername(), user.getUsername());
    }
}
```

### Mapper Layer
- Use `@Mapper` annotation or XML mappers
- XML mappers: `src/main/resources/mapper/`

### DTO/VO Classes
- Located in `study-pojo/src/main/java/com/study/dto/` and `vo/`
- Use Lombok annotations
- DTOs = request, VOs = response

## Imports Organization

Order imports:
1. Java/JDK (java.*, javax.*)
2. Spring (org.springframework.*)
3. Third-party (lombok, mybatis, swagger, etc.)
4. Project (com.study.*)

## Error Handling

- Use `RuntimeException` for business logic errors
- Wrap responses: `Result.success(data)` or `Result.error(msg)`
- Log before operations: `log.info("...")` or `log.error("...", e)`

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
|----------|---------|
| Spring Boot | 3.2.5 |
| MyBatis | 3.0.3 |
| Lombok | 1.18.32 |
| JWT (jjwt) | 0.12.5 |
| Druid | 1.2.23 |
| PageHelper | 2.1.0 |
| Knife4j | 4.4.0 |

## Testing

No tests currently exist. When adding tests:
- Place in `src/test/java/` matching package structure
- Use JUnit with Spring Boot Test
- Naming: `*Test.java` or `*Tests.java`