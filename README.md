# Gradle Website Optimizer
A gradle plugin to easily optimize HTML websites and contained CSS and JS files.


## Usage

### Maven Central

```

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "at.tripwire.gradle.wo:website-optimizer:VERSION"
    }
}

apply-plugin: "at.tripwire.website-optimizer"

```

### Gradle Plugin

```
plugins {
	id "at.tripwire.website-optimizer" version "VERSION"
}

```
