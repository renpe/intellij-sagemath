# IntelliJ SageMath

IntelliJ IDEA plugin for the [SageMath](https://www.sagemath.org/) computer
algebra system. Sister project to
[intellij-magma](https://github.com/renpe/intellij-magma).

> **Disclaimer:** This is an unofficial, community-maintained plugin and is
> **not affiliated with, endorsed by, or developed by** the SageMath project.
> *SageMath* is a trademark of its respective owners.

## Features

- Syntax highlighting for `.sage`, `.sagews`, `.spyx` and `.spx` files. Sage is
  a Python superset, so the lexer also recognises Sage-only constructs:
  - `R.<x, y> = PolynomialRing(QQ)` generator declarations
  - `^` as a power operator (the Sage preparser rewrites it to `**`)
  - REPL magics at the top of a line (`%time`, `%timeit`, `%display`, …)
- Brace matching for `()`, `[]`, `{}`
- Line (`#`) and triple-quoted (`""" """`) comments
- Live templates for the most common Sage constructs: `def`, `cls`, `for`,
  `if`, `ife`, `while`, `try`, `from`, `pr`, `as`, `poly`, `nf`, `gf`, `ec`,
  `mat`, `vec`, `var`, `solv`, `ig`, `dr`, `lim`, `pl`, `fa`, `ip`, `main`
- Dedicated color scheme entries under
  *Settings → Editor → Color Scheme → SageMath*
- Run Sage scripts from the editor — gutter play icon, *Run 'script.sage'* in
  the context menu, and a dedicated **SageMath** run configuration type
- Recognises hundreds of Sage built-ins (`Integer`, `Matrix`, `EllipticCurve`,
  `factor`, `plot`, …) out of the box, regenerable from your local Sage install
  via `scripts/generate_builtins.py`

## Running Sage scripts

1. Open *Settings → Tools → SageMath* and set the path to the `sage` binary.
2. Open any `.sage` / `.spyx` file and click the green ▶ in the gutter, or
   right-click the file → *Run 'script.sage'*.
3. The first run creates a **SageMath** run configuration that you can refine
   via *Run → Edit Configurations…* (script arguments, working directory,
   interpreter override, WSL distribution, `--python` mode, `--preparse` only).

### WSL (Windows)

If `sage` lives inside a WSL distribution, set the global SageMath settings
(or the run-configuration-level override) to the **WSL-side** interpreter
path, e.g. `/usr/bin/sage`, and pick the distribution from the dropdown.
Windows script paths (e.g. `C:\…\script.sage`) are translated to `/mnt/c/…`
automatically and the process is launched via the WSL platform integration.

## Regenerating the built-in name list

The bundled `SagemathBuiltins.kt` and `SagemathTypes.kt` lists are a curated
baseline (a few hundred names). For full coverage against your Sage version,
run the generator from a shell where `sage` is on `PATH`:

```bash
sage -python scripts/generate_builtins.py
```

This walks `dir(sage.all)`, classifies every public name as a class/type or a
callable/value, and rewrites both Kotlin files. Commit the result if you want
the updated lists baked into the plugin.

## Build

### Plugin ZIP for installation

```bash
./gradlew buildPlugin
```

The plugin ZIP is written to:

```
build/distributions/intellij-sagemath-<version>.zip
```

Note: plain `./gradlew build` only compiles classes and runs tests — it does
**not** produce the ZIP. Use `buildPlugin` (or `./gradlew build buildPlugin`)
to get the installable archive.

### Run in a sandbox IDE

```bash
./gradlew runIde
```

Starts an isolated IntelliJ IDEA instance with the plugin pre-installed for
quick testing.

## Install

In your real IntelliJ IDEA: *Settings → Plugins → ⚙ → Install Plugin from
Disk…* and pick the ZIP from `build/distributions/`.

## Requirements

- JDK 21
- IntelliJ IDEA 2025.1 or newer (`pluginSinceBuild = 251`)

The Gradle wrapper is included; you do not need to install Gradle separately.

## License

Plugin source code: Apache License 2.0 — Copyright (c) 2026 René Peschmann.
See [LICENSE](LICENSE).

Bundled icons (`src/main/resources/icons/sagemath.png`,
`sagemath@2x.png`, `META-INF/pluginIcon.svg`) are derived from the
official SageMath organisation avatar — © the SageMath project,
[CC-BY-SA-4.0](https://creativecommons.org/licenses/by-sa/4.0/). See
[NOTICE](NOTICE) for full attribution.
