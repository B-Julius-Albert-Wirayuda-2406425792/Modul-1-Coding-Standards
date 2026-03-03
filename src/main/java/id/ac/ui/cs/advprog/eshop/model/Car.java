package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Car implements IdHolder {
    private String id;
    private String carName;
    private String carColor;
    private int carQuantity;
}
