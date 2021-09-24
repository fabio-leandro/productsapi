package br.com.fabio.productapi.cotrollers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;

import br.com.fabio.productapi.cotrollers.validations.ProductValidationsErrors;
import br.com.fabio.productapi.dtos.ProductDTO;
import br.com.fabio.productapi.entities.Product;
import br.com.fabio.productapi.exceptions.ProductNotFoundException;
import br.com.fabio.productapi.services.ProductService;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody @Valid ProductDTO productDTO){
		Product productCreated = productService.create(productDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
	}
	
	@GetMapping
	public ResponseEntity<List<ProductDTO>> listAll(){
		return ResponseEntity.status(HttpStatus.OK).body(productService.listAll());
	}
	
	@GetMapping("/{productCode}")
	public ResponseEntity<ProductDTO> findByProductCode(@PathVariable Long productCode) throws ProductNotFoundException{
		ProductDTO productDTO = productService.findById(productCode);
		return ResponseEntity.status(HttpStatus.OK).body(productDTO);
	}
	
	@DeleteMapping("/{productCode}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable  Long productCode) throws ProductNotFoundException{
		productService.delete(productCode);
	}
	
	@PutMapping("/{productCode}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long productCode, 
			@RequestBody @Valid ProductDTO productDTO) throws ProductNotFoundException{
		
		productService.update(productCode, productDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, Object> handlerValidationException(MethodArgumentNotValidException ex){
		return new ProductValidationsErrors().callValidatorsException(ex);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JsonParseException.class)
	public Map<String, Object> callJsonParseException(){
		return new ProductValidationsErrors().callJsonException();
	}

}
