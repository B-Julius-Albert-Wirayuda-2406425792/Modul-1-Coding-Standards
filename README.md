# eshop

Julius Albert Wirayuda \
2406425792 \
ADPRO B

## Reflection 1

---

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
