plugins {
    id("trees.kotlin-application-conventions")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val sqliteJdbcVersion: String by project

dependencies {
    implementation(project(":lib"))
    implementation("org.neo4j.driver:neo4j-java-driver:5.7.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.xerial", "sqlite-jdbc", sqliteJdbcVersion)
}
//
//kotlin {
//    jvmToolchain(8)
//}

application {
    mainClass.set("MainKt")
}