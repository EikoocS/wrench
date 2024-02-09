import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins{
    id("com.github.johnrengelman.shadow")
}

dependencies{
    implementation(project(":wrench-core"))
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${rootProject.name}-${project.name}-${project.version}.jar")
    archiveBaseName.set("${rootProject.name}-${project.name}-${project.version}")
    archiveVersion.set("${project.version}")
}

tasks.build{
    dependsOn("shadowJar")
}