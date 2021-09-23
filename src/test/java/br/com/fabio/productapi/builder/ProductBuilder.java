package br.com.fabio.productapi.builder;


import br.com.fabio.productapi.dtos.ProductDTO;



public class ProductBuilder {

    private static final long serialVerisonUID = 1L;

    private Long productCode;
    private String productName;
    private double price;
    private int quantity;

    public ProductBuilder() {
        this.productCode = 1L;
        this.productName = "Ball";
        this.price = 35.40;
        this.quantity = 3;
    }

    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductDTO toProductDTO(){
        return new ProductDTO(this.productCode,this.productName,this.price,this.quantity);
    }


}
