package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(10);
        java.util.List<Product> products = new java.util.ArrayList<>();
        products.add(product);
        order = new Order("13652556-012a-4c30-b5d5-f2910185c2ad", products, 1708560000L, "Bambang Sugeni");
    }

    @Test
    void testCreatePaymentWithVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", "SUCCESS", paymentData, order);
        assertEquals("1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(paymentData, payment.getPaymentData());
        assertSame(order, payment.getOrder());
    }

    @Test
    void testCreatePaymentWithBankTransfer() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "12345678");
        Payment payment = new Payment("2", "BANK_TRANSFER", "SUCCESS", paymentData, order);
        assertEquals("2", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(paymentData, payment.getPaymentData());
        assertSame(order, payment.getOrder());
    }

    @Test
    void testSetStatusToRejected() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", "SUCCESS", paymentData, order);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testInvalidStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", "SUCCESS", paymentData, order);
        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("INVALID"));
    }

    @Test
    void testCreatePaymentWithNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("1", "VOUCHER", "SUCCESS", null, order);
        });
    }

    @Test
    void testCreatePaymentWithEmptyPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("1", "VOUCHER", "SUCCESS", new HashMap<>(), order);
        });
    }

    @Test
    void testCreatePaymentWithNullOrder() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("1", "VOUCHER", "SUCCESS", paymentData, null);
        });
    }
}
