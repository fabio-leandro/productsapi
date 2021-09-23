package br.com.fabio.productapi.repositories;

import br.com.fabio.productapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,Long> {
}
