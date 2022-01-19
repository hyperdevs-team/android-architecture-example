## Detekt
_Embrace the dark side of coding, we have cookies :)_

### Introduction
[detekt](https://github.com/detekt/detekt) is a pretty handy tool to check for code style errors and to statically analyze Kotlin code.
It operates on the abstract syntax tree provided by the Kotlin compiler.

### Configuration
#### At commit time
The hooks in [hooks.md](hooks.md) are automatically installed when building the project.
If you want to run it manually (though it won't know about git changes), run `./gradlew detekt`.