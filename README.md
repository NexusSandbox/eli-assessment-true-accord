# eli-assessment-true-accord

Elisha Boozer's Assessment for TrueAccord interview

Mock internal administration tool to manage client debts.

## Prerequisites

* Java 15
* Gradle v6.8.x
* JUnit5 Jupiter

## Implementation Process

* Review requirements
    * Break down requirements into discrete chunks
* Outline/sketch out design options
* Create initial project structure and functioning baseline
    * Decide on initial technology choices to fit the problems
    * Decide on any external libraries
* Create initial tests to validate basic workflows
* Implement initial basic workflows
    * Refine workflows with tests
* Create comprehensive testing suite
    * Check for peripheral use-case scenarios (uncommon but valid)
    * Check for edge-case scenarios
    * Check for failure/exception-handling scenarios
    * Check for at least 1 test/requirement
* Implement remaining workflows
    * Confirm against documented requirements
    * Refine workflows with tests
* Review implementation and refactor
    * Clean-up and self-review code
        * Look to simplify and remove obvious complexity
        * Restructure code
    * Use static analysis tools
    * Document all externally exposed classes/methods
    * Document all tests with accurate descriptions of intent, requirement, and expected result
    * Add logging at critical points in logic
* Ensure all tests continue to pass

## [Initial Design Diagram](https://excalidraw.com/#json=4713253091409920,gnH0Pgw1gomnldl30SsccQ)

## Key Functional Logic Locations/Notes

* [eli.assessment.true_accord.Debt]: Core functional-driver for the app, and holds the key logic endpoints
* [eli.assessment.true_accord.App]: Makes the initial call into the external API endpoints, and stitches the
  Debt objects together
* [eli.assessment.true_accord.client.TrueAccordClient]: Manager service that handles the TrueAccord endpoint calls

## Assumptions

* Debts do not consider interest, additional fees, or over/under payments
* Debt manager does not provide any mechanism to update existing data (read-only)
* Scale is currently unknown, and thus may need to account for unbounded lists
    * For simplicity, I am assuming relatively small lists that can be processed in memory given a reasonable period of
      time
* (Question answered by the recruiter) Payments can only be made through a payment plan
    * API does not provide a way to associate a payment to a debt without a plan, so no way to implement requirement

## Execution Instructions

* Note: I created this on a Windows 10 machine, with an IntelliJ IDE 2020.3
  * I would build this as an executable jar, but time constraints...
* Ensure your system has the documented prerequisites installed and configured
* Clone the project to the local system using the Git Clone URL for the project
  * Open a terminal application
  * Navigate to the same location as the root of the project on the system
  * Enter the following command into the terminal:
  > gradle build
* Run project from the same location in the terminal
  > gradle run

## Post-Implementation Thoughts

* Although this was not algorithmically challenging, this I think is more indicative of a real world use-case (even if it was greatly simplified)
  * This was a fun little toy project that touched on a lot of fundamentals and helped me display what I can do
* If I had more time, and this was an actual production implementation I would likely add the following to this project:
  * More robust testing (e.g. Add Mocking; More Integration, and Performance tests, etc.)
  * Sub-divide the client endpoint calls into bound chunks to limit the memory heap size
  * If this was bound to a database, I would instead query for these results and tie them together prior to pulling them into the app memory
