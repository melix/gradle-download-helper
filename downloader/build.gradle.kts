plugins {
    groovy
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases/")
    }
}

dependencies {
    implementation("org.gradle:gradle-tooling-api:7.2")
    implementation("info.picocli:picocli:4.6.1")
    implementation(project(":model"))
    implementation(project(":model-plugin"))

    testImplementation("org.codehaus.groovy:groovy:3.0.9")
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")

}

application {
    mainClass.set("gradle.helper.App")
}

tasks.test {
    useJUnitPlatform()
}
