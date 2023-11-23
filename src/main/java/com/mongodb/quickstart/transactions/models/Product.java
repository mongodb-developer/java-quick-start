package com.mongodb.quickstart.transactions.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private String id;
    private Integer stock;
    private BigDecimal price;

    public Product(String id, Integer stock, BigDecimal price) {
        this.id = id;
        this.stock = stock;
        this.price = price;
    }

    public Product() {
    }

    public String getId() {
        return id;
    }

    public Product setId(String id) {
        this.id = id;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public Product setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Product{" + "id='" + id + '\'' + ", stock=" + stock + ", price=" + price + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(stock, product.stock) && Objects.equals(price,
                                                                                                        product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stock, price);
    }
}
