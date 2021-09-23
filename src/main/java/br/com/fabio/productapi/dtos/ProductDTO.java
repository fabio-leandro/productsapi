package br.com.fabio.productapi.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productCode;
    
    @NotNull(message = "The product name cannot be null.")
    @NotBlank(message = "Field cannot be blank")
    @Size(min = 2, message = "Characters insuficients")
    private String productName;
    
    @NotNull(message = "The product price cannot be null.")
    private Double price;
    
    @NotNull(message = "The quantity cannot be null.")
    private Integer quantity;
}
