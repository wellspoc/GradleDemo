package org.example.service;


import lombok.NoArgsConstructor;
import org.example.entity.Users;
import org.example.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class MainService {
    @Autowired
    MainRepository mainRepository;
    public List<Users> getData() {
       List<Users> data = mainRepository.findAll();
       return data;
    }

    public List<String> getTables(){
        return mainRepository.getTableList();
    }

    public List<String> getTableColumn(String tableName){
        return mainRepository.getTableColumns(tableName);
    }
}
