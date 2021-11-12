plugins {
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases/")
    }
}

dependencies {
    implementation(project(":downloader"))
}

application {
    mainClass.set("io.micronaut.helper.Seeder")
}

tasks.named<JavaExec>("run") {
    outputs.upToDateWhen { false }
}
