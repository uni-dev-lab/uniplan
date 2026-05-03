# Backend Rules — Spring Boot 3 / Java 21

## Stack

Java 21, Spring Boot 3, Maven, Lombok, MapStruct, Spring Data JPA, Liquibase, PostgreSQL, H2 (test only), springdoc OpenAPI

## Architecture Layers

```
Controller → Facade (WebFacade) → Service → Repository → Entity
```

<<<<<<< HEAD
uniplan is migrating to a Facade architecture. **All new feature work must use the Facade layer.** Existing features (`major/`, `course/`, `student/`, etc.) where Controllers still call Services directly are pre-existing and will be migrated incrementally — do not refactor them as a side effect of unrelated work.
=======
uniplan is migrating to the eJourney-style Facade architecture. **All new feature work must use the Facade layer.** Existing features (`major/`, `course/`, `student/`, etc.) where Controllers still call Services directly are pre-existing and will be migrated incrementally — do not refactor them as a side effect of unrelated work.
>>>>>>> 033a30e (Add Claude Code config, Facade-pattern rules, and PR review workflow)

### Controller
- Thin delegation layer — no business logic, no logging, no entity↔DTO conversion.
- Syntactic validation via `@Valid` + Jakarta annotations on request bodies.
- Annotated: `@RestController`, `@RequiredArgsConstructor`, `@RequestMapping("/<entities>")`.
- Tag controllers with `@Tag(name = ..., description = ...)` from springdoc for the OpenAPI UI.
- **Calls the Facade only — never a Service or Repository directly.** Returns whatever the Facade returns (a response DTO or a list of response DTOs).

### Facade (WebFacade)
- Orchestrates business logic across one or more Services.
- **Owns `@Transactional`** (writes) and `@Transactional(readOnly = true)` (reads). Never on Services or Repositories.
- Calls a dedicated `<Feature>Validator` component for semantic (business) validation when business rules go beyond Jakarta annotations.
- Handles `Optional` from Services / Repositories — throws `ResourceNotFoundException` (with the matching `ErrorConstants.*_NOT_FOUND.getMessage(id)`) if absent. The Service no longer throws this; that responsibility moves up to the Facade.
- Converts entities↔DTOs via MapStruct.
- Logs business operations at INFO level (`log.info("Creating major {}", request.majorName())`).
- Annotated: `@Component`, `@Slf4j`, `@RequiredArgsConstructor`.
- File location: alongside the Service in the feature package, named `<Feature>WebFacade.java` (e.g. `MajorWebFacade.java`).
- One Facade method = one business operation = one transaction. Resist the urge to inline multiple operations into a single method to "save a round-trip".

### Service
- Thin wrapper around the repository with focused domain logic.
- **Receives already-validated, non-null data** from the Facade — no defensive null checks, no `@Valid`-style assertions.
- **No `@Transactional`** — the Facade owns transactions.
- Pure domain operations: `findById(id) -> Optional<Entity>`, `save(entity)`, `delete(entity)`, custom finders, calculations. Returns entities (or projections), not DTOs.
- Returns `Optional` for single-entity lookups so the Facade can decide whether absence is a 404, a fallback, or just expected.
- Annotated: `@Service`, `@RequiredArgsConstructor`. Add `@Slf4j` only when actually logging WARN/ERROR for unexpected situations or DEBUG for troubleshooting.

### Validator (semantic / business validation)
- Used when validation goes beyond Jakarta annotations (e.g. "this faculty must already exist", "this major name is unique within the faculty", "this date range cannot overlap an existing program").
- Annotated: `@Component`, `@RequiredArgsConstructor`.
- Named `<Feature>Validator`, lives in the feature package alongside the Facade.
- Methods are verbs that throw on failure: `validateForCreate(request)`, `validateForUpdate(id, request)`, `validateExists(id)`. Failure throws an exception that `GlobalExceptionHandler` already maps (e.g. `ResourceNotFoundException` for missing FK targets, a future `BusinessValidationException` for rule violations).
- Called by the Facade as the first thing in the method body, before any Service call.
- Skip the Validator class entirely when a feature has no semantic rules beyond `@Valid` annotations on the request DTO. Don't create empty placeholders.

### Repository
- Extends `JpaRepository<Entity, UUID>`.
- Returns `Optional` for single-entity lookups.
- JPQL with `@Query` and fetch joins to prevent N+1 when needed; `@Repository` annotation is fine.
- `Pageable` / `Page<T>` for pagination.

### Layer rules summary

| From / To       | Controller | Facade | Service | Repository |
|-----------------|:----------:|:------:|:-------:|:----------:|
| **Controller**  | —          | ✅     | ❌      | ❌         |
| **Facade**      | —          | ⚠️ same-feature only | ✅ | ❌ (go through Service) |
| **Service**     | —          | ❌     | ⚠️ same-feature only | ✅ |

- ❌ entries are blocking violations (Critical/Important in review).
- ⚠️ same-feature cross-calls are allowed but should be rare. A Facade calling another feature's Facade is fine when truly orchestrating across features; a Service calling another feature's Service is a smell — usually the Facade should orchestrate both.
- Mappers can be injected into both Facades and (rarely) Services. Prefer the Facade.

## Package Structure

Feature-based. Each domain module (`major/`, `course/`, `student/`, etc.) lives at `src/main/java/org/unilab/uniplan/<feature>/`.

**For new features**, the package contains:

- `<Feature>.java` — entity
- `<Feature>Repository.java`
- `<Feature>Service.java` — non-transactional, returns entities/`Optional`
- `<Feature>WebFacade.java` — `@Transactional`, orchestration, MapStruct conversion, business logging
- `<Feature>Validator.java` — only when semantic rules exist beyond `@Valid` annotations
- `<Feature>Mapper.java`
- `<Feature>Controller.java` — calls the Facade only
- `dto/` — request/response/internal DTOs

**Existing features** still follow the older `Controller → Service → Repository` shape (no Facade, no Validator, `@Transactional` on the Service). They are pre-existing — see the migration guidance under "Architecture Layers" above.

When adding a new feature, mirror the structure above. Do not copy an existing feature wholesale (it would propagate the old layering); instead, take its DTO/entity/mapper conventions and apply the new Facade pattern on top.

## Base Entity

All entities extend `BaseEntity` (abstract `@MappedSuperclass`):
- UUID `id` auto-generated in `@PrePersist` (do NOT rely on a database sequence).
- `equals` / `hashCode` are Hibernate-proxy-aware — do NOT override them.
- Auditing timestamps come from `AuditableEntity` (extended by `BaseEntity`): `created_at`, `updated_at`, populated by Spring Data JPA auditing (enabled in `config/JpaAuditingConfig`).

For person-like entities (students, lecturers), extend `Person` instead — it adds `firstName` / `lastName` on top of `BaseEntity`.

## DTOs

Per feature, four DTO records:

- `<Feature>RequestDto` — client → server, no `id`, validation annotations.
- `<Feature>ResponseDto` — server → client.
- `<Feature>Dto` — internal service-layer DTO, includes `id`.
- Optionally `<Feature><Projection>Dto` / `<Feature><Projection>ResponseDto` for nested-projection endpoints (e.g. `MajorCoursesDto` includes the course list).

Rules:
- Records, not classes.
- Never reuse a single DTO across request and response.
- Validation annotations (`@NotNull`, `@Size`, etc.) on the request DTO and the internal DTO.

## MapStruct

- `@Mapper(uses = {OtherMapper.class, ...})` only — `componentModel` and `injectionStrategy` are set globally in `pom.xml` (`-Amapstruct.defaultComponentModel=spring`, `-Amapstruct.defaultInjectionStrategy=constructor`). Mappers are auto-wired Spring beans.
- Provide methods for each conversion direction needed (`toEntity`, `toDto`, `toResponseDto`, `toInnerDto`, `toResponseDtoList`, `updateEntityFromDto`).
- `@Mapping(source = "...", target = "...")` for foreign-key-to-entity-id translations (see `MajorMapper` for the reference pattern).

## JPA Entities

- UUID primary keys via `BaseEntity` `@PrePersist` (no `@GeneratedValue`).
- `@Enumerated(EnumType.STRING)` for all enums.
- **Enum column lengths must match the Liquibase changelog**: when a string-backed column is declared `varchar(50)`, set `@Column(length = 50)`. JPA defaults to `varchar(255)`, which silently diverges.
- `FetchType.LAZY` on every `@ManyToOne` / `@OneToMany` / `@ManyToMany`.
- Money fields: `BigDecimal`.
- **Collection encapsulation**: never expose internal collections directly. Suppress the setter (`@Setter(AccessLevel.NONE)`) and have the getter return `Collections.unmodifiableList(field)`. Reference pattern: `Major.courses`. If callers need to mutate the collection, expose `add*` / `remove*` methods on the entity.

## Database & Migrations

- **PostgreSQL** in production with `spring.jpa.hibernate.ddl-auto: validate` — Liquibase owns the schema, Hibernate never modifies it.
- **H2 in-memory** for tests with `ddl-auto: create-drop`, Liquibase **disabled** (`src/test/resources/application.yaml`). Service unit tests are pure Mockito and don't boot Spring.
- Liquibase changelogs live under `src/main/resources/db/changelog/`, currently organized by `YYYY/MM/NNNN-<slug>.yaml`.
- Master changelog: `src/main/resources/db/changelog/db.changelog-master.yaml` — every new changelog must be added as an `include:` entry there.
- Always set `objectQuotingStrategy: QUOTE_ONLY_RESERVED_WORDS` on every `changeSet`.
- New changelogs should declare a `rollback:` block (inverse operations) or `rollback: []` with a comment explaining why the change is irreversible.

## Configuration

- All API endpoints served under `/api` (`spring.mvc.servlet.path` in `application.yaml`).
- Database connection from env: `POSTGRES_URL`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD`.
- Swagger UI at `/api/swagger-ui.html` (configured in `config/SwaggerConfig`, which also injects the standard 400/500 + per-method response codes on every operation).

## i18n Message Keys

- `MessageSource` is configured with basename `messages` and UTF-8 encoding (`fallback-to-system-locale: false`).
- Locale resolver: `SessionLocaleResolver` defaulting to English; locale switch via `?lang=<code>` query parameter (see `config/LocaleConfig`).
- Locale files: `messages.properties`, `messages_en.properties`, `messages_bg.properties` — keep them in sync. Every key must exist in every locale file.
- Keys must be clearly distinguishable; check existing keys before adding new ones.

## Exception Handling

`exception/GlobalExceptionHandler` (`@RestControllerAdvice`) maps exceptions to `ErrorResponse` with i18n message via `MessageSource`:

| Exception                              | HTTP Status | Use for                                   |
|----------------------------------------|-------------|-------------------------------------------|
| `ResourceNotFoundException`            | 404         | Entity not found                          |
| `HandlerMethodValidationException`     | 400         | `@Valid` failures on controller params    |
| `ResponseStatusException`              | from ex     | Explicit status thrown by code            |
| `MethodArgumentTypeMismatchException`  | 400         | Path/query type coercion failures         |
| `RuntimeException` (catch-all)         | 500         | Unhandled runtime                         |
| `Exception` (final catch-all)          | 500         | Anything else                             |

New domain exceptions belong in `org.unilab.uniplan.exception` and need a matching `@ExceptionHandler` method in `GlobalExceptionHandler`. Not-found message templates live in `utils.ErrorConstants` (enum, `MessageFormat`-style placeholders) — add an entry per entity rather than inlining the message.

## Java Code Style

- Use `final` keyword for method parameters and local variables.
- Use Lombok annotations (`@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Builder`, `@Slf4j`, `@NoArgsConstructor`, `@AllArgsConstructor`).
- Optimize imports — specific classes only, no wildcards. Remove unused imports.
- Parameterized logging: `log.info("Processing major {}", majorId)` — never `+` concatenation.
- Prefer streams over `for` / `forEach` where it reads cleanly.
- `.toList()` over `.collect(Collectors.toList())`.
- Never return `null` — return `List.of()` / `Optional.empty()` for empty results.
- Early return for guard clauses.
- No out-parameters: helper methods return their result, do not mutate caller-provided collections.
- **Extract repeated string literals** to `private static final` constants — especially i18n message keys, JSON field names, header names. Any literal used more than once, or whose value is part of an external contract, belongs in a constant.
- Use `<` instead of `!=` for size invariants where one side is bounded (e.g. `if (found.size() < uniqueIds.size())`).

## Editor / Style Enforcement

`.editorconfig` is the source of truth:
- Java: 4-space indent, **CRLF** line endings, 100-char line limit.
- Other files: 2-space indent.

Match the IntelliJ formatter settings in `.editorconfig` when editing.

---

## Appendix A — Worked Facade-pattern reference

Reference shape for a new feature. Use this as the template when adding one; do not paraphrase from older Service-only features.

### Controller

```java
@RestController
@RequestMapping("/programs")
@RequiredArgsConstructor
@Tag(name = "Programs", description = "Manage academic programs")
public class ProgramController {

    private final ProgramWebFacade programWebFacade;

    @PostMapping
    public ResponseEntity<ProgramResponseDto> addProgram(@RequestBody @NotNull
                                                         @Valid final ProgramRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(programWebFacade.createProgram(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponseDto> getProgramById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(programWebFacade.findProgramById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable @NotNull final UUID id) {
        programWebFacade.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
```

Note: the Controller takes the request DTO straight in and returns the response DTO straight out. No internal DTOs leak to it. No services, no mapper, no logging, no `@Transactional`.

### WebFacade

```java
@Component
@Slf4j
@RequiredArgsConstructor
public class ProgramWebFacade {

    private final ProgramService programService;
    private final ProgramValidator programValidator;
    private final ProgramMapper programMapper;

    @Transactional
    public ProgramResponseDto createProgram(final ProgramRequestDto request) {
        log.info("Creating program {}", request.programName());
        programValidator.validateForCreate(request);

        final Program entity = programMapper.toEntity(request);
        final Program saved = programService.save(entity);

        return programMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public ProgramResponseDto findProgramById(final UUID id) {
        final Program program = programService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                ErrorConstants.PROGRAM_NOT_FOUND.getMessage(String.valueOf(id))));

        return programMapper.toResponseDto(program);
    }

    @Transactional
    public void deleteProgram(final UUID id) {
        final Program program = programService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                ErrorConstants.PROGRAM_NOT_FOUND.getMessage(String.valueOf(id))));

        programService.delete(program);
    }
}
```

Note:
- The Validator is called first; it throws on rule violations.
- The Facade unwraps `Optional` from the Service (the Service no longer throws `ResourceNotFoundException`).
- The Facade is the only place `@Transactional` appears.
- Business operations are logged at INFO with parameterized arguments, never `+` concatenation.

### Validator

```java
@Component
@RequiredArgsConstructor
public class ProgramValidator {

    private final FacultyRepository facultyRepository;
    private final ProgramRepository programRepository;

    public void validateForCreate(final ProgramRequestDto request) {
        validateFacultyExists(request.facultyId());
        validateProgramNameUniqueWithinFaculty(request.facultyId(), request.programName());
    }

    private void validateFacultyExists(final UUID facultyId) {
        if (!facultyRepository.existsById(facultyId)) {
            throw new ResourceNotFoundException(
                ErrorConstants.FACULTY_NOT_FOUND.getMessage(String.valueOf(facultyId)));
        }
    }

    private void validateProgramNameUniqueWithinFaculty(final UUID facultyId, final String programName) {
        if (programRepository.existsByFacultyIdAndProgramName(facultyId, programName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Program with name " + programName + " already exists in faculty " + facultyId);
        }
    }
}
```

The Validator is only created when at least one rule exists. When a feature has only `@Valid` syntactic rules, omit the Validator entirely — do not create an empty class.

### Service

```java
@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    public Optional<Program> findById(final UUID id) {
        return programRepository.findById(id);
    }

    public Program save(final Program program) {
        return programRepository.save(program);
    }

    public void delete(final Program program) {
        programRepository.delete(program);
    }

    public List<Program> findAllByFacultyId(final UUID facultyId) {
        return programRepository.findAllByFacultyId(facultyId);
    }
}
```

Note: returns entities (or `Optional<Entity>`), not DTOs. No `@Transactional`. No exception throwing for missing entities — the Facade decides whether absence is a 404.

### Mapper

Same MapStruct shape as today, but exposes both `<Feature>RequestDto -> <Feature>` and `<Feature> -> <Feature>ResponseDto` directions so the Facade no longer needs an internal `<Feature>Dto` round-trip:

```java
@Mapper
public interface ProgramMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Program toEntity(ProgramRequestDto request);

    @Mapping(source = "faculty.id", target = "facultyId")
    ProgramResponseDto toResponseDto(Program program);

    List<ProgramResponseDto> toResponseDtoList(List<Program> programs);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDto(ProgramRequestDto request, @MappingTarget Program program);
}
```

For new Facade-pattern features, the internal `<Feature>Dto` record can be omitted entirely — the Facade takes a `RequestDto` in, returns a `ResponseDto` out, and works in entities internally. Keep the internal DTO only when there is a genuine reason (e.g. cross-feature reuse where the response shape differs).

---

## Appendix B — Migration of pre-existing features

Existing features (e.g. `major/`) follow the old layering. When migrating one to the Facade pattern, do it in one focused PR per feature:

1. Add `<Feature>WebFacade` and (if needed) `<Feature>Validator`. Move `@Transactional` from the Service to the Facade methods.
2. Move the `ResourceNotFoundException` throw from the Service to the Facade. The Service now returns `Optional`.
3. Update the Controller to call the Facade only.
4. If the internal `<Feature>Dto` is no longer needed (Facade works in entities directly), keep it for now if it's used elsewhere — DTO removal is a follow-up cleanup, not part of the Facade migration.
5. Update the unit tests: the Service tests shrink to repository-call verification; new Facade tests cover orchestration, Validator interaction, transaction boundaries, and exception mapping.

Do not bundle Facade migrations of unrelated features into one PR.
