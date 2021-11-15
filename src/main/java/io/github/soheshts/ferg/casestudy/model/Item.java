package io.github.soheshts.ferg.casestudy.model;

import java.io.Serializable;

public class Item implements Serializable {
    private Integer itemId;
    private String itemCategory;
    private String locationId;
    private Long price;
    private Integer quantity;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemCategory='" + itemCategory + '\'' +
                ", locationId='" + locationId + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
