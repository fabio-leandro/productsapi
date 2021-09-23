package br.com.fabio.productapi.dtos.mapper;

import br.com.fabio.productapi.dtos.ProductDTO;
import br.com.fabio.productapi.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO toDTO(Product product);
    Product toModel(ProductDTO productDTO);
}
