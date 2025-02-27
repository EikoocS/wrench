plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("io.freefair.lombok") version "8.12.1"
}

group = "tech.cookiepower"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.mozilla/rhino
    compileOnly("org.mozilla:rhino:1.8.0")
    // https://mvnrepository.com/artifact/org.reflections/reflections
    compileOnly("org.reflections:reflections:0.10.2")

    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.processResources {
    filesMatching("**/paper-plugin.yml") {
        expand("wrench_version" to project.version)
    }
}