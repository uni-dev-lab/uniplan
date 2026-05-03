---
name: liquibase-changelog
description: Scaffold a new Liquibase changelog for the uniplan backend. Creates src/main/resources/db/changelog/<YYYY>/<MM>/<NNNN>-<slug>.yaml with the mandatory objectQuotingStrategy and a rollback block, and appends the include to db.changelog-master.yaml. Use when adding a new database migration.
disable-model-invocation: false
argument-hint: <kebab-case-slug-describing-the-change>
---

You are scaffolding a new Liquibase changelog. The change slug is: $ARGUMENTS

If `$ARGUMENTS` is empty, ask the invoker for a short kebab-case slug describing the change (e.g. `add-major-credit-hours-column`, `drop-legacy-room-flag`) before proceeding.

Follow these steps exactly. Run each step as a **separate Bash call** — do NOT combine them with `&&` or shell variables.

## Step 1: Get today's date (UTC)

Run `date -u +%Y-%m-%d` and capture three values:
- `${YEAR}` — the four-digit year (e.g. `2026`)
- `${MONTH}` — the two-digit month (e.g. `05`)
- `${DATE_FULL}` — the full date string (e.g. `2026-05-03`), used in the changeset id only.

## Step 2: Get the current git user name

Run `git config user.name` and capture the result as `${AUTHOR}`. If the command returns an empty string or errors, stop and ask the invoker to set `git config user.name` before continuing.

## Step 3: Compute the next sequence number

Run `ls src/main/resources/db/changelog/${YEAR}/${MONTH}/` (create the directory in Step 4 if `ls` errors with "No such file or directory"). From the output, enumerate every filename matching `NNNN-*.yaml` — do not attempt arithmetic shortcuts; read each filename literally.

- If the directory does not yet exist or no files are present, set `${NN}` = `0001`.
- Otherwise take the highest existing four-digit prefix across all of `src/main/resources/db/changelog/**/*.yaml` (NOT just this month's folder — the sequence is repository-wide, not per-month — see existing `0001-...` and `0002-...` files), increment by 1, and zero-pad to four digits.

Run `find src/main/resources/db/changelog -type f -name '*.yaml' -not -name 'db.changelog-master.yaml'` to enumerate every existing changelog and read the highest prefix.

## Step 4: Ensure the target directory exists

Run `mkdir -p src/main/resources/db/changelog/${YEAR}/${MONTH}`.

## Step 5: Verify the target path is free

The target file path is `src/main/resources/db/changelog/${YEAR}/${MONTH}/${NN}-<slug>.yaml`.

Derive the target path from Step 3's enumeration — if it already appears, stop and report. Do not re-probe with another `ls`.

## Step 6: Create the changelog file

Write the file with exactly this skeleton (substitute `${DATE_FULL}`, `${NN}`, `${AUTHOR}`, and the slug from `$ARGUMENTS`):

```yaml
databaseChangeLog:
  - changeSet:
      id: ${NN}-<slug>
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

## Step 7: Register the include in the master changelog

Open `src/main/resources/db/changelog/db.changelog-master.yaml` and append a new include block at the end, preserving the two-space indentation used in the file:

```yaml
  - include:
      file: db/changelog/${YEAR}/${MONTH}/${NN}-<slug>.yaml
```

## Step 8: Report

Print:

- The path of the new changelog file (clickable).
- A reminder to fill in the `changes:` block **and** the `rollback:` block (or replace the rollback block with `rollback: []` + a comment explaining why the change is irreversible).
- A reminder to update the corresponding JPA entity if the change affects column type / length / nullability — the entity `@Column(length = N)` must match the changelog's `varchar(N)` (prod runs `ddl-auto: validate`).
- A reminder to run `./mvnw -B verify` to confirm Liquibase loads the new changelog and Hibernate validates the schema without error.

## Rules

- `author` is the committer name captured from `git config user.name` in Step 2 — never hardcode a value.
- Every changeSet MUST carry `objectQuotingStrategy: QUOTE_ONLY_RESERVED_WORDS` per [.claude/rules/backend.md](../../rules/backend.md). Do not omit it.
- Never overwrite an existing changelog file.
- Filename format is `NNNN-<slug>.yaml` (four-digit zero-padded NN, repository-wide sequence). Do not deviate.
- Do not edit the existing baseline files (`0001-baseline-init-changelog.yaml`, `0002-seed-university-data-from-csv.yaml`) — they are frozen.
