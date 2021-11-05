package io.github.soheshts.ferg.casestudy.model;

import java.io.Serializable;
import java.util.List;

public class Items implements Serializable {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
