# eshop

Julius Albert Wirayuda \
2406425792 \
ADPRO B

## Reflection 1

After re-evaluating my source code and the coding standards that I've learned, here is my analysis of my current implementation and improvement recommendations.

### Implementation

1. **Meaningful Names** \
   Variable and method clearly describe the code, making the code self-documenting.
2. **Single Responsibility Principle (SRP)** \
   The logic is separated into different layers.
   - `ProductController` handles HTTP requests and navigation logic.
   - `ProductService` handles the business logic.
   - `ProductRepository` handles data persistence.
3. **Don't Repeat Yourself** \
   Methods, such as `findById`, can be reused instead of rewriting the logic each time it's needed.
4. **ID Generation (UUID)** \
   The use of random UUID ensures that identifiers are globally unique and unpredictable, preventing attackers from guessing IDs to access unauthorized data.
5. **Exception Handling** \
   The repository throws `IllegalArgumentException` when a product is not found, preventing the application from proceeding with `null` objects and potentially leaking system details via a `NullPointerException`.

### Mistakes and Improvements

1. **Controller Mapping Incosistency** \
    The `delete` feature uses GET mapping instead of POST. "Delete" operation modifies state and should ideally use `@PostMapping` or `@DeleteMapping` to follow RESTful principles and prevent accidental deletions from search engine crawlers or browser pre-fetching.

2. **No Validation for User Input** \
    There is no validation for user input before saving to the repository. The use of Spring's `@Valid` and Bean Validation (JSR 303) is recommended in the controller to ensure data integrity before it reaches the service layer.

## Reflection 2

### Unit Testing
After writing the unit test, I feel more confident in the robustness of the code. Unit tests act as a safety net, ensuring that the core logic works as intended and preventing future changes from introducing regressions.

**How many unit tests should be made in a class?** \
There is no fixed number of unit tests required for a class. The number depends on the complexity of the methods and the variety of possible inputs. Ideally, you should have enough tests to cover:
*   **Positive Scenarios**: Verifying correct behavior with valid input.
*   **Negative Scenarios**: Verifying error handling with invalid input (e.g., nonexistent IDs).
*   **Boundary Cases**: Testing edge values (e.g., empty lists, maximum values).
    Every distinct path through the code (branches of if-statements, loops) should ideally be covered by at least one test.

**Code Coverage** \
Having 100% code coverage does **not** guarantee that the code is free of bugs or errors.
*   **Logic Errors**: Code can be fully covered but still implement the wrong business logic.
*   **Missing Scenarios**: Coverage tools check executed lines, but they cannot detect missing requirements or scenarios that were never implemented in the code.
*   **Integration Issues**: Unit tests test components in isolation; they do not verify that different components interact correctly.

### Functional Test Cleanliness
If I were to create a new functional test suite (e.g., `CountProductFunctionalTest.java`) by copying the setup code from `CreateProductFunctionalTest.java`, it would negatively affect code cleanliness.

**Potential Clean Code Issue: Code Duplication** \
Copying the instance variables (`serverPort`, `testBaseUrl`, `baseUrl`) and the `@BeforeEach` setup method into a new class violates the **DRY (Don't Repeat Yourself)** principle.

**Reasons:**
1.  **Maintainability**: If the setup logic needs to change (e.g., changing how the base URL is constructed), you would need to update the code in every single test class. This increases the risk of inconsistencies.
2.  **Readability**: The test class becomes cluttered with boilerplate configuration code, distracting from the actual test logic.

**Suggested Improvement** \
To make the code cleaner, I suggest creating a **Base Test Class** (e.g., `BaseFunctionalTest`) that encapsulates the common setup logic.

## Reflection Module 02

### Code Quality Issues and Fix Strategy

**Issue A: Sonarqube couldn't detect test coverage (JaCoCo XML report missing**
- **What I fixed**: I configured JaCoCo to generate an XML coverage report so SonarQube can read it (in tasks.jacocoTestReport inside build.gradle.kts).
- **My strategy**: Make quality signals automatic and machine-readable. I kept coverage generation inside the Gradle lifecycle (tests finalize into the report task), so coverage is always produced consistently in CI.

**Issue B: Dependencies weren’t verified (supply-chain risk)**
- **What I fixed**: I added Gradle Dependency Verification metadata to validate dependency integrity via checksums/keys in verification-metadata.xml (supported by the keyring files like verification-keyring.gpg / verification-keyring.keys).
- **My strategy**: Reduce “works on my machine” and security uncertainty by making dependency resolution fail fast if artifacts change unexpectedly.

### CI/CD Workflows: Does this meet Continuous Integration and Continuous Deployment?

In my opinion, this project meets Continuous Integration (CI) because GitHub Actions automatically runs unit tests on every push and pull request using ci.yml. It also runs build and analysis (SonarQube) on pull requests and pushes to master via build.yml, which helps ensure new code is validated before/when it gets merged.

I also consider this project meets Continuous Deployment (CD), even though the deployment isn’t implemented inside GitHub Actions. My deployment is configured directly in Koyeb to automatically build and deploy from the repository whenever there is a push/PR update to master, and it uses the container build defined in Dockerfile. So the deployment step is still fully automated and happens continuously after changes land in master, just managed by Koyeb rather than a GitHub workflow.

## Reflection 3

In this module, I have applied the SOLID principles to improve the maintainability, scalability, and readability of the e-shop codebase. Below is an explanation of the principles applied and the impact they have on the project.

---

### 1) SOLID Principles Applied

#### **Single Responsibility Principle (SRP)**
SRP states that a class should have only one reason to change. I have implemented this by separating the controllers for `Product` and `Car`. Initially, they might have been mixed, but now:
- `ProductController` only handles HTTP requests related to `Product`.
- `CarController` only handles HTTP requests related to `Car`.
- Business logic is delegated to their respective service layers (`ProductService`, `CarService`), and data persistence is handled by the repositories.

#### **Open/Closed Principle (OCP)**
OCP states that software entities should be open for extension but closed for modification. I applied this by creating a `BaseRepository<T>` and `IdHolder` interface.
- If I want to add a new model (e.g., `Category`), I don't need to modify the core logic of how repositories save or find items. I can simply create a `CategoryRepository` that extends `BaseRepository<Category>`. The base behavior is reused and extended through inheritance without modifying the base class code.

#### **Liskov Substitution Principle (LSP)**
LSP states that objects of a superclass should be replaceable with objects of its subclasses without affecting the correctness of the program.
- In my implementation, `ProductRepository` and `CarRepository` both extend `BaseRepository`. They override the `update` method because the specific fields to update (like `productName` vs `carColor`) differ. However, they maintain the contract of the base class. Any part of the system expecting a `BaseRepository` can work with either subclass correctly because they both follow the expected behavior of managing entities with IDs.

#### **Interface Segregation Principle (ISP)**
ISP recommends breaking down large interfaces into smaller, more specific ones.
- I have split the service interfaces into `ReadService<T>` and `WriteService<T>`.
- Instead of one massive `Service` interface, `ProductService` and `CarService` inherit from these smaller interfaces. This allows potential future clients that only need to read data (like a dashboard) to depend only on `ReadService` without being forced to know about creation or deletion methods.

#### **Dependency Inversion Principle (DIP)**
DIP recommends that high-level modules should not depend on low-level modules; both should depend on abstractions.
- My controllers (e.g., `ProductController`) depend on the interface `ProductService`, not the concrete implementation `ProductServiceImpl`.
- This makes the code more flexible. For instance, if I decide to change how products are managed (e.g., using an external API instead of a local repository), I can create a new implementation of `ProductService` and swap it in the Spring configuration without changing the controller's code.

---

### 2) Advantages of Applying SOLID Principles

1.  **Easier Maintenance and Readability**: By following SRP, classes are smaller and focused. For example, if there's a bug in how cars are updated, I know exactly that the issue is in `CarRepository` or `CarService`, and I won't accidentally break Product logic.
2.  **Scalability**: Thanks to OCP and DIP, adding new features is easy. If I want to add a `Customer` module, I can follow the existing pattern of `BaseRepository` and `BaseService` interfaces without touching the existing `Product` or `Car` code.
3.  **Better Testability**: Because of DIP, I can easily mock the `ProductService` when writing unit tests for `ProductController`. I don't need a real database or repository to test the navigation logic in the controller.
4.  **Reduced Side Effects**: With LSP, I can be confident that substituting a subclass won't break the system. This makes refactoring much safer.

---

### 3) Disadvantages of NOT Applying SOLID Principles

1.  **Rigid Code (Tightly Coupled)**: Without DIP, if `ProductController` was directly instantiated with `ProductServiceImpl`, changing the service implementation would require modifying the controller. In a large project, this leads to a "ripple effect" where one change breaks many files.
2.  **God Classes**: Without SRP, we might end up with one `EshopController` handling Products, Cars, Users, and Orders. This file would become thousands of lines long, making it extremely difficult for a team to work on simultaneously due to merge conflicts.
3.  **Code Duplication**: Without OCP and inheritance (like `BaseRepository`), I would have to copy-paste the `create`, `delete`, and `findById` logic into every single repository. If I found a bug in the `delete` logic, I would have to fix it in five different places.
4.  **Fragility**: Without ISP, a client that only needs to list products would still be affected if I changed the signature of the `delete` method, even though that client never uses it. This leads to unnecessary recompilations and potential bugs.

## Reflection 4

Based on Percival's (2017) principles for evaluating testing objectives, I believe the Test-Driven Development (TDD) flow I followed is useful but could be further optimized.

1.  **Is the TDD flow useful?** Yes, it is. By writing tests before implementation, I was forced to think clearly about the requirements and the interface of my methods (like `update` and `delete`) before writing the actual code. This prevented over-engineering and ensured that every line of code was justified by a requirement.
2.  **What to do next time?** To improve my TDD flow, I need to focus more on the **Refactor** phase of the Red-Green-Refactor cycle. While I successfully reached the "Green" state, I sometimes moved on to the next feature too quickly without thoroughly checking if the code could be made cleaner or more efficient. Next time, I will dedicate more time to identifying code smells immediately after a test passes, rather than waiting for a separate refactoring session.

After reviewing my unit tests (such as `ProductRepositoryTest`), I have evaluated them against the **F.I.R.S.T.** principle:

*   **Fast**: The tests run very quickly because they only test logic in memory and use Mockito to isolate dependencies.
*   **Independent**: Each test case (e.g., `testEditProduct` and `testDeleteProduct`) is independent. They do not rely on the state left by previous tests because I use a fresh `ProductRepository` instance for each test.
*   **Repeatable**: The tests are deterministic. Whether I run them locally or in the CI pipeline, the results are consistent because they don't depend on external factors like a real database or network.
*   **Self-Validating**: Each test has clear assertions (`assertEquals`, `assertTrue`, `assertThrows`) that automatically determine if the test passed or failed without requiring manual inspection of logs.
*   **Timely**: In the TDD workflow, the tests were written just before the production code was implemented, satisfying the "Timely" aspect.

Overall, I believe my tests have successfully followed the F.I.R.S.T. principle. For future tests, I will continue to maintain this standard, specifically ensuring that even as the project grows and becomes more complex, the "Independent" and "Fast" qualities are not compromised by shared state or heavy external dependencies.