package io.github.soheshts.ferg.casestudy.processor;

import io.github.soheshts.ferg.casestudy.model.Item;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ItemValidationProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Item item = exchange.getMessage().getBody(Item.class);
        StringBuilder errorMessage = new StringBuilder();
        Boolean isError = false;
        if (item.getItemId() == null) {
            errorMessage.append("Item ID cannot be null; ");
            isError = true;
        }
    }
}
