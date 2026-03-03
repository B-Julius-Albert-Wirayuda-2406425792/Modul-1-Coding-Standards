package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @InjectMocks
    CarRepository carRepository;

    @Test
    void update_success_updatesFields() {
        Car c = new Car();
        c.setId("c1");
        c.setCarName("Old");
        c.setCarColor("Black");
        c.setCarQuantity(1);
        carRepository.create(c);

        Car updated = new Car();
        updated.setId("c1");
        updated.setCarName("New");
        updated.setCarColor("Red");
        updated.setCarQuantity(2);

        Car result = carRepository.update("c1", updated);

        assertNotNull(result);
        assertEquals("New", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(2, result.getCarQuantity());
    }

    @Test
    void update_notFound_returnsNull() {
        Car updated = new Car();
        updated.setId("missing");
        updated.setCarName("X");

        Car result = carRepository.update("missing", updated);

        assertNull(result);
    }

    @Test
    void inheritedCrud_create_find_delete_work() {
        Car c = new Car();
        c.setId("c1");
        c.setCarName("A");
        carRepository.create(c);

        assertEquals("A", carRepository.findById("c1").getCarName());

        carRepository.delete("c1");
        Iterator<Car> it = carRepository.findAll();
        assertFalse(it.hasNext());
    }
}