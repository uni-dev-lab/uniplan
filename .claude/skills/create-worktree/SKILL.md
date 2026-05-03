---
name: create-worktree
description: Create (or reuse) a git worktree for a given remote branch in a sibling uniplan-worktree directory
disable-model-invocation: false
argument-hint: <branch-name>
---

You are creating (or reusing) a git worktree for the branch: $ARGUMENTS

Follow these steps exactly. Run each step as a **separate Bash call** — do NOT combine them with `&&` or shell variables.

## Step 1: Get repo root

Run `git rev-parse --show-toplevel` and capture the output as the literal repo root path (e.g. `/Users/stoyan/projects/uni-dev-lab/uniplan`).

## Step 2: Derive paths

Using the literal repo root from Step 1:

- **Worktree base**: `<repo-root>/../uniplan-worktree`
- **Normalized branch name**: replace every `/` with `-` in `$ARGUMENTS` (e.g. `feature/UNI-1234` → `feature-UNI-1234`)
- **Full worktree path**: `<worktree-base>/<normalized-branch>`

## Step 3: Create worktree base directory

Run `mkdir -p <repo-root>/../uniplan-worktree` using the literal repo root from Step 1.

## Step 4: Fetch branch

Run `git fetch origin $ARGUMENTS`.

## Step 5: Check for existing worktree

Run `git worktree list` and inspect the output to determine whether a worktree for this branch already exists.

## Step 6: Add worktree (if not already present)

- If no worktree exists for this branch, run: `git worktree add <worktree-path> $ARGUMENTS`
- If that fails because the branch is already checked out locally, run: `git worktree add <worktree-path> --detach origin/$ARGUMENTS`
- If a worktree already exists (detected in Step 5), skip this step and report "reused".

## Step 7: Print result

Print the absolute worktree path and state whether it was **created** or **reused**.

## Notes

<<<<<<< HEAD
uniplan does not currently have a `scripts/post_checkout.sh`. When that exists, this skill should invoke it after Step 7 for newly created worktrees only.
=======
uniplan does not currently have a `scripts/post_checkout.sh` (eJourney does). When that exists, this skill should invoke it after Step 7 for newly created worktrees only — see eJourney's `create-worktree/SKILL.md` for the reference shape.
>>>>>>> 033a30e (Add Claude Code config, Facade-pattern rules, and PR review workflow)
