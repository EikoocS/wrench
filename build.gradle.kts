plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("io.freefair.lombok") version "8.4" apply false
}

group = "tech.cookiepower"
version = "1.0-SNAPSHOT"

allprojects {
    group = "tech.cookiepower.wrench"
    version = "${rootProject.version}"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.freefair.lombok")

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation("org.jetbrains:annotations:24.0.0")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

