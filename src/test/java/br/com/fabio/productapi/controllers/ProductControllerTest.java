package br.com.fabio.productapi.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fabio.productapi.builder.ProductBuilder;
import br.com.fabio.productapi.cotrollers.ProductController;
import br.com.fabio.productapi.dtos.ProductDTO;
import br.com.fabio.productapi.dtos.mapper.ProductMapper;
import br.com.fabio.productapi.entities.Product;
import br.com.fabio.productapi.exceptions.ProductNotFoundException;
import br.com.fabio.productapi.services.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
	
	private static final long PRODUCT_CODE_INVALID = 2L;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Mock
	private ProductService productService;
	
	private ProductMapper productMapper = ProductMapper.INSTANCE;
	
	@InjectMocks
	private ProductController productController;
	
	
	@BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setViewResolvers((s, locale) -> new MappingJackson2JsonView()).build();
    }
	
	@Test
	void whenCalledThenReturnProductCreatedStatusAndAlsoReturnProduct() throws Exception {
		
			//given
			ProductDTO productDTO = new ProductBuilder().toProductDTO();
			Product product = productMapper.toModel(productDTO);
			
			//when
			when(productService.create(productDTO)).thenReturn(product);
			
			//then
			this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
					.content(objectMapper.writeValueAsString(product))
							.contentType(MediaType.APPLICATION_JSON))
							.andExpect(status().isCreated())
							.andExpect(jsonPath("$.productCode", is(productDTO.getProductCode().intValue())))
							.andExpect(jsonPath("$.productName", is(productDTO.getProductName())))
							.andExpect(jsonPath("$.price", is(productDTO.getPrice())))
							.andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())));

	}
	
	@Test
	void whenCalledThenReturnStatusOk() throws Exception {
		
		//given
    	ProductDTO productDTO = new ProductBuilder().toProductDTO();
    	
    	//when
    	when(productService.listAll()).thenReturn(Collections.singletonList(productDTO));
    	
    	//then
    	this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$[0].productCode",is(productDTO.getProductCode().intValue())))
    			.andExpect(jsonPath("$[0].productName", is(productDTO.getProductName())))
    			.andExpect(jsonPath("$[0].price", is(productDTO.getPrice())))
    			.andExpect(jsonPath("$[0].quantity", is(productDTO.getQuantity())));
    	
	}
	
	@Test
	void whenGETCalledWithParameterProductCodeThenReturnStatusOK() throws Exception {
		
		//given
		ProductDTO productDTO = new ProductBuilder().toProductDTO();
		//when
		when(productService.findById(productDTO.getProductCode())).thenReturn(productDTO);
		
		//then
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/"+productDTO.getProductCode())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.productCode",is(productDTO.getProductCode().intValue())))
    			.andExpect(jsonPath("$.productName", is(productDTO.getProductName())))
    			.andExpect(jsonPath("$.price", is(productDTO.getPrice())))
    			.andExpect(jsonPath("$.quantity", is(productDTO.getQuantity())));

	}
	
	@Test
	void whenGETCalledWithParameterProductCodeThenReturnProductNotFoundException() throws Exception {
		
		//when
		when(productService.findById(PRODUCT_CODE_INVALID)).thenThrow(ProductNotFoundException.class);
		
		//then
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/"+PRODUCT_CODE_INVALID)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
				
	}
	
	
	@Test
	void whenDeleteCalledThenReturnStatusNoContent() throws Exception {
		
		//given
		ProductDTO productDTO = new ProductBuilder().toProductDTO();
		
		//when
		doNothing().when(productService).delete(productDTO.getProductCode());
		
		//then
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/"+productDTO.getProductCode())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
	}
	
	@Test
	void whenDeleteCalledAndProductNotFountThenReturnAException() throws Exception {
		
		// when
		doThrow(ProductNotFoundException.class).when(productService).delete(PRODUCT_CODE_INVALID);
		
		// then
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/" + PRODUCT_CODE_INVALID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
	
	@Test
	void whenUpdateCalledThenReturnedStatusCodeOK() throws Exception {
		
		//given
		ProductDTO productDTO = new ProductBuilder().toProductDTO();
		
		//when
		when(productService.update(productDTO.getProductCode(), productDTO))
		.thenReturn(productDTO);
		
		//then
		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/" + 
		productDTO.getProductCode()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDTO)))
				.andExpect(status().isCreated());
		
	}
	
	@Test
	void whenPutCalledThenItShouldThrowException() throws Exception {
		
		//given
		ProductDTO productDTO = new ProductBuilder().toProductDTO();
		
		//when
		when(productService.update(PRODUCT_CODE_INVALID, productDTO))
		.thenThrow(ProductNotFoundException.class);
		
		
		//then
		this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/" + 
				PRODUCT_CODE_INVALID).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productDTO)))
						.andExpect(status().isNotFound());
		
	}
	
	
	

}
