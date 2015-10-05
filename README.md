# Gradle Website Optimizer
A gradle plugin to easily optimize HTML websites and contained CSS and JS files.

## Version

Current Version: `0.1`

## Dependency

### Maven Central

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "at.tripwire.gradle.wo:website-optimizer:<version>"
    }
}

apply-plugin: "at.tripwire.website-optimizer"
```

### Gradle Plugin

Gradle Plugin page: https://plugins.gradle.org/plugin/at.tripwire.website-optimizer

```groovy
plugins {
	id "at.tripwire.website-optimizer" version "<version>"
}
```

## Usage
