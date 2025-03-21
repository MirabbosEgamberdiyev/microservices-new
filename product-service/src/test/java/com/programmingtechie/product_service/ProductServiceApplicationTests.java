//package com.programmingtechie.product_service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.programmingtechie.product_service.model.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.testcontainers.containers.MongoDBContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@Testcontainers
//@AutoConfigureMockMvc
//class ProductServiceApplicationTests {
//
//	@Container
//	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.4");
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	private String savedProductId;
//
//	@DynamicPropertySource
//	static void setProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//	}
//
//	@BeforeEach
//	void setUp() throws Exception {
//		Product product = Product.builder()
//				.name("Laptop")
//				.description("Gaming Laptop")
//				.price(BigDecimal.valueOf(1500))
//				.build();
//
//		String productJson = objectMapper.writeValueAsString(product);
//
//		String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(productJson))
//				.andExpect(MockMvcResultMatchers.status().isCreated())
//				.andReturn()
//				.getResponse()
//				.getContentAsString();
//
//		savedProductId = objectMapper.readTree(response).get("id").asText();
//	}
//
//	// ? GET ALL PRODUCTS TEST
//	@Test
//	void shouldGetAllProducts() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(jsonPath("$.length()").value(Optional.of(1)));
//	}
//
//	// ? GET PRODUCT BY ID TEST
//	@Test
//	void shouldGetProductById() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/" + savedProductId))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(jsonPath("$.name").value("Laptop"))
//				.andExpect(jsonPath("$.price").value(Optional.of(1500)));
//	}
//
//	// ? UPDATE PRODUCT TEST
//	@Test
//	void shouldUpdateProduct() throws Exception {
//		Product updatedProduct = Product.builder()
//				.id(savedProductId)
//				.name("Updated Laptop")
//				.description("Updated Gaming Laptop")
//				.price(BigDecimal.valueOf(1700))
//				.build();
//
//		String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);
//
//		mockMvc.perform(MockMvcRequestBuilders.put("/api/product/" + savedProductId)
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(updatedProductJson))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(jsonPath("$.name").value("Updated Laptop"))
//				.andExpect(jsonPath("$.price").value(Optional.of(1700)));
//	}
//
//	// ? DELETE PRODUCT TEST
//	@Test
//	void shouldDeleteProduct() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/" + savedProductId))
//				.andExpect(MockMvcResultMatchers.status().isNoContent());
//
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/" + savedProductId))
//				.andExpect(MockMvcResultMatchers.status().isNotFound());
//	}
//}
