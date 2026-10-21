# Nightly Regression Demo

Java 17 · Maven · Cucumber (JUnit Platform) · Rest Assured · Selenium 4 · masterthought Cucumber HTML reports · GitHub Actions

## Layout
- `src/test/resources/features/<MALCODE>/` - one folder per malcode; every feature is tagged `@<MALCODE>`
- `steps/ApiSteps` (generic REST steps), `steps/UiSteps` (Selenium), `hooks/Hooks` (browser start/stop, failure screenshots)
- `scripts/summarize.sh` - turns `cucumber.json` into pass/fail counts for the email
- `.github/workflows/nightly-regression.yml` - nightly pipeline, one parallel job + one email per malcode

## How environments work
- `src/test/resources/tcoe.properties` has one field: `env=MAL001_SIT`
- The framework loads `environment_<env>.properties` (here `environment_MAL001_SIT.properties`), which holds that malcode's properties for that environment (`base.url`, ...)
- Features are selected by tag; locally the tag is the `cucumber.filter.tags` line in `src/test/resources/junit-platform.properties`

## Run locally (e.g. MAL002 against PAT)
1. In `junit-platform.properties` set `cucumber.filter.tags=@MAL002`
2. In `tcoe.properties` set `env=MAL002_PAT`
3. Run `mvn verify -Dmaven.test.failure.ignore=true` (or run `RunCucumberTest` from your IDE)
Report: `target/cucumber-html-reports/local/overview-features.html`
Add `-Dheadless=false` to watch the browser for UI malcodes.

Helper: `bash scripts/set-env.sh MAL002 PAT` does step 2 for you.

## Pipeline
The workflow matrix is malcode x environment (6 jobs). Each job runs `scripts/set-env.sh` to overwrite the `env` field,
then runs Maven with `-Dcucumber.filter.tags=@MALxxx`, so each night produces one report and one email per pair.

## Adding a new malcode
1. Create `src/test/resources/features/MAL004/` with features tagged `@MAL004`
2. Create `environment_MAL004_SIT.properties` and `environment_MAL004_PAT.properties`
3. Add `MAL004` to the `malcode` list in the workflow matrix (and a `MAL004_RECIPIENTS` variable)

## GitHub setup
**Settings > Secrets and variables > Actions**

Secrets: `SMTP_SERVER`, `SMTP_PORT`, `SMTP_USERNAME`, `SMTP_PASSWORD`, `MAIL_FROM`

Variables (one recipient list per malcode, used for both SIT and PAT): `MAL001_RECIPIENTS`, `MAL002_RECIPIENTS`, `MAL003_RECIPIENTS` (comma-separated emails), `DEFAULT_RECIPIENTS` (fallback)

Outlook values (personal account): server `smtp-mail.outlook.com`, port `587`, username = your address,
password = an **app password** (requires 2FA on the account).

## Schedule
GitHub cron is UTC-only with no DST. Two cron entries (03:00 and 04:00 UTC) plus a `gate` job that checks
`TZ=America/New_York` hour == 23 make it fire at 11pm Eastern year-round. Scheduled workflows only run from the default branch.
Use **Actions > Nightly Regression > Run workflow** to trigger manually.
