package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.constant.Status;
import org.example.dto.QueryBuilderDTO;
import org.example.dto.RulesDTO;
import org.example.dto.TaskDTO;
import org.example.dto.TaskDetailsDTO;
import org.example.entity.ProcessInstance;
import org.example.entity.RuleData;
import org.example.entity.Rules;
import org.example.entity.Tasks;
import org.example.entity.Users;
import org.example.repository.MainRepository;
import org.example.repository.ProcessInstanceRepository;
import org.example.repository.RuleDataRepository;
import org.example.repository.RulesRepository;
import org.example.repository.TasksRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    MainRepository mainRepository;
    @Autowired
    RulesRepository rulesRepository;

    @Autowired
    RuleDataRepository ruleDataRepository;
    @Autowired
    ProcessInstanceRepository processInstanceRepository;
    @Autowired
    KieService kieService;

    @Autowired
    TasksRepository tasksRepository;

    @Value("${spring.tableschema}")
    String tableSchema = "public";
    public List<String> getTables() {
        return mainRepository.getTableList(tableSchema);
    }

    public List<String> getTableColumn(String[] tableName) {
        return mainRepository.getTableColumns(tableName,tableSchema).stream()
                .map(row -> row[0] + "." + row[1])
                .collect(Collectors.toList());
    }

    public Long saveRule(QueryBuilderDTO queryBuilderDTO) {
        Users user = new Users();
        user.setUserId(1L);

        queryBuilderDTO.setCreatedDate(LocalDateTime.now());
        queryBuilderDTO.setUpdatedDate(LocalDateTime.now());

        Rules rules = modelMapper.map(queryBuilderDTO, Rules.class);
        rules.setCreatedBy(user);
        rules.setUpdatedBy(user);
        rules = rulesRepository.save(rules);

        RuleData ruleData = modelMapper.map(queryBuilderDTO, RuleData.class);
        ruleData.setRule(rules);
        ruleDataRepository.save(ruleData);
        if(rules.getStatus() == 1){
            ProcessInstance processInstance = processInstanceRepository.findByRule(rules);
            if(processInstance!=null){
                processInstanceRepository.delete(processInstance);
            }
            Long instanceId = kieService.invokeProcess("ReviewProcess.review","ReviewProcess_1.0.0-SNAPSHOT");
            processInstance = new ProcessInstance();
            processInstance.setRule(rules);
            processInstance.setProcessId(instanceId);
            processInstance.setStatus(rules.getStatus());
            processInstance.setUpdatedDate(LocalDateTime.now());
            processInstance.setCreatedDate(LocalDateTime.now());
            processInstance = processInstanceRepository.save(processInstance);
            List<Tasks> tasksList = kieService.getTasks(instanceId,"ReviewProcess_1.0.0-SNAPSHOT");
            for(int i=0;i<tasksList.size();i++){
                Tasks tasks= tasksList.get(i);
                tasks.setProcess(processInstance);
                tasks = tasksRepository.save(tasks);
                System.out.println(tasks);
            }
        }
        return rules.getRuleId();
    }

    public List<RulesDTO> getRuleList() {
        return rulesRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    private RulesDTO mapEntityToDto(Rules entity) {
        RulesDTO dto = new RulesDTO();
        dto.setActive(entity.getActive());
        dto.setRuleId(entity.getRuleId());
        dto.setStatus(Status.values()[entity.getStatus()].toString());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(Math.toIntExact(entity.getCreatedBy().getUserId()));
        dto.setRuleName(entity.getRuleName());
        return dto;
    }

    public QueryBuilderDTO getRule(Long ruleId) {
        Rules rules=rulesRepository.findById(ruleId).get();
        RuleData ruleData = ruleDataRepository.findByRule(rules);
        QueryBuilderDTO queryBuilderDTO = new QueryBuilderDTO();
        queryBuilderDTO.setRuleName(rules.getRuleName());
        queryBuilderDTO.setColumnName(ruleData.getColumnName());
        queryBuilderDTO.setSqlQuery(ruleData.getSqlQuery());
        queryBuilderDTO.setTableName(ruleData.getTableName());
        queryBuilderDTO.setRuleId(ruleId);
        queryBuilderDTO.setRuleDataId(ruleData.getId());
        queryBuilderDTO.setStatus(rules.getStatus());
        queryBuilderDTO.setState(Status.values()[rules.getStatus()].toString());
        return queryBuilderDTO;
    }

    public List<TaskDTO> getTaskList() {
        return tasksRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    private TaskDTO mapEntityToDto(Tasks entity) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(entity.getTaskId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getUpdatedDate());
        dto.setOwner(entity.getOwner());
        dto.setRuleName(entity.getProcess().getRule().getRuleName());
        return dto;
    }

    public TaskDetailsDTO getTaskDetails(Long taskId) {
        Tasks task = tasksRepository.findById(taskId).get();
        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();
        taskDetailsDTO.setRole(task.getRole());
        taskDetailsDTO.setTaskId(taskId);
        taskDetailsDTO.setRuleName(task.getProcess().getRule().getRuleName());
        taskDetailsDTO.setStatus(task.getStatus());
        RuleData ruleData = ruleDataRepository.findByRule(task.getProcess().getRule());
        taskDetailsDTO.setQuery(ruleData.getSqlQuery());
        return taskDetailsDTO;
    }

    public Long updateTask(TaskDetailsDTO taskDetailsDTO) {
            kieService.startTask("ReviewProcess_1.0.0-SNAPSHOT",taskDetailsDTO.getTaskId(),"wbadmin");
            kieService.completeTask("ReviewProcess_1.0.0-SNAPSHOT",taskDetailsDTO.getTaskId(),"wbadmin",taskDetailsDTO.getStatus());
            Tasks tasks = tasksRepository.findById(taskDetailsDTO.getTaskId()).get();
            tasks.setStatus("Completed");
            tasksRepository.save(tasks);
            if(taskDetailsDTO.getStatus().equals("Accept")){
                Rules rules = tasks.getProcess().getRule();
                rules.setStatus(3);
                rulesRepository.save(rules);
            }
            else{
                ProcessInstance processInstance = tasks.getProcess();
                List<Tasks> tasksList = kieService.getTasks(processInstance.getProcessId(),"ReviewProcess_1.0.0-SNAPSHOT");
                for(int i=0;i<tasksList.size();i++){
                    Tasks task= tasksList.get(i);
                    task.setProcess(processInstance);
                    tasksRepository.save(task);
                }
            }
            return taskDetailsDTO.getTaskId();
    }
}
