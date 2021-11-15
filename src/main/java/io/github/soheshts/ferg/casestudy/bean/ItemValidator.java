package io.github.soheshts.ferg.casestudy.bean;

import io.github.soheshts.ferg.casestudy.model.Item;
import io.github.soheshts.ferg.casestudy.model.Items;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;

public class ItemValidator {
    //@Handler
    public void validateItem(@Body Items items, Exchange exchange) {
        System.out.println("Inside Validate method");
        StringBuilder errorMessage = new StringBuilder();
        boolean error = false;
        for (Item item : items.getItems()) {
            if (item.getItemId() == null || item.getItemId() <= 0) {
                errorMessage.append("ItemId cannot be null or less than 0.");
                error = true;
            }
            if (StringUtils.isEmpty(item.getItemCategory())) {
                errorMessage.append("ItemCategory cannot be Empty.");
                error = true;
            }
            if (StringUtils.isEmpty(item.getLocationId())) {
                errorMessage.append("LocationID cannot be null.");
                error = true;
            }
            if (item.getPrice() == null || item.getPrice() <= 0) {
                errorMessage.append("Price cannot be null  or less than 0.");
                error = true;
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                errorMessage.append("Quantity cannot be null  or less than 0.");
                error = true;
            }

        }
        if (error) {
            System.out.println("ERROR: " + errorMessage);
            exchange.getMessage().setBody(errorMessage.toString());
            exchange.setProperty("VALID", false);
        }
        else{
            exchange.setProperty("VALID", true);
        }
    }

}
