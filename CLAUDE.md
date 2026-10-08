# UPB-Game-Swing

A small Swing game library (`edu.upb.lp.game`) with an example game, BugWorld.

## Who uses it
Students only get `upb-game.jar`. They never see or change this repo. They write their own games against the jar, and they learn the library by reading the source files in `core/`, which are bundled in the jar. So:

- Comments in `core/` are the students' documentation. Keep them clear and accurate, and in English.
- Changing a `core/` interface breaks student code. Treat it as an API change and point it out.
- `internal/` is hidden from students in practice. It can change freely as long as `core/` behaves the same.

## Build & run

- `./build.sh` builds `upb-game.jar` (classes + sources + resources). Use Git Bash on Windows.
- `java -jar upb-game.jar` runs BugWorld. The jar also works as a library.

## Java version
The target is **Java 11** (`--release 11` in `build.sh`). Don't use newer language or API features, even if a local IDE is set to a newer version.

## Layout

- `core/`: the public API. Interfaces like `GameController`, `MainLibrary`, `GraphicsLibrary`, `SoundLibrary`, `StorageLibrary`, `TimeLibrary`, `MessagesLibrary`.
- `internal/`: Swing implementations of `core/`. `MainSwingLibrary` is the entry point.
- `bugworld/`: the example game. It depends only on `core/`. Keep games that way.
- `Main.java`: wires a `GameController` to `MainSwingLibrary`.
- `resources/`: images and sounds. Edit assets here.

## Not tracked
`bin/`, `build/`, `upb-game.jar` and Eclipse metadata (`.project`, `.settings/`) are gitignored. Don't edit them or rely on them.
