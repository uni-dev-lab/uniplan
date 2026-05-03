# Backend Test Rules

## Test Types

| Type        | Suffix       | Annotations                                | Purpose                       |
|-------------|--------------|--------------------------------------------|-------------------------------|
| Unit        | `*Test.java` | `@ExtendWith(MockitoExtension.class)`      | Pure logic in isolation       |
| Integration | `*IT.java`   | (planned) Spring Boot test slice + H2      | Full controller→repo flow     |

Integration tests are not yet established in uniplan (one PR is in progress). Until that lands, all backend tests are unit tests with Mockito.

## Stack

- **JUnit 5** (`junit-jupiter`)
- **Mockito** (`mockito-core`, `mockito-junit-jupiter`)
- **H2** in-memory for any future integration tests (Liquibase disabled in test profile — entity-only schema)

## Running Tests

Use the Maven wrapper. Run each command as a separate Bash call (do NOT chain with `&&`).

```
./mvnw -B verify                                # full build + all tests
./mvnw test                                     # all tests only
./mvnw test -Dtest=MajorServiceTest             # single class
./mvnw test -Dtest=MajorServiceTest#createMajorShouldReturnAndSavedMajorDTO  # single method
```

CI (`.github/workflows/build.yml`) runs `mvn -B clean verify` on every PR against `main`.

---

# Unit Tests

## Test Class Structure

For a new Facade-pattern feature, **each layer is unit-tested in isolation** with its collaborators mocked.

### Facade test (orchestration, transactions, exception mapping)

```java
@ExtendWith(MockitoExtension.class)
class ProgramWebFacadeTest {

    @InjectMocks
    private ProgramWebFacade programWebFacade;

    @Mock
    private ProgramService programService;

    @Mock
    private ProgramValidator programValidator;

    @Mock
    private ProgramMapper programMapper;

    @Test
    void createProgram_shouldReturnResponseDto_whenInputIsValid() {
        // test
    }

    @Test
    void createProgram_shouldThrowResourceNotFoundException_whenFacultyMissing() {
        // test — verify validator throws and the facade propagates
    }
}
```

The Facade test is the primary place to assert:
- The Validator is called before the Service (`InOrder` verification).
- `ResourceNotFoundException` is thrown when the Service returns `Optional.empty()`.
- The MapStruct mapper is called on the saved entity, not the input.
- Logging happens at INFO for the business operation (use `LogCaptor` if asserting log output).

### Service test (repository interaction only)

```java
@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {

    @InjectMocks
    private ProgramService programService;

    @Mock
    private ProgramRepository programRepository;

    @Test
    void findById_shouldReturnEntity_whenProgramExists() {
        // test
    }
}
```

Service tests are thin: a one-line `findById` delegating to the repository deserves at most one happy-path test. Do not pad Service tests with assertions that belong on the Facade (validation order, exception types, DTO conversion).

### Validator test (one test per business rule)

```java
@ExtendWith(MockitoExtension.class)
class ProgramValidatorTest {

    @InjectMocks
    private ProgramValidator programValidator;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private ProgramRepository programRepository;

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenFacultyMissing() {
        // test
    }

    @Test
    void validateForCreate_shouldPass_whenFacultyExistsAndNameIsUnique() {
        // test
    }
}
```

One test per rule — happy path plus each failure mode. The Validator is where business rules are documented through tests.

### Pre-existing Service-only features

Existing features (e.g. `major/`) still combine orchestration + transactions + exception throwing in the Service. Their `*ServiceTest` classes can keep the existing shape until the feature is migrated to the Facade pattern. Do not preemptively split them.

```java
@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

    @Mock
    private MajorMapper majorMapper;

    @Test
    void createMajor_shouldReturnSavedDto_whenInputIsValid() {
        // test
    }
}
```

## Test Naming

Pattern: `methodName_shouldExpectedBehavior_whenCondition`.

```java
void findMajorById_shouldReturnDto_whenMajorExists() { }
void findMajorById_shouldThrowResourceNotFoundException_whenMajorMissing() { }
void deleteMajor_shouldRemoveEntity_whenMajorExists() { }
```

The existing tests (e.g. `MajorServiceTest`) use a camelCase descriptive variant (`createMajorShouldReturnAndSavedMajorDTO`). New tests use the `_shouldX_whenY` snake-segment form for clarity. Migration of older tests is opportunistic — do not blanket-rename in unrelated PRs.

## Structure

- Given–When–Then with **blank-line separators**.
- Do NOT add `// Given` / `// When` / `// Then` comments.

## Assertions

- Prefer **AssertJ** (`assertThat`, `assertThatThrownBy`) — chainable, more expressive.
- Existing tests use JUnit `assertEquals` / `assertNotNull`. Migration is opportunistic — do not force.
- One logical assertion per test (multiple `assertThat` OK when verifying the same logical concept).

## Mockito Patterns

```java
when(repository.findById(id)).thenReturn(Optional.of(entity));
when(repository.findById(id)).thenReturn(Optional.empty());

verify(repository).save(entity);
verify(repository, never()).delete(any());

@Captor
private ArgumentCaptor<Major> majorCaptor;
verify(repository).save(majorCaptor.capture());
assertThat(majorCaptor.getValue().getMajorName()).isEqualTo("Informatics");
```

## Test Data with Fixtures

Shared test data should live in `org.unilab.uniplan.fixtures.<Feature>Fixture` (`@UtilityClass`):
- `aComplete<Feature>()` — fully populated entity / DTO builder.
- `a<Feature>WithNullFields()` — partial / minimal builder for null-safety tests.

uniplan does not yet have a fixtures package. Introduce it the first time a piece of test data is needed in two or more test classes — until then, building inline in `@BeforeEach` is acceptable.

Use `final var` for local variables in tests.

---

# Integration Tests (target shape, when introduced)

- Class name ends with `IT` (not `Test`).
- Same `methodName_shouldBehavior_whenCondition` naming.
- Same Given–When–Then structure (blank-line separators, no comments).
- HTTP via `MockMvc` / `MockMvcTester`. AssertJ for response assertions.
- The test profile uses H2 in-memory; if a real Postgres is needed for a specific test (e.g. for a Postgres-only feature), use Testcontainers.

---

# Checklist

## Unit Test Checklist

1. `@ExtendWith(MockitoExtension.class)` on class.
2. Naming `methodName_shouldBehavior_whenCondition` for new tests.
3. Given–When–Then with blank-line separators (no comments).
4. AssertJ preferred for new tests; existing JUnit assertions OK.
5. One logical assertion per test.
6. Dependencies mocked with `@Mock`, class under test with `@InjectMocks`.
7. Shared data in fixture classes once it's needed in 2+ tests.
8. **For new Facade-pattern features**: separate test classes for Facade, Service, and Validator. Do not collapse them into a single test class — each layer has different assertions to make.
9. **Facade tests** assert orchestration (`InOrder` between Validator and Service), exception mapping (Service `Optional.empty()` → `ResourceNotFoundException`), and DTO conversion calls. Do not duplicate Service-level repository assertions or Validator-level rule assertions in Facade tests.

## Integration Test Checklist (when applicable)

1. Class name ends with `IT`.
2. Same naming, same Given–When–Then structure.
3. MockMvc / MockMvcTester for HTTP calls; AssertJ for response assertions.
4. Liquibase stays disabled in test profile — schema from entity definitions, seed data per test.
