IntelliJ Tool Windows Actions Plugin
====================================

This IntelliJ plugin adds just one action to IntelliJ:
- **Hide Bottom Tool Windows** (Ctrl+ESC) \
  Hides all bottom tool windows at once.
  
The plugin should work on all IDEA applications, ie. PyCharm, Resharper
etc., but only IntelliJ IDEA has been tested.

This plugin is a fork of the [SimpleActions plugin](https://plugins.jetbrains.com/plugin/207-simpleactions/)
by Etienne Studer. It has exactly the same action, but it also bundles
some other actions, which don't work anymore and produce errors. It's
not maintained anymore.

### Build & test

`./gradlew runIde`

### Publish plugin

`./gradlew publishPlugin`

but you'll need a deploy key, aka "Jetbrains Hub permanent token".
Add file `gradle.properties` to the project's root with content:
```
intellijPublishToken=<your-token>
```
Don't commit this file!

See [Publishing Plugins with Gradle](https://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system/deployment.html#providing-your-hub-permanent-token-to-gradle)
for more informations.

### Features
- Persistent saving of last shown tool windows

### TODO
-[ ] Publish to marketplace
-[ ] Add dark phone for plugin icon
-[x] Use DumbAware interface. (when indices are in background update)
