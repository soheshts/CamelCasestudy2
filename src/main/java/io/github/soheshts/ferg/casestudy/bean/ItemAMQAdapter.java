package io.github.soheshts.ferg.casestudy.bean;

import io.github.soheshts.ferg.casestudy.model.Item;
import io.github.soheshts.ferg.casestudy.model.ItemCategory;
import io.github.soheshts.ferg.casestudy.model.amq.ItemAMQ;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;

public class ItemAMQAdapter {
    @Handler
    public void adapt(Exchange exchange) {
        ItemCategory category1 = exchange.getMessage().getBody(ItemCategory.class);
        ItemAMQ itemAMQ = new ItemAMQ();
        Item item = exchange.getProperty("ITEM", Item.class);
        itemAMQ.setItemCategory(item.getItemCategory());
        itemAMQ.setItemRecord(item.getItemId());
        itemAMQ.setLocationId(item.getLocationId());
        itemAMQ.setPrice(item.getPrice());
        itemAMQ.setQuantity(item.getQuantity());
        itemAMQ.setMaxPrice(category1.getMaxPrice());
        itemAMQ.setMinPrice(category1.getMinPrice());
        exchange.getMessage().setBody(itemAMQ);
    }
}
