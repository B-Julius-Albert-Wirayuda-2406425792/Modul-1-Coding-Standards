package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarServiceImpl service;

    @Test
    void create_delegates() {
        Car c = new Car(); c.setId("c1");
        when(carRepository.create(c)).thenReturn(c);

        Car result = service.create(c);

        assertSame(c, result);
        verify(carRepository).create(c);
    }

    @Test
    void findAll_collectsIterator() {
        Car c1 = new Car(); c1.setId("1");
        Car c2 = new Car(); c2.setId("2");
        Iterator<Car> it = Arrays.asList(c1, c2).iterator();
        when(carRepository.findAll()).thenReturn(it);

        List<Car> result = service.findAll();

        assertEquals(2, result.size());
        verify(carRepository).findAll();
    }

    @Test
    void findById_delegates() {
        Car c = new Car(); c.setId("c1");
        when(carRepository.findById("c1")).thenReturn(c);

        Car result = service.findById("c1");

        assertSame(c, result);
        verify(carRepository).findById("c1");
    }

    @Test
    void update_delegates() {
        Car c = new Car(); c.setId("c1");
        service.update("c1", c);
        verify(carRepository).update("c1", c);
    }

    @Test
    void deleteById_delegates() {
        service.deleteById("c1");
        verify(carRepository).delete("c1");
    }
}