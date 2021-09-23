package br.com.fabio.productapi.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long productCode;
    
    private String productName;
    
    private Double price;
    
    private Integer quantity;

}
