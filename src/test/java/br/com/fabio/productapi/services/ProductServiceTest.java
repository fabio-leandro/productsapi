package br.com.fabio.productapi.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fabio.productapi.builder.ProductBuilder;
import br.com.fabio.productapi.dtos.ProductDTO;
import br.com.fabio.productapi.dtos.mapper.ProductMapper;
import br.com.fabio.productapi.entities.Product;
import br.com.fabio.productapi.exceptions.ProductNotFoundException;
import br.com.fabio.productapi.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper = ProductMapper.INSTANCE;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenCalledTheProductShouldBeCreated(){

        //given
        ProductDTO productDTO = new ProductBuilder().toProductDTO();
        Product product = productMapper.toModel(productDTO);

        //when
        when(productRepository.save(product)).thenReturn(product);

        //then
        Product productCreated = productService.create(productDTO);
        assertThat(productCreated,equalTo(product));

    }
    
    @Test
    void whenCalledThenReturnAListByProducts() {
    	
    	//given
    	ProductDTO productDTO = new ProductBuilder().toProductDTO();
    	Product product = productMapper.toModel(productDTO);
    	
    	List<Product> productsList = Collections.singletonList(product);
    	List<ProductDTO> productsDtosList = Collections.singletonList(productDTO);
    	
    	
    	//when
    	when(productRepository.findAll()).thenReturn(productsList);
    	
    	//then
    	List<ProductDTO> productsDtosListActual = productService.listAll();
    	
    	Assertions.assertEquals(productsDtosList,productsDtosListActual);
    	
    	
    }
    
    @Test
    void whenGETCalledWithProdutoCodeThenReturnThisProduct() throws ProductNotFoundException {
    	
    	//given
    	ProductDTO productDTO = new ProductBuilder().toProductDTO();
    	Product  product = productMapper.toModel(productDTO);
    	
    	//when
    	when(productRepository.findById(productDTO.getProductCode()))
    	.thenReturn(Optional.of(product));
    	
    	//then
    	ProductDTO productExpectedDto = productService.findById(productDTO.getProductCode());
    	Assertions.assertEquals(productExpectedDto, productDTO);
    	
    	
    }

    @Test
    void whenCalledItShouldBeDeletedProductWithInformedCode() throws ProductNotFoundException {
    	//given
    	ProductDTO productDTO = new ProductBuilder().toProductDTO();
    	
    	//when
    	doNothing().when(productRepository).deleteById(productDTO.getProductCode());
    	
    	//then
    	productService.delete(productDTO.getProductCode());
    	verify(productRepository,times(1)).deleteById(productDTO.getProductCode());
    	
    }
}
