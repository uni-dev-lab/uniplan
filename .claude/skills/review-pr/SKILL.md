---
name: review-pr
description: Review a GitHub pull request by creating a local worktree and dispatching the uniplan-be-reviewer subagent
disable-model-invocation: true
argument-hint: <pr-number>
---

You are orchestrating a review of a GitHub pull request. The PR number is: $ARGUMENTS

Your role is a **thin dispatcher**: fetch PR metadata, prepare the worktree, classify the diff, spawn the `uniplan-be-reviewer` subagent against the correct scope, and present its findings together with your own review of any non-Java files (docs, CI, infra, root configs). Do not perform the backend review yourself — the subagent pre-loads project rules and produces the per-domain findings.

uniplan is currently a backend-only project; this skill is structured so a frontend reviewer can be added later as a sibling Agent dispatch without restructuring.

Follow these steps exactly.

## Step 1: Fetch PR metadata

Run:
```
gh pr view $ARGUMENTS --json number,title,body,baseRefName,headRefName,author,files
```

Extract the PR title, description, base branch, head branch, author, and list of changed files. Display a summary header like:

```
## PR #<number>: <title>
**Author:** <author>  |  **Base:** <base> <- <head>  |  **Files changed:** <count>
```

If the PR is not found, stop and inform the user.

## Step 2: Create a git worktree for the PR branch

**IMPORTANT: Always create the worktree — never skip this step, regardless of PR size or number of files changed.** The reviewer subagent reads source files from this worktree.

Run each command as a **separate Bash call**:

1. Get the repo root:
   ```
   git rev-parse --show-toplevel
   ```

2. Derive paths from the literal repo root captured above:
   - Normalize `<headRefName>` by replacing every `/` with `-` (e.g. `feature/UNI-1234` → `feature-UNI-1234`).
   - Worktree path: `<repo-root>/../uniplan-worktree/<normalized-branch>`.

3. Ensure the worktrees directory exists:
   ```
   mkdir -p <repo-root>/../uniplan-worktree
   ```

4. Fetch the branch:
   ```
   git fetch origin <headRefName>
   ```

5. Check whether a worktree for this branch already exists:
   ```
   git worktree list
   ```
   - If one already exists, skip to Step 3 and use its path as `<worktree-path>`.

6. If not, create it. Use the branch name (not `origin/<headRefName>`) so git's DWIM behavior creates a local branch tracking the remote — passing `origin/<headRefName>` results in a detached HEAD:
   ```
   git worktree add <worktree-path> <headRefName>
   ```
   If a local branch with that name already exists and points elsewhere, fall back to explicit tracking:
   ```
   git worktree add --track -b <headRefName> <worktree-path> origin/<headRefName>
   ```

The literal `<worktree-path>` derived above is used in all subsequent steps.

## Step 3: Classify the diff by touched area

From the `files` array returned in Step 1, classify the PR:

- **Backend files** — paths under `src/main/java/`, `src/main/resources/`, `src/test/java/`, `src/test/resources/`, or `pom.xml`.
- **Other files** — paths outside the backend slice (`docs/`, `.github/`, root configs like `Dockerfile`, `compose.yaml`, `.editorconfig`, `db/`, etc.).

The two dimensions are independent:

- **Domain scope** (for subagent dispatch):
  - `BE-only` — at least one backend file (current uniplan default).
  - `None` — no backend files (rare — docs/CI/infra-only PR).
- **Other slice** (for orchestrator direct review): non-empty whenever any file falls outside the backend slice. This is independent of domain scope: a `BE-only` PR can still have an Other slice that needs review.

Print one line announcing the classification and the dispatch plan, e.g.:
```
Diff scope: BE (12 backend files) + Other slice (2 files) — dispatching uniplan-be-reviewer; orchestrator will review the Other slice directly.
```

## Step 4: Dispatch the reviewer subagent

Spawn `uniplan-be-reviewer` if the **Backend** bucket is non-empty:

| Domain scope | Subagent to spawn                              |
|--------------|------------------------------------------------|
| `BE-only`    | 1 × `uniplan-be-reviewer`                      |
| `None`       | No subagent — only orchestrator's Other-slice review applies |

### Subagent prompt template

The reviewer subagent pre-loads its system prompt with the distilled ruleset — do **not** repeat those instructions. Pass only the PR-specific context. Use this template (substitute the literal values):

```
Review PR #<number> — "<title>".

Context line (use verbatim in your output heading): PR #<number> review

Worktree path (read source files from here, not the main repo):
<worktree-path>

Base branch: <baseRefName>
Head branch: <headRefName>

Get the diff by running, from the worktree:
  git -C <worktree-path> diff origin/<baseRefName>...HEAD -- src/ pom.xml

Apply the diff-scope rule from your system prompt: only flag issues in
newly added or modified lines. Pre-existing issues go under
"Pre-existing (out of scope)". You may read surrounding code in the worktree
for context.

Produce findings in the Review Output Format from your system prompt.
If no issues are found, state "No issues found." (still produce Summary + Praise sections).
```

This is a single-pass PR review — do NOT ask the subagent to modify any state files.

## Step 4b: Orchestrator review of the Other slice

Whenever the Other slice is non-empty, the orchestrator reviews those files directly — the reviewer subagent is scoped to backend paths and will not look at them. Run this step in parallel with Step 4 whenever possible.

Scope the Other slice by path prefix:

| Area                                       | What to check                                                                |
|--------------------------------------------|------------------------------------------------------------------------------|
| `docs/`, `*.md`, `README*`, `CLAUDE.md`    | Accuracy vs. the code change, broken links, stale commands, rules drift      |
| `.github/workflows/`, CI configs           | Job graph, permissions, caching, secrets usage, no `--no-verify` bypasses    |
| `compose*.yaml`, `Dockerfile`              | Profile wiring, port/volume correctness, image tags pinned, no secrets       |
| `db/` (root, password file)                | Secrets never committed in plaintext                                         |
| Root configs (`.gitignore`, `.editorconfig`, `pom.xml` non-Java parts) | Lockfile churn justified, ignored paths correct                              |
| `.claude/rules/`, `.claude/agents/`, `.claude/skills/` | Rule/agent/skill changes consistent across files; no contradictions          |

Get the Other-slice diff by running, from the worktree:
```
git -C <worktree-path> diff origin/<baseRefName>...HEAD -- <other-paths>
```
where `<other-paths>` is the space-separated list of top-level paths in the slice.

Apply the same diff-scope rule as the subagent. Apply the **Severity Calibration** from `.claude/rules/implementation-common.md` (or the reviewer's system prompt). Produce findings in the **Review Output Format** below — they will be merged in Step 5 alongside the subagent output.

If the Other slice is empty, skip this step.

## Step 5: Merge findings into a single PR review

After the subagent returns, combine its output **together with your own Other-slice findings from Step 4b** into one consolidated review.

- Preserve every finding from the subagent verbatim, grouped into the sections below. Fold in the Other-slice findings the orchestrator produced in Step 4b at the same severity they were flagged.
- When the subagent (or the Other-slice pass) returns "No issues found.", treat that stream as contributing zero findings (do not invent any).
- Cap **Suggestions** at three entries total across all streams — if more are returned, rank by impact and drop the rest.
- Add a **Summary** paragraph (2–3 sentences) describing what the PR does and your overall assessment, synthesized from the subagent output, the Other-slice review, and the PR metadata.
- Add a **Praise** section highlighting things done well.

Output format (use verbatim):

```
---

## Code Review: PR #<number> — <title>

### Summary
<2-3 sentence summary of what this PR does and your overall assessment>

### Critical (must fix)
<merged Critical findings; "None" if empty>

### Important (should fix)
<merged Important findings; "None" if empty>

### Minor (could fix)
<merged Minor findings; "None" if empty>

### Suggestions (max 3, does not block merge)
<top 3 suggestions; "None" if empty>

### Pre-existing (out of scope)
<merged Pre-existing findings; "None" if empty>

### Praise
<things done well — good patterns, thorough tests, clean code>

---
```

For each finding, preserve the subagent's `path/to/file:line` reference and description. Do not soften, rephrase, or drop findings.

## Step 6: Print implementation target

After the review output, always print the following block (substituting the literal worktree path from Step 2):

```
---
> **Implementation target:** `<worktree-path>`
> All file edits for this PR must be made inside the worktree above.
> Do **not** modify files in the main repository.
```
