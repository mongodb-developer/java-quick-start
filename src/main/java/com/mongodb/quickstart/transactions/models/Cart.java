package com.mongodb.quickstart.transactions.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Cart {

    private String id;
    private List<Item> items;

    public Cart() {
    }

    public Cart(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public Cart setId(String id) {
        this.id = id;
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public Cart setItems(List<Item> items) {
        this.items = items;
        return this;
    }

    @Override
    public String toString() {
        return "Cart{" + "id='" + id + '\'' + ", items=" + items + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(items, cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items);
    }

    public static class Item {

        private String productId;
        private Integer quantity;
        private BigDecimal price;

        public Item(String productId, Integer quantity, BigDecimal price) {
            this.productId = productId;
            this.quantity = quantity;
            this.price = price;
        }

        public Item() {
        }

        public String getProductId() {
            return productId;
        }

        public Item setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Item setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public Item setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        @Override
        public String toString() {
            return "Item{" + "productId=" + productId + ", quantity=" + quantity + ", price=" + price + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(productId, item.productId) && Objects.equals(quantity,
                                                                               item.quantity) && Objects.equals(price,
                                                                                                                item.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, quantity, price);
        }
    }
}
