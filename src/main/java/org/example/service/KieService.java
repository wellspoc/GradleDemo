package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.entity.Tasks;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KieService {

    @Value("${kieserver.url}")
    String kieUrl = "";
    @Value("${kieserver.username}")
    String KieUsername = "";
    @Value("${kieserver.password}")
    String KiePassword = "";

    private KieServicesClient createKieServicesClient() {
        KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(kieUrl, KieUsername, KiePassword);
        config.setMarshallingFormat(org.kie.server.api.marshalling.MarshallingFormat.JSON);
        return KieServicesFactory.newKieServicesClient(config);
    }

    public Long invokeProcess(String id,String container) {
        KieServicesClient kieServicesClient = createKieServicesClient();
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        Map<String, Object> params = new HashMap<>();
        params.put("Accept", "false");
        params.put("Reject", "false");
        params.put("Rewrite", "true");
        params.put("initiator", "wbadmin");
        return processClient.startProcess(container, id, params);
    }
    public ProcessInstance getProcess(Long datapid, String container) throws JsonProcessingException {
        KieServicesClient kieServicesClient = createKieServicesClient();
        ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
        return processClient.getProcessInstance(container, datapid);
    }
    public List<Tasks> getTasks(Long datapid, String container){
        List<Tasks> tasksList= new ArrayList<>();
        KieServicesClient kieServicesClient = createKieServicesClient();
        UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);

        List<String> statuses = Arrays.asList("Ready", "Reserved", "InProgress");
        List<TaskSummary> taskSummaryList= taskClient.findTasksByStatusByProcessInstanceId(datapid, statuses, 0,10);

        if(taskSummaryList.size()>0){
            for(int i=0;i<taskSummaryList.size();i++){
                TaskSummary t = taskSummaryList.get(i);
                Tasks tasks= new Tasks();
                tasks.setTaskId(t.getId());
                tasks.setCreator(t.getCreatedBy());
                tasks.setOwner(t.getActualOwner());
                tasks.setStatus(t.getStatus());
                tasks.setRole(t.getName());
                tasks.setCreatedDate(LocalDateTime.now());
                tasks.setUpdatedDate(LocalDateTime.now());
                tasksList.add(tasks);
            }

        }
        return tasksList;
    }
    public String startTask(String container, Long taskId,String userId){
        KieServicesClient kieServicesClient = createKieServicesClient();
        UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
        taskClient.releaseTask(container,taskId,userId);
        taskClient.startTask(container,taskId,userId);
       return "started";
    }
    public String completeTask(String container, Long taskId,String userId, String response)
    {
        KieServicesClient kieServicesClient = createKieServicesClient();
        UserTaskServicesClient taskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
        Map<String, Object> params = new HashMap<>();
        params.put("Accept", "false");
        params.put("Reject", "false");
        params.put("Rewrite", "false");
        params.put("initiator", userId);
        params.put(response,"true");
        taskClient.completeTask(container, taskId, userId, params);
        return "completed";
    }
}
