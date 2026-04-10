# AGENTS.md - StudyRoom Project Guidelines

This document provides guidelines for agentic coding agents working in this repository.

## Project Overview

- **Type**: Java Spring Boot 3.2.5 Multi-Module Maven Project
- **Modules**: study-common, study-pojo, study-server
- **Package**: com.study
- **Database**: MySQL with Druid connection pool
- **ORM**: MyBatis

## Build Commands

### Build All Modules
```bash
cd /home/hhwf/code/java/studyRoom
mvn clean install
```

### Build Single Module
```bash
mvn clean install -pl study-server -am
```

### Run Application
```bash
mvn spring-boot:run -pl study-server
```

### Run Single Test
```bash
mvn test -pl study-server -Dtest=TestClassName#testMethodName
```

### Run All Tests
```bash
mvn test -pl study-server
```

### Linting (if configured)
```bash
mvn checkstyle:check
```

## Code Style Guidelines

### Project Structure
```
studyRoom/
├── study-common/        # Shared utilities, Result wrapper, constants
├── study-pojo/          # DTOs, Entities, VOs
└── study-server/        # Controllers, Services, Mappers
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `UserController`, `LoginServiceImpl`)
- **Methods**: camelCase (e.g., `userLogin`, `addRoom`)
- **Variables**: camelCase (e.g., `userName`, `pageResult`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `STATUS_NORMAL`)
- **Packages**: lowercase (e.g., `com.study.controller.admin`)

### Entity Classes
- Use Lombok annotations: `@Data`, `@Builder`, `@AllArgsConstructor`, `@NoArgsConstructor`
- Implement `Serializable` with `serialVersionUID`
- Example:
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String name;
    private String phone;
    private LocalDateTime createTime;
}
```

### DTO/VO Classes
- Located in `study-pojo/src/main/java/com/study/dto/` and `com/study/vo/`
- Use Lombok annotations
- DTOs for request data, VOs for response data

### Controller Layer
- Use `@RestController`, `@RequestMapping`
- Use `@Tag(name = "...")` and `@Operation(summary = "...")` for API documentation (Knife4j)
- Use `@Slf4j` for logging
- Inject services via `@Autowired`
- Always return `Result<T>` wrapper

### Service Layer
- Use `@Service` annotation
- Implement interface-based design (e.g., `LoginServiceImpl` implements `LoginService`)
- Use `@Autowired` for dependency injection

### Mapper Layer
- Use `@Mapper` annotation or XML-based MyBatis mappers
- XML mappers stored in `src/main/resources/mapper/`

### Imports Organization
Order imports:
1. Java/JDK imports
2. Spring imports
3. Third-party library imports (Lombok, MyBatis, etc.)
4. Project imports (com.study.*)

### Error Handling
- Use custom `RuntimeException` for business logic errors
- Wrap all responses in `Result<T>` - use `Result.success(data)` or `Result.error(msg)`
- Add logging with `@Slf4j` before operations

### Database
- Use MySQL with Druid connection pool
- Use PageHelper for pagination
- Use MyBatis with XML mappers or annotations

### API Documentation
- Uses Knife4j (OpenAPI 3)
- Annotate controllers with `@Tag` and `@Operation`
- Access docs at `/doc.html` when running

### Configuration
- Application config: `src/main/resources/application.yml`
- Environment configs: `application-dev.yml`, `application-prod.yml`
- MyBatis mappers: `src/main/resources/mapper/`

### Key Dependencies
- Spring Boot 3.2.5
- MyBatis 3.0.3
- MySQL Connector
- Lombok 1.18.32
- JWT (jjwt) 0.12.5
- Druid 1.2.23
- PageHelper 2.1.0
- Knife4j 4.4.0

## Testing

Currently no test files exist in the project. When adding tests:
- Place in `src/test/java/` under matching package structure
- Use JUnit with Spring Boot Test
- Follow naming: `*Test.java` or `*Tests.java`

## Common Patterns

### API Response Wrapper
```java
@RestController
@RequestMapping("/admin")
public class AdminController {
    @PostMapping("/room/add")
    public Result addRoom(@RequestBody RoomAddDTO roomAddDTO) {
        adminService.addRoom(roomAddDTO);
        return Result.success();
    }
}
```

### Service Implementation
```java
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

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

## Running the Application

1. Ensure MySQL is running and configured in `application.yml`
2. Build: `mvn clean install -DskipTests`
3. Run: `mvn spring-boot:run -pl study-server`
4. Access API docs: `http://localhost:8080/doc.html`
