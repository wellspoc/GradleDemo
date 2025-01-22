package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.ServiceResponse;
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
    public String getText() throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(kieServicesClient);
        return json;
    }

    @GetMapping("getProcess")
    public String getProcess(@RequestParam Long datapid, @RequestParam String container) throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        ProcessInstance processDetails = processClient.getProcessInstance(container,datapid);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(processDetails);
        return json;
    }
    @GetMapping("getContainers")
    public String getContainer() throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);

        StringBuffer sb = new StringBuffer();
        ServiceResponse<KieContainerResourceList> list = kieServicesClient.listContainers();

        ServiceResponse<KieContainerResourceList> response = kieServicesClient.listContainers();
        if (response.getType() == ServiceResponse.ResponseType.SUCCESS) {
            List<KieContainerResource> containers = response.getResult().getContainers();
            sb.append("Available Containers:\n");
            for (KieContainerResource containerLis : containers) {
                sb.append("Container ID: " + containerLis.getContainerId()+" \t");
                sb.append("Release ID: " + containerLis.getReleaseId()+"\n");
            }
        } else {
            sb.append("Error: " + response.getMsg());
        }

        return sb.toString();
    }
    @GetMapping("invokeProcess")
    public Long invokeProcess(@RequestParam String id, @RequestParam String container) throws JsonProcessingException {
        KieServicesConfiguration config;
        config = KieServicesFactory.newRestConfiguration(
                "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server",
                "wbadmin",
                "wbadmin"
        );
        KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(config);
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        Map<String, Object> params = new HashMap<>();
        Long datapid = processClient.startProcess(container, id);
        return datapid;
    }

}
