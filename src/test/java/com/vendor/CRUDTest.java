package com.vendor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendor.models.Product;
import com.vendor.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
public class CRUDTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // will not test as needed security context mock
    public void testGetByNameProductEndpoint() throws Exception {
        Product p = new Product(5, "cola");
        given(productService.getByName(p.getName())).willReturn(p);
        ResultActions response = mockMvc.perform(get("/products?name=" + p.getName()))
                ;
    }
}
