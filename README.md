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

## [Initial Design Diagram](https://excalidraw.com/#json=6197044154728448,MAskeqL7Z6SHRkt9LsDu6A)

## Assumptions

* Debts do not consider interest, additional fees, or over/under payments
* Debt manager does not provide any mechanism to update existing data (read-only)
* Scale is currently unknown, and thus may need to account for unbounded lists
    * For simplicity, I am assuming relatively small lists that can be processed in memory given a reasonable period of
      time
* (Pending question) Payments can only be made through a payment plan
    * API does not provide a way to associate a payment to a debt without a plan
    * Unfortunately, I was unable to get a response from the recruiter on this question

## Execution Instructions

* Note: I created this on a Windows 10 machine, with an IntelliJ IDE 2020.3
