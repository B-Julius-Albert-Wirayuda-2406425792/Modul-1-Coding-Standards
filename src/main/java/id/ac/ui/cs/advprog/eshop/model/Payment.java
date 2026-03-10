package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

/**
 * Represents a payment for an order in the e-shop.
 */
@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, String method, String status, Map<String, String> paymentData, Order order) {
        this.id = id;
        this.method = method;
        this.status = status;
        this.order = order;

        if (paymentData == null || paymentData.isEmpty() || order == null) {
            throw new IllegalArgumentException();
        } else {
            this.paymentData = paymentData;
        }
    }

    public void setStatus(String status) {
        if (status.equals("SUCCESS") || status.equals("REJECTED")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
