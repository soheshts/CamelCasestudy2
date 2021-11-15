package io.github.soheshts.ferg.casestudy.model.amq;

public class ItemAMQ {
    private Integer itemRecord;
    private String itemCategory;
    private String locationId;
    private Long price;
    private Integer quantity;
    private Integer minPrice;
    private Integer maxPrice;

    public Integer getItemRecord() {
        return itemRecord;
    }

    public void setItemRecord(Integer itemRecord) {
        this.itemRecord = itemRecord;
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

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }
}
