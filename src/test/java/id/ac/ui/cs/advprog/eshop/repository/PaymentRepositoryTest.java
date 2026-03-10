package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private List<Payment> payments;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();
        
        Product product = new Product();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(10);
        List<Product> products = new ArrayList<>();
        products.add(product);
        order = new Order("13652556-012a-4c30-b5d5-f2910185c2ad", products, 1708560000L, "Bambang Sugeni");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("1", "VOUCHER", "SUCCESS", paymentData, order);
        payments.add(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "12345678");
        Payment payment2 = new Payment("2", "BANK_TRANSFER", "SUCCESS", paymentData2, order);
        payments.add(payment2);
    }

    @Test
    void testSave() {
        Payment payment = payments.get(0);
        Payment result = paymentRepository.save(payment);
        assertEquals(payment, result);
        Payment foundPayment = paymentRepository.findById(payment.getId());
        assertEquals(payment, foundPayment);
    }

    @Test
    void testFindById() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }
        Payment foundPayment = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1), foundPayment);
    }

    @Test
    void testFindByIdNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }
        Payment foundPayment = paymentRepository.findById("non-existent-id");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }
        List<Payment> foundPayments = paymentRepository.findAll();
        assertEquals(payments.size(), foundPayments.size());
        assertTrue(foundPayments.containsAll(payments));
    }
}
