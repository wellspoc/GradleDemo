package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.dto.*;
import org.example.service.KieService;
import org.example.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;
    private final KieService kieService;

    public MainController(MainService mainService, KieService kieService) {
        this.mainService = mainService;
        this.kieService = kieService;
    }

    @GetMapping("/tables")
    public ResponseEntity<List<String>> getTables() {
        return ResponseEntity.ok(mainService.getTables());
    }

    @GetMapping("/columns")
    public ResponseEntity<List<Map<String, String>>> getTableColumns(@RequestParam String tableName) {
        return tableName.isEmpty()
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(mainService.getTableColumn(tableName.split(",")));
    }

    @PostMapping("/rules")
    public ResponseEntity<Long> saveRule(@RequestBody QueryBuilderDTO queryBuilderDTO) {
        return ResponseEntity.ok(mainService.saveRule(queryBuilderDTO));
    }

    @GetMapping("/rules")
    public ResponseEntity<List<RulesDTO>> getRuleList() {
        return ResponseEntity.ok(mainService.getRuleList());
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTaskList() {
        return ResponseEntity.ok(mainService.getTaskList());
    }

    @GetMapping("/rules/{ruleId}")
    public ResponseEntity<QueryBuilderDTO> getRule(@PathVariable Long ruleId) {
        if (ruleId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        QueryBuilderDTO rule = mainService.getRule(ruleId);
        return rule != null ? ResponseEntity.ok(rule) : ResponseEntity.notFound().build();
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDetailsDTO> getTask(@PathVariable Long taskId) {
        if (taskId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        TaskDetailsDTO taskDetails = mainService.getTaskDetails(taskId);
        return taskDetails != null ? ResponseEntity.ok(taskDetails) : ResponseEntity.notFound().build();
    }
    @PostMapping("/tasks")
    public ResponseEntity<Long> updateTask(@RequestBody TaskDetailsDTO taskDetailsDTO) {
        return ResponseEntity.ok(mainService.updateTask(taskDetailsDTO));
    }

    @PostMapping("/executeQuery")
    public ResponseEntity<String> executeQuery(@RequestBody String query) throws JsonProcessingException {
        return ResponseEntity.ok(mainService.executeQuery(query));
    }
}