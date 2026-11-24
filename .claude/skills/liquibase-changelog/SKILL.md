---
name: liquibase-changelog
description: Scaffold a new Liquibase changelog for the uniplan backend. Creates src/main/resources/db/changelog/<yyyyMMddHHmmss>-<slug>.yaml (UTC timestamp, flat — no subfolders) with the mandatory objectQuotingStrategy and a rollback block, and appends the include to db.changelog-master.yaml. Use when adding a new database migration.
disable-model-invocation: false
argument-hint: <kebab-case-slug-describing-the-change>
---

You are scaffolding a new Liquibase changelog. The change slug is: $ARGUMENTS

If `$ARGUMENTS` is empty, ask the invoker for a short kebab-case slug describing the change (e.g. `add-major-credit-hours-column`, `drop-legacy-room-flag`) before proceeding.

Follow these steps exactly. Run each step as a **separate Bash call** — do NOT combine them with `&&` or shell variables.

## Step 1: Generate the UTC timestamp

Run `date -u +%Y%m%d%H%M%S` and capture the 14-digit result as `${TS}` (e.g. `20260806115300`). It prefixes both the filename and the changeset id.

## Step 2: Get the current git user name

Run `git config user.name` and capture the result as `${AUTHOR}`. If the command returns an empty string or errors, stop and ask the invoker to set `git config user.name` before continuing.

## Step 3: Verify the target path is free

Changelogs live flat under `src/main/resources/db/changelog/` — no year/month subfolders. The target file path is `src/main/resources/db/changelog/${TS}-<slug>.yaml`.

Run `ls src/main/resources/db/changelog/`. If a file with the same `${TS}` prefix already appears, stop and report (re-run Step 1 after a second rather than editing the timestamp by hand).

## Step 4: Create the changelog file

Write the file with exactly this skeleton (substitute `${TS}`, `${AUTHOR}`; number further changesets in the same file `${TS}-2`, `${TS}-3`, ...):

```yaml
databaseChangeLog:
  - changeSet:
      id: ${TS}-1
      author: ${AUTHOR}
      objectQuotingStrategy: QUOTE_ONLY_RESERVED_WORDS
      changes:
        # fill in: add change operations (createTable / addColumn / dropColumn / ...)
      rollback:
        # fill in: add inverse operations that undo `changes:` (e.g. dropColumn for addColumn,
        # dropTable for createTable). If the change is genuinely irreversible (data transforms,
        # drops of non-recoverable data), replace this block with `rollback: []` and add a
        # one-line comment above explaining why rollback is empty.
```

The scaffold includes `rollback:` by default — every new changelog should either specify inverse operations or explicitly document why rollback is not possible.

## Step 5: Register the include in the master changelog

Open `src/main/resources/db/changelog/db.changelog-master.yaml` and append a new include block at the end, preserving the two-space indentation used in the file:

```yaml
  - include:
      file: db/changelog/${TS}-<slug>.yaml
```

## Step 6: Report

Print:

- The path of the new changelog file (clickable).
- A reminder to fill in the `changes:` block **and** the `rollback:` block (or replace the rollback block with `rollback: []` + a comment explaining why the change is irreversible).
- A reminder to update the corresponding JPA entity if the change affects column type / length / nullability — the entity `@Column(length = N)` must match the changelog's `varchar(N)` (prod runs `ddl-auto: validate`).
- A reminder to run `./mvnw -B verify` to confirm Liquibase loads the new changelog and Hibernate validates the schema without error.

## Rules

- `author` is the committer name captured from `git config user.name` in Step 2 — never hardcode a value.
- Every changeSet MUST carry `objectQuotingStrategy: QUOTE_ONLY_RESERVED_WORDS` per [.claude/rules/backend.md](../../rules/backend.md). Do not omit it.
- Never overwrite or edit an existing changelog file — already-applied changesets are frozen; make changes in a new changelog.
- Filename format is `<yyyyMMddHHmmss>-<slug>.yaml` (UTC timestamp, kebab-case slug), flat under `src/main/resources/db/changelog/`. Do not deviate.
