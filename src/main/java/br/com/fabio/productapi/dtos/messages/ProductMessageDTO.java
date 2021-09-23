package br.com.fabio.productapi.dtos.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductMessageDTO {
	
	private String message;
	
	public static ProductMessageDTO messageProductDeleted(Long productCode) {
		String msg = String.format("Product with ID %s was Deleted", productCode);
		return new ProductMessageDTO(msg);
	}
		
	
}
