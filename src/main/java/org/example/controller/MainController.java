package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.kie.internal.utils.KieHelper;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @GetMapping("getInfo")
    public String getText(@RequestParam String id, @RequestParam String container) throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        List<ProcessInstance> data = processClient.findProcessInstances(container, 0, 10);  // Replace with your container ID

        Map<String, Object> params = new HashMap<>();
        Long datapid = processClient.startProcess(container, id);
        KieHelper kieHelper = new KieHelper();
        ProcessInstance processDetails = processClient.getProcessInstance(container,datapid);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(kieServicesClient);
        return json;
    }
}
