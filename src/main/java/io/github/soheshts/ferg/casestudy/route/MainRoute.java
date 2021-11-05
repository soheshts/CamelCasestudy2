package io.github.soheshts.ferg.casestudy.route;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainRoute extends RouteBuilder {
    @Autowired
    CamelContext context;
    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto).host("localhost").port(8080);

        rest()
                .get("/health").route().routeId("HEALTH").log("Service is up")
                .setBody(constant("Service is up")).endRest();
    }
}
