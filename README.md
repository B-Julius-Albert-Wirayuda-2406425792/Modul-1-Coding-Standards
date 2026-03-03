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

## Reflection 3 — Applying SOLID Principles in the E-Shop Project

In this project, I tried to apply **SOLID** so the codebase is easier to maintain as features grow.

### 1) SOLID principles applied in this project

#### **S — Single Responsibility Principle (SRP)**
I separate responsibilities by feature/domain:
- `ProductService` handles product logic only.
- `OrderService` handles order flow only.
- `PaymentService` handles payment processing only.

So, each class has one clear reason to change.

#### **O — Open/Closed Principle (OCP)**
I design payment and shipping features to be extended without editing core checkout logic.
- Example: adding a new payment type (e.g., bank transfer) by creating a new implementation, not rewriting `CheckoutService`.

#### **L — Liskov Substitution Principle (LSP)**
Implementations can replace their parent abstraction safely.
- Example: any class implementing `PaymentMethod` can be used by checkout without breaking behavior.

#### **I — Interface Segregation Principle (ISP)**
I avoid “fat” interfaces by splitting contracts by use case.
- Example: admin product management and customer product browsing use different interfaces, so classes only implement what they need.

#### **D — Dependency Inversion Principle (DIP)**
High-level modules depend on abstractions, not concrete classes.
- Example: `CheckoutService` depends on `IPaymentGateway` and `IOrderRepository`, so infrastructure details can be swapped easily.

---

### 2) Advantages of applying SOLID (with examples)

1. **Code is easier to maintain**
    - If tax calculation changes, I update tax-related logic only, not the whole checkout flow.

2. **Features are easier to extend**
    - New payment gateway can be added as a new class implementing `IPaymentGateway`.

3. **Testing becomes simpler**
    - Services that depend on interfaces can use mocks in unit tests.
    - Example: test `CheckoutService` with a fake payment gateway, without real external API calls.

4. **Lower risk when refactoring**
    - Since responsibilities are separated, changing one part is less likely to break unrelated parts.

5. **Better teamwork**
    - Different team members can work on catalog, order, and payment modules in parallel with fewer merge conflicts.

---

### 3) Disadvantages of NOT applying SOLID (with examples)

1. **Tightly coupled code**
    - If checkout directly creates a specific payment class, replacing that provider requires editing many files.

2. **Large “god classes”**
    - One class handling product, order, payment, and notification becomes hard to read and debug.

3. **Hard to test**
    - Without abstractions, logic is mixed with database/API calls, so unit tests become slow and brittle.

4. **Frequent side effects**
    - Small changes can break unrelated features because everything is connected in one place.

5. **Slower development over time**
    - At first it may feel faster, but as the project grows, every new feature needs risky modifications to old code.

---

### Conclusion

Applying SOLID in this e-shop project helps me build a cleaner architecture that is easier to scale, test, and maintain.  
Without SOLID, development may look faster in the short term, but technical debt increases and slows down future iterations.
