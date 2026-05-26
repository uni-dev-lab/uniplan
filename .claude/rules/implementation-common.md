# Implementation Common Rules

Orchestrator + fix-agent shared conventions. Rules that belong to one specific agent (TDD flows, Done Checklist, reviewer checklists, output formats, severity calibration) live in that agent's system prompt — do not duplicate them here. Triage Principles stay here because the orchestrator reads them to audit fix-agent output, and the fix agent applies them when processing reviewer findings.

Read this file once at session start.

## Term Semantics

- **Pre-existing code/changes** — code that was already in `main` when the current feature branch diverged. Code newly added on the feature branch is never pre-existing, even after it has been committed on the branch.

## Triage Principles (fix agent / orchestrator)

Before fixing any reviewer findings:

1. **Read all reviewer outputs** for the current round.
2. **Deduplicate** — multiple findings may be symptoms of one root cause.
3. **Dismiss** findings that are incorrect, already addressed, or represent valid intentional choices. Each dismissal must record:
   - Finding reference (reviewer, round, finding number).
   - Clear rationale with evidence (rules file section, codebase pattern, plan decision).
4. **Prioritize** non-dismissed findings: Critical first; root cause over symptom patches; architectural fixes before surface fixes.
5. If reviewers contradict each other, resolve by referencing rules files and existing codebase patterns.
6. **Suggestions** are notes, not actions — do not fix unless trivial.

**Dismissal guardrail.** If >50% of findings in a round are dismissed, pause and reassess. Either reviewers are miscalibrated or dismissals are too aggressive.

**Abort threshold.** 5+ Critical findings in a single round:
1. Group by root cause. If 3+ share a root cause, fix the root cause.
2. Discard and restart implementation only if criticals are diverse across subsystems.
3. Never discard without analysis.

## Flaky Test Rule

If a test fails intermittently (passes on re-run without code changes), re-run up to 2 times to confirm. If still intermittent, mark it flaky in the final report — do not mask it with retries or relaxed assertions.

## Test Output Hygiene

Every agent that runs tests must use quiet modes by default. Verbose output wastes 50–200K tokens per run.

| Command | Default                  | When to use verbose                  |
|---------|--------------------------|--------------------------------------|
| Maven   | `./mvnw -B verify -q`    | Only for a specific failure trace    |

Never pipe a full test run's output back to the caller agent — return a digest: pass/fail/skip counts + the failure blocks only. Full output is for targeted debugging, not routine verification.

## Stall Detection

If an agent runs 3 full test cycles without progress (same tests failing, same errors), stop and report the blocker. Do not loop indefinitely.

## Bash Tool Usage

- Never use compound Bash commands (`&&`, `;`, `||`). Always run each command as a separate Bash tool call. Reason: in `dontAsk` permission mode, allow-list patterns match the full command string — compound commands break pattern matching and get rejected, blocking autonomous work.
- Never prefix commands with `cd <path> &&` — use `git -C <path>` for git, or run Maven from the correct directory in a separate call.
- Never use shell redirect operators (`>`, `>>`, `|`). Let the Bash tool capture stdout; if the output needs to be saved, use the Write tool with the captured result.
- Never write files outside the project directory.
