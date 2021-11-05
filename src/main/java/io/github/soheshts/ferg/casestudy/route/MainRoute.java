package io.github.soheshts.ferg.casestudy.route;

import io.github.soheshts.ferg.casestudy.model.Items;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainRoute extends RouteBuilder {
    @Autowired
    CamelContext context;
    @Override
    public void configure() throws Exception {
        context.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        context.getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto).host("localhost").port(8080);

        rest()
                .get("/health").route().routeId("HEALTH").log("Service is up")
                .setBody(constant("Service is up")).endRest()

                .post("/sendToAMQ").route().routeId("SENDTOAMQ").convertBodyTo(Items.class)
                .log("Body: ${body}").marshal().json(JsonLibrary.Jackson).to("activemq:queue:ferguson?exchangePattern=InOnly").endRest();
    }
}
