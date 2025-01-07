package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("getInfo")
    public String getText() throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
        System.out.println(kieServicesClient.toString());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(kieServicesClient);
        return json;
    }
}
