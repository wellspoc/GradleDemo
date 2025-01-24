package org.example.controller;

import org.example.dto.QueryBuilderDTO;
import org.example.dto.RulesDTO;
import org.example.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainControll {

    @Autowired
    MainService mainService;

    @GetMapping("getTables")
    public ResponseEntity getTables() {
        List<String> data = mainService.getTables();
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }


    @GetMapping("getColumns")
    public ResponseEntity getTableColumns(@RequestParam String tableName) {
        String[] tableList = tableName.split(",");
        List<String> data = mainService.getTableColumn(tableList);
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }
    @PostMapping("save")
    public ResponseEntity saveRule(@RequestBody QueryBuilderDTO queryBuilderDTO) {
        Long data = mainService.saveRule(queryBuilderDTO);
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }

    @GetMapping("getRules")
    public ResponseEntity getRuleList() {
        List<RulesDTO> data = mainService.getRuleList();
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }

    @GetMapping("fetchRule")
    public ResponseEntity getRule(@RequestParam Long ruleId) {
        QueryBuilderDTO data = mainService.getRule(ruleId);
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }
}
