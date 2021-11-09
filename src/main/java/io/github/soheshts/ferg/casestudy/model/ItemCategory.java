package io.github.soheshts.ferg.casestudy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCategory {
    @JsonProperty("item_category")
    private String itemCategory;
    @JsonProperty("min_price")
    private Integer minPrice;
    @JsonProperty("max_price")
    private Integer maxPrice;

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
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

    @Override
    public String toString() {
        return "ItemCategory{" +
                "itemCategory='" + itemCategory + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}
