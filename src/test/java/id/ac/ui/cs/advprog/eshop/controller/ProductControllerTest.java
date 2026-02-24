package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService service;

    @Test
    void getCreate_returnsCreateView_andHasProductModel() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
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
    }

    @Test
    void getList_returnsListView_andAddsProducts() throws Exception {
        Product p = new Product();
        p.setProductId("id-1");
        p.setProductName("A");
        p.setProductQuantity(1);

        when(service.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void getEdit_success_returnsEditView_andAddsProduct() throws Exception {
        Product p = new Product();
        p.setProductId("id-1");
        when(service.findById("id-1")).thenReturn(p);

        mockMvc.perform(get("/product/edit").param("id", "id-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attributeExists("product"));

        verify(service, times(1)).findById("id-1");
    }

    @Test
    void getEdit_notFound_returnsEditView_andAddsError() throws Exception {
        when(service.findById("missing")).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/product/edit").param("id", "missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(service, times(1)).findById("missing");
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
    }

    @Test
    void getDelete_success_redirectsToProductList_andCallsDelete() throws Exception {
        Product p = new Product();
        p.setProductId("id-1");
        when(service.findById("id-1")).thenReturn(p);

        mockMvc.perform(get("/product/delete/id-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).findById("id-1");
        verify(service, times(1)).delete(p);
    }

    @Test
    void getDelete_notFound_redirectsToProductList_andDoesNotDelete() throws Exception {
        when(service.findById("missing")).thenThrow(new IllegalArgumentException("not found"));

        mockMvc.perform(get("/product/delete/missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).findById("missing");
        verify(service, never()).delete(any(Product.class));
    }
}