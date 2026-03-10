package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = "SUCCESS";

        if (method.equals("VOUCHER")) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode == null || !isValidVoucher(voucherCode)) {
                status = "REJECTED";
            }
        } else if (method.equals("BANK_TRANSFER")) {
            if (!isValidBankTransfer(paymentData)) {
                status = "REJECTED";
            }
        }

        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData, order);
        return paymentRepository.save(payment);
    }

    private boolean isValidBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        return bankName != null && !bankName.isEmpty() && referenceCode != null && !referenceCode.isEmpty();
    }

    private boolean isValidVoucher(String voucherCode) {
        if (voucherCode.length() != 16) return false;
        if (!voucherCode.startsWith("ESHOP")) return false;
        
        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }
        return digitCount == 8;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        if (status.equals("SUCCESS")) {
            payment.getOrder().setStatus("SUCCESS");
        } else if (status.equals("REJECTED")) {
            payment.getOrder().setStatus("FAILED");
        }
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
