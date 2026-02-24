package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService service;

    @BeforeEach
    void setUp() {
        ProductController controller = new ProductController();
        ReflectionTestUtils.setField(controller, "service", service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    private static Product product(String id, String name, int qty) {
        Product p = new Product();
        p.setProductId(id);
        p.setProductName(name);
        p.setProductQuantity(qty);
        return p;
    }

    @Test
    void getCreate_returnsCreateView_andHasProductModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));

        verifyNoInteractions(service);
    }

    @Test
    void postCreate_redirectsToList_andCallsServiceCreate() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productId", "id-1")
                        .param("productName", "N")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).create(any(Product.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getList_returnsListView_andAddsProducts() throws Exception {
        when(service.findAll()).thenReturn(List.of(product("id-1", "A", 1)));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEdit_success_returnsEditView_andAddsProduct() throws Exception {
        when(service.findById("id-1")).thenReturn(product("id-1", "A", 1));

        mockMvc.perform(get("/product/edit").param("id", "id-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));

        verify(service, times(1)).findById("id-1");
        verifyNoMoreInteractions(service);
    }

    @Test
    void getEdit_notFound_redirectsToList() throws Exception {
        when(service.findById("missing")).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/product/edit").param("id", "missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).findById("missing");
        verifyNoMoreInteractions(service);
    }

    @Test
    void postEdit_redirectsToList_andCallsUpdate() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "id-1")
                        .param("productName", "B")
                        .param("productQuantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).update(any(Product.class));
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDelete_success_redirectsToProductList_andCallsDelete() throws Exception {
        Product p = product("id-1", "A", 1);
        when(service.findById("id-1")).thenReturn(p);

        mockMvc.perform(get("/product/delete/id-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).findById("id-1");
        verify(service, times(1)).delete(p);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getDelete_notFound_redirectsToProductList_andDoesNotDelete() throws Exception {
        when(service.findById("missing")).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/product/delete/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).findById("missing");
        verify(service, never()).delete(any(Product.class));
        verifyNoMoreInteractions(service);
    }
}