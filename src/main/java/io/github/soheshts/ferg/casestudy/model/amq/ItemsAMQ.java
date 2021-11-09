package io.github.soheshts.ferg.casestudy.model.amq;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class ItemsAMQ {
    private List<ItemAMQ> items = new ArrayList<>();

    @JsonValue
    public List<ItemAMQ> getItems() {
        return items;
    }

    public void setItems(List<ItemAMQ> items) {
        this.items = items;
    }
}
