package io.github.soheshts.ferg.casestudy.route;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import io.github.soheshts.ferg.casestudy.bean.ItemAMQAdapter;
import io.github.soheshts.ferg.casestudy.bean.ItemValidator;
import io.github.soheshts.ferg.casestudy.model.Item;
import io.github.soheshts.ferg.casestudy.model.Items;
import io.github.soheshts.ferg.casestudy.model.amq.ItemAMQ;
import io.github.soheshts.ferg.casestudy.model.amq.ItemsAMQ;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bson.Document;
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
        context.getRegistry().bind("mongobean", MongoClients.create("mongodb://localhost:27017"));
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto).host("localhost").port(8080);

        rest()
                .get("/health").route().routeId("HEALTH").log("Service is up")
                .setBody(constant("Service is up")).endRest()

                .post("/sendToAMQ").route().routeId("SENDTOAMQ").convertBodyTo(Items.class).log("Body: ${body}")
                .bean(ItemValidator.class, "validateItem").log("Valid:  ${exchangeProperty.VALID}")
                .choice()
                    .when(simple("${exchangeProperty.VALID} == 'false'"))
                        .log("NOT VALID").stop()
                    .otherwise()
                        .split(simple("${body.items}"))
                            .setProperty("ITEM", body()).to("direct:uniqueChecker")
                            .choice()
                                .when(simple("${exchangeProperty.UNIQUE} == 'true'"))
                                    .to("direct:getPrices")
                                    .bean(ItemAMQAdapter.class).process(exchange -> {
                                        ItemAMQ itemAMQ = exchange.getMessage().getBody(ItemAMQ.class);
                                        itemsAMQ.getItems().add(itemAMQ);
                                    }).to("direct:insertToDB")
                            .endChoice()
                        .end()
                    .process(exchange -> {
                        if(itemsAMQ != null && itemsAMQ.getItems() != null && !itemsAMQ.getItems().isEmpty()) {
                            exchange.getMessage().setBody(itemsAMQ);
                            exchange.setProperty("EMPTY",false);

                        }
                        else{
                            exchange.setProperty("EMPTY",true);
                        }

                    })
                    .choice()
                        .when(simple("${exchangeProperty.EMPTY} == 'false'"))
                            .marshal().json(JsonLibrary.Jackson).log("AMQ BODY : ${body}").to("direct:toAMQ").process(exchange -> {
                                itemsAMQ.setItems(new ArrayList<>());
                            }).log("Items published to queue")
                        .otherwise().log("Nothing to publish to queue!")
                    .endChoice()
                .endChoice().endRest();

        from("direct:getPrices").routeId("SQLQUERY").log("ITEM CATEGORY: ${body.itemCategory}").
                to("sql:SELECT * FROM item_category where item_category = :#${body.itemCategory}?outputType=SelectOne&outputClass=io.github.soheshts.ferg.casestudy.model.ItemCategory")
                .log("${body}");
        from("direct:toAMQ").routeId("DIRECT:TOAMQ").to("activemq:queue:ferguson?exchangePattern=InOnly");
        from("direct:insertToDB").routeId("DIRECT:INSERTTODB")
                .to("mongodb:mongobean?database=ferguson&collection=ProductAudit&operation=insert");
        from("direct:findOneFromDB").routeId("FIND_ONE_FROM_DB")
                .to("mongodb:mongobean?database=ferguson&collection=ProductAudit&operation=findOneByQuery");
        from("direct:uniqueChecker").routeId("UNIQUE_CHECKER").process(exchange -> {
                    Item item =  exchange.getMessage().getBody(Item.class);
                    String searchString = "{\"itemRecord\":"+item.getItemId()+"}";
                    exchange.getMessage().setBody(BasicDBObject.parse(searchString));
                })
                .to("direct:findOneFromDB").process(exchange -> {
                    if( exchange.getMessage().getBody() ==null){
                        exchange.setProperty("UNIQUE",true);
                    }
                    exchange.getMessage().setBody(exchange.getProperty("ITEM"));
                }).log("retrieved body: " + body());
    }
}
