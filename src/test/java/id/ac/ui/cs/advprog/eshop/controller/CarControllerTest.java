package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CarController controller = new CarController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static Car car(String id, String name, String color, int qty) {
        Car c = new Car();
        c.setId(id);
        c.setCarName(name);
        c.setCarColor(color);
        c.setCarQuantity(qty);
        return c;
    }

    @Test
    void getCreateCar_returnsView() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateCar"))
                .andExpect(model().attributeExists("car"));
        verifyNoInteractions(service);
    }

    @Test
    void postCreateCar_redirectsList_andCallsCreate() throws Exception {
        mockMvc.perform(post("/car/createCar")
                        .param("id", "c1")
                        .param("carName", "Avanza")
                        .param("carColor", "Black")
                        .param("carQuantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(service).create(any(Car.class));
    }

    @Test
    void getListCar_returnsView() throws Exception {
        when(service.findAll()).thenReturn(List.of(car("c1", "A", "Red", 1)));

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CarList"))
                .andExpect(model().attributeExists("cars"));

        verify(service).findAll();
    }

    @Test
    void getEditCar_returnsView() throws Exception {
        when(service.findById("c1")).thenReturn(car("c1", "A", "Red", 1));

        mockMvc.perform(get("/car/editCar/c1"))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"))
                .andExpect(model().attributeExists("car"));

        verify(service).findById("c1");
    }

    @Test
    void postEditCar_redirectsList_andCallsUpdate() throws Exception {
        mockMvc.perform(post("/car/editCar")
                        .param("id", "c1")
                        .param("carName", "B")
                        .param("carColor", "Blue")
                        .param("carQuantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(service).update(eq("c1"), any(Car.class));
    }

    @Test
    void postDeleteCar_redirectsList_andCallsDeleteById() throws Exception {
        mockMvc.perform(post("/car/deleteCar").param("carId", "c1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(service).deleteById("c1");
    }
}