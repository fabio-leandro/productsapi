package br.com.fabio.productapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fabio.productapi.dtos.ProductDTO;
import br.com.fabio.productapi.dtos.mapper.ProductMapper;
import br.com.fabio.productapi.entities.Product;
import br.com.fabio.productapi.exceptions.ProductNotFoundException;
import br.com.fabio.productapi.repositories.ProductRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

		
        private final ProductRepository productRepository;
        
        private final ProductMapper productMapper = ProductMapper.INSTANCE;

        public Product create(ProductDTO productDTO){
            Product product = productMapper.toModel(productDTO);
            Product productSaved = productRepository.save(product);
            return productSaved;
        }
        
        public List<ProductDTO> listAll(){
            List<Product> products = productRepository.findAll();
            List<ProductDTO> productsDtos = products.stream()
            		.map(p -> productMapper.toDTO(p)).collect(Collectors.toList());
            return productsDtos;
        }

		public ProductDTO findById(Long productCode) throws ProductNotFoundException {
			Product product = productRepository.findById(productCode)
					.orElseThrow(()-> new ProductNotFoundException("Not found product with code "+productCode));
			ProductDTO productDTO = productMapper.toDTO(product);
			return productDTO;
		}

		public void delete(Long productCode) throws ProductNotFoundException {
			ProductDTO productDTO = findById(productCode);
			productRepository.deleteById(productDTO.getProductCode());
		}
		
		public ProductDTO update(Long productCode, ProductDTO productDTO) throws ProductNotFoundException{
			findById(productCode);
			productRepository.save(productMapper.toModel(productDTO));
			return productDTO;
			
		}
		
		
        
        
        
        
}
