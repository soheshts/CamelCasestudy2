package io.github.soheshts.ferg.casestudy.route;

import io.github.soheshts.ferg.casestudy.model.Item;
import io.github.soheshts.ferg.casestudy.model.ItemCategory;
import io.github.soheshts.ferg.casestudy.model.Items;
import io.github.soheshts.ferg.casestudy.model.amq.ItemAMQ;
import io.github.soheshts.ferg.casestudy.model.amq.ItemsAMQ;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MainRoute extends RouteBuilder {
    @Autowired
    CamelContext context;
    ItemsAMQ itemsAMQ = new ItemsAMQ();

    @Override
    public void configure() throws Exception {
        context.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        context.getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto).host("localhost").port(8080);

        rest()
                .get("/health").route().routeId("HEALTH").log("Service is up")
                .setBody(constant("Service is up")).endRest()

                .post("/sendToAMQ").route().routeId("SENDTOAMQ").convertBodyTo(Items.class).log("Body: ${body}")
                .split(simple("${body.items}")).setProperty("ITEM", body()).to("direct:getPrices").process(exchange -> {
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
                    itemsAMQ.getItems().add(itemAMQ);

                }).end().process(exchange -> exchange.getMessage().setBody(itemsAMQ))
                .marshal().json(JsonLibrary.Jackson).log("AMQ BODY : ${body}").to("direct:toAMQ").process(exchange -> {
                    itemsAMQ.setItems(new ArrayList<>());
                }).endRest();

        from("direct:getPrices").routeId("SQLQUERY").log("ITEM CATEGORY: ${body.itemCategory}").
                to("sql:SELECT * FROM item_category where item_category = :#${body.itemCategory}?outputType=SelectOne&outputClass=io.github.soheshts.ferg.casestudy.model.ItemCategory")
                .log("${body}");
        from("direct:toAMQ").routeId("DIRECT:TOAMQ").to("activemq:queue:ferguson?exchangePattern=InOnly");
    }
}
