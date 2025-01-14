package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.entity.Users;
import org.example.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainControll {

    @Autowired
    MainService mainService;

    @GetMapping("getUsers")
    public ResponseEntity getData() throws JsonProcessingException {
        List<Users> data = mainService.getData();
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }

    @GetMapping("getTables")
    public ResponseEntity getTables() {
        List<String> data = mainService.getTables();
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }


    @GetMapping("getColumns")
    public ResponseEntity getTableColumns(@RequestParam String tableName) {
        List<String> data = mainService.getTableColumn(tableName);
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        return responseEntity;
    }
}
