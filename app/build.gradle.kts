plugins {
    id("trees.kotlin-application-conventions")
    id("org.jetbrains.compose") version "1.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val sqliteJdbcVersion: String by project

dependencies {
    implementation(project(":lib"))
    implementation("org.neo4j.driver:neo4j-java-driver:5.7.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)

    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3-desktop:1.2.1")
}

application {
    mainClass.set("app.MainKt")
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}
