plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

val jacksonVersion = "2.12.2"
val floggerVersion = "0.5.1"

dependencies {
    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // General utility library
    implementation("com.google.guava:guava:30.1.1-jre")
    // JSON interpreter
    implementation("com.fasterxml.jackson.core:jackson-core:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${jacksonVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    // Logger Framework
    implementation("com.google.flogger:flogger:$floggerVersion")
    implementation("com.google.flogger:flogger-system-backend:$floggerVersion")
    implementation("com.google.flogger:flogger-log4j2-backend:$floggerVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("eli.assessment.true_accord.App")
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()

    systemProperty("flogger.backend_factory", "com.google.common.flogger.backend.log4j2.Log4j2BackendFactory#getInstance")
    testLogging {
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("FAILED", "SKIPPED", "PASSED")
        showStandardStreams = true
    }
    addTestListener(object : TestListener {
        override fun beforeSuite(descriptor: TestDescriptor?) {
            println("Initiating tests for: $descriptor")
        }

        override fun beforeTest(descriptor: TestDescriptor?) {
            println("Initiating test: $descriptor")
        }

        override fun afterTest(descriptor: TestDescriptor?, result: TestResult?) {
            println("Completed test: $descriptor")
        }

        override fun afterSuite(descriptor: TestDescriptor?, result: TestResult?) {
            println("Completed tests for: $descriptor")
            if (result != null) {
                println(
                        """Test results: ${result.resultType}
                                |  Test Count: ${result.testCount}
                                |  Succeeded:  ${result.successfulTestCount}
                                |  Failed:     ${result.failedTestCount}
                                |  Skipped:    ${result.skippedTestCount}
                            """.trimMargin()
                )
            }
        }
    })
}
