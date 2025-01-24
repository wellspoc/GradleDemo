package org.example.controller;

import org.example.dto.QueryBuilderDTO;
import org.example.dto.RulesDTO;
import org.example.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api")  // Common base path for all endpoints
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/tables")
    public ResponseEntity<List<String>> getTables() {
        List<String> tables = mainService.getTables();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/columns")
    public ResponseEntity<List<String>> getTableColumns(@RequestParam String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Input validation
        }

        String[] tableList = tableName.split(",");
        List<String> columns = mainService.getTableColumn(tableList);
        return ResponseEntity.ok(columns);
    }

    @PostMapping("/rules")
    public ResponseEntity<Long> saveRule(@RequestBody QueryBuilderDTO queryBuilderDTO) {
        if (queryBuilderDTO == null) {
            return ResponseEntity.badRequest().body(null);  // Input validation
        }

        Long savedRuleId = mainService.saveRule(queryBuilderDTO);
        return ResponseEntity.ok(savedRuleId);
    }

    @GetMapping("/rules")
    public ResponseEntity<List<RulesDTO>> getRuleList() {
        List<RulesDTO> rules = mainService.getRuleList();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/rules/{ruleId}")
    public ResponseEntity<QueryBuilderDTO> getRule(@PathVariable Long ruleId) {
        if (ruleId == null || ruleId <= 0) {
            return ResponseEntity.badRequest().body(null);  // Validate ID
        }

        QueryBuilderDTO rule = mainService.getRule(ruleId);
        if (rule == null) {
            return ResponseEntity.notFound().build();  // Return 404 if rule is not found
        }

        return ResponseEntity.ok(rule);
    }
    private static final String KIE_SERVER_URL = "https://jbpm-53jhowy2kq-uc.a.run.app/kie-server/services/rest/server";
    private static final String KIE_SERVER_USER = "wbadmin";
    private static final String KIE_SERVER_PASSWORD = "wbadmin";
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private KieServicesClient createKieServicesClient() {
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(KIE_SERVER_URL, KIE_SERVER_USER, KIE_SERVER_PASSWORD);
        config.setMarshallingFormat(org.kie.server.api.marshalling.MarshallingFormat.JSON);
        return KieServicesFactory.newKieServicesClient(config);
    }

    @GetMapping("/getInfo")
    public String getInfo() throws JsonProcessingException {
        KieServicesClient kieServicesClient = createKieServicesClient();
        return OBJECT_WRITER.writeValueAsString(kieServicesClient);
    }

    @GetMapping("/getProcess")
    public String getProcess(@RequestParam Long datapid, @RequestParam String container) throws JsonProcessingException {
        KieServicesClient kieServicesClient = createKieServicesClient();
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        ProcessInstance processDetails = processClient.getProcessInstance(container, datapid);
        return OBJECT_WRITER.writeValueAsString(processDetails);
    }

    @GetMapping("/getContainers")
    public String getContainers() {
        KieServicesClient kieServicesClient = createKieServicesClient();
        ServiceResponse<KieContainerResourceList> response = kieServicesClient.listContainers();

        if (response.getType() == ServiceResponse.ResponseType.SUCCESS) {
            List<KieContainerResource> containers = response.getResult().getContainers();
            StringBuilder sb = new StringBuilder("Available Containers:\n");
            containers.forEach(container -> sb.append(String.format("Container ID: %s \t Release ID: %s\n", container.getContainerId(), container.getReleaseId())));
            return sb.toString();
        } else {
            return String.format("Error: %s", response.getMsg());
        }
    }

    @GetMapping("/invokeProcess")
    public Long invokeProcess(@RequestParam String id, @RequestParam String container) {
        KieServicesClient kieServicesClient = createKieServicesClient();
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        Map<String, Object> params = Collections.emptyMap();
        return processClient.startProcess(container, id, params);
    }
}
