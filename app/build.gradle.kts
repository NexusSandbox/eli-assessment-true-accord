plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

val jacksonVersion = "2.12.2"
val googleHttpClientVersion = "1.39.1"

dependencies {
    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // General utility library
    implementation("com.google.guava:guava:30.1.1-jre")
    // HTTP Client
    implementation("com.google.http-client:google-http-client:${googleHttpClientVersion}")
    implementation("com.google.http-client:google-http-client-jackson2:${googleHttpClientVersion}")
    // JSON interpreter
    implementation("com.fasterxml.jackson.core:jackson-core:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
}

application {
    // Define the main class for the application.
    mainClass.set("eli.assessment.true_accord.App")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}
