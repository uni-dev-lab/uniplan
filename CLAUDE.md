# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Common commands

The project uses the Maven wrapper. Use `./mvnw` (Linux/macOS) or `mvnw.cmd` (Windows).

- Build (compile + tests + package): `./mvnw clean verify`
- Run the app locally: `./mvnw spring-boot:run` — requires `POSTGRES_URL`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD` env vars (see `compose.yaml` / `application.yaml`).
- Run all tests: `./mvnw test`
- Run a single test class: `./mvnw test -Dtest=MajorServiceTest`
- Run a single test method: `./mvnw test -Dtest=MajorServiceTest#createMajorShouldReturnAndSavedMajorDTO`
- Run via Docker Compose (app + Postgres): `docker compose up --build` — requires `db/password.txt` and the `POSTGRES_*` env vars from `compose.yaml`.
- Liquibase changeset operations go through the Maven plugin, e.g. `./mvnw liquibase:status` (driver/changeLogFile already configured in `pom.xml`).

CI (`.github/workflows/build.yml`) runs `mvn -B clean verify` on PRs against `main`. Tests must pass for merge.

## Runtime configuration

- All API endpoints are served under the `/api` context path (`spring.mvc.servlet.path` in `application.yaml`).
- Production profile uses PostgreSQL with `ddl-auto: validate` — schema is owned by Liquibase, never by Hibernate. Adding/changing entity columns requires a corresponding changeset under `src/main/resources/db/changelog/`, registered in `db.changelog-master.yaml`.
- Tests (`src/test/resources/application.yaml`) use H2 in-memory with `ddl-auto: create-drop` and Liquibase **disabled** — entity-only schema. Service tests are pure Mockito and don't boot Spring.
- Swagger UI is available at `/api/swagger-ui.html` (springdoc, configured in `config/SwaggerConfig.java` which also injects standard error responses on every operation).
- i18n: `LocaleConfig` registers a `?lang=` query parameter interceptor and session locale resolver; default is English. Error messages resolve via `MessageSource` from `messages*.properties`.

## Architecture

Spring Boot 3.4 / Java 21 monolith. Code lives under `org.unilab.uniplan` (note: `unilab`, not `uniplan` as the README diagram suggests). The package layout is **feature-modular**: each domain entity gets its own top-level package and follows the same internal shape.

### The standard feature-package shape

uniplan is migrating to a Facade-based layering: `Controller → Facade (WebFacade) → Service → Repository`. **All new features use this shape.** Existing features (`major/`, `course/`, `student/`, etc.) still use the older direct-Service shape and will be migrated incrementally — see `.claude/rules/backend.md` § Architecture Layers + Appendix B for full details.

#### New features (Facade pattern — required)

For a feature `X`, the package contains:

- `X.java` — JPA entity extending `common.model.BaseEntity` or `Person`.
- `XRepository.java` — `JpaRepository<X, UUID>` with custom finders.
- `XService.java` — `@Service`, no `@Transactional`, returns `Optional<X>` / `X` / `List<X>` (entities, not DTOs). Does not throw `ResourceNotFoundException`.
- `XWebFacade.java` — `@Component @Slf4j @RequiredArgsConstructor`, owns `@Transactional`, orchestrates Validator + Service + Mapper, unwraps `Optional` to throw `ResourceNotFoundException`, logs business operations at INFO.
- `XValidator.java` — `@Component`, semantic/business validation only. Skip the file entirely if the feature has no rules beyond `@Valid` annotations.
- `XMapper.java` — MapStruct interface; global compiler args (`defaultComponentModel=spring`, `defaultInjectionStrategy=constructor`) make it an auto-wired Spring bean. Composes peer mappers via `@Mapper(uses = {...})`.
- `XController.java` — `@RestController @RequestMapping("/xs")`, injects ONLY the Facade, takes `XRequestDto` in / returns `XResponseDto` out.
- `dto/` — records:
  - `XRequestDto` (client → server, no `id`, validation annotations).
  - `XResponseDto` (server → client).
  - Optional `XFooResponseDto` for nested-projection endpoints (e.g. `MajorCoursesResponseDto`).
  - Internal `XDto` is no longer required for new features — the Facade works in entities and converts straight to/from request/response DTOs. Add it only when there is a genuine reason (cross-feature reuse, response shape divergence).

A worked reference example lives in `.claude/rules/backend.md` § Appendix A. Use it — do not paraphrase from older Service-only features when scaffolding a new one.

#### Existing features (Service-only — legacy, do not refactor opportunistically)

Pre-migration features still look like:

- `XService.java` — `@Service`, owns `@Transactional`, throws `ResourceNotFoundException`, returns DTOs.
- `XController.java` — calls Service + Mapper directly.
- `XDto` (internal) is present and round-tripped through the Service.

Do not refactor these as a side effect of unrelated work. Migrating a feature to the Facade pattern is its own focused PR (see `.claude/rules/backend.md` § Appendix B).

### Cross-cutting infrastructure

- `common/model/BaseEntity` — UUID PK assigned in `@PrePersist`; `equals`/`hashCode` are Hibernate-proxy-aware (use class identity, not field equality). Don't override them.
- `common/model/AuditableEntity` — `created_at` / `updated_at` populated by JPA auditing (`config/JpaAuditingConfig` enables it).
- `exception/GlobalExceptionHandler` — `@RestControllerAdvice` translating `ResourceNotFoundException` (404), `HandlerMethodValidationException` (400), `MethodArgumentTypeMismatchException` (400), `ResponseStatusException`, and generic `RuntimeException`/`Exception` (500) into a uniform `ErrorResponse`. New domain exceptions belong here.
- `utils/ErrorConstants` — enum of `*_NOT_FOUND` message templates (`MessageFormat`-style). Add a new entry per entity rather than inlining strings.
- Read-only collections on entities: see `Major.courses` — the field setter is suppressed (`@Setter(AccessLevel.NONE)`) and the getter returns `Collections.unmodifiableList(...)`. Follow the same pattern for `@OneToMany` collections that shouldn't be reassigned by callers.

### Coding conventions enforced by `.editorconfig`

Java files use **4-space indent**, **CRLF line endings**, **100-char max line length**. Most other files use 2-space indent. The IntelliJ formatter settings are extensive — match them when editing.

## Coding Conventions

Detailed rules live in `.claude/rules/` and should be read before implementing or reviewing code:

- `.claude/rules/backend.md` — Backend architecture, patterns, style.
- `.claude/rules/backend-test.md` — Backend testing conventions.
- `.claude/rules/implementation-common.md` — Triage, test-output hygiene, Bash usage rules for agents.

<<<<<<< HEAD
Where rules call out intentional deviations from prior conventions (e.g. no Facade layer, no `@PreAuthorize`, no Keycloak in legacy features), those deviations are explicit in the rules files.
=======
The structure mirrors the [eJourney](https://github.com/SAPTCO/ejourney) project's `.claude/` layout so workflow ideas (skills, agents, commands) can be ported across with minimal translation. Where uniplan deviates intentionally (e.g. no Facade layer, no `@PreAuthorize`, no Keycloak), it is called out explicitly in the rules files.
>>>>>>> 033a30e (Add Claude Code config, Facade-pattern rules, and PR review workflow)

## Project Subagents

- `uniplan-be-reviewer` — backend reviewer (read-only); produces review findings against project rules. Cannot edit code. Defined in `.claude/agents/uniplan-be-reviewer.md`. Invoke via the Agent tool with `subagent_type: uniplan-be-reviewer`. Used by the `/review-pr` skill and the `Claude Code Review` GitHub Actions workflow.

No implementer subagent yet — implementations happen in the main session, where CLAUDE.md preloading already gives the conversational loop access to project conventions. Add an implementer when (a) the codebase grows beyond what fits comfortably in the rules files, or (b) a second backend dev / frontend / auth module starts requiring orchestrated multi-task work.

## Project Skills

- `.claude/skills/review-pr/` — orchestrates a PR review: creates a worktree, classifies the diff, dispatches `uniplan-be-reviewer`, and merges findings.
- `.claude/skills/liquibase-changelog/` — scaffolds a new Liquibase changelog under `src/main/resources/db/changelog/<YYYY>/<MM>/<NNNN>-<slug>.yaml` with the mandatory `objectQuotingStrategy` and a `rollback:` block, and registers the include in `db.changelog-master.yaml`.
- `.claude/skills/create-worktree/` — creates (or reuses) a git worktree for a remote branch in a sibling `uniplan-worktree/` directory.

## Bash Usage for Agents

When working in `dontAsk` permission mode (CI / autonomous runs):

- Never use compound Bash commands (`&&`, `;`, `||`). Run each command as a separate Bash tool call.
- Never prefix commands with `cd <path> &&`. Use `git -C <path>` for git, or run Maven from the correct directory in a separate call.
- Never use shell redirect operators (`>`, `>>`, `|`). Let the Bash tool capture stdout; if output needs to be saved, use the Write tool with the captured result.

Reason: allow-list patterns match the full command string; compound commands break pattern matching and get rejected, blocking autonomous work. See `.claude/rules/implementation-common.md` for details.
