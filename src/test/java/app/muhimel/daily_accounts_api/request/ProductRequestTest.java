package app.muhimel.daily_accounts_api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void test_ValidProductRequestHasNoViolations() {
        ProductRequest request = new ProductRequest();
        request.setName("Fish");
        request.setUnit("pcs");

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void test_NullNameIsRejected() {
        ProductRequest request = new ProductRequest();
        request.setName(null);
        request.setUnit("pcs");

        assertViolation(request, "name", "Name is Required");
    }

    @Test
    void test_EmptyNameIsRejected() {
        ProductRequest request = new ProductRequest();
        request.setName("");
        request.setUnit("pcs");

        assertViolation(request, "name", "Name is Required");
    }

    @Test
    void test_NullUnitIsRejected() {
        ProductRequest request = new ProductRequest();
        request.setName("Fish");
        request.setUnit(null);

        assertViolation(request, "unit", "unit is Required");
    }

    @Test
    void test_EmptyUnitIsRejected() {
        ProductRequest request = new ProductRequest();
        request.setName("Fish");
        request.setUnit("");

        assertViolation(request, "unit", "unit is Required");
    }

    private void assertViolation(ProductRequest request, String field, String message) {
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anySatisfy(violation -> {
                    assertThat(violation.getPropertyPath().toString()).isEqualTo(field);
                    assertThat(violation.getMessage()).isEqualTo(message);
                });
    }
}
