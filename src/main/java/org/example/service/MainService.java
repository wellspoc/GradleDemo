package org.example.service;


import lombok.NoArgsConstructor;
import org.example.constant.Status;
import org.example.dto.QueryBuilderDTO;
import org.example.dto.RulesDTO;
import org.example.entity.RuleData;
import org.example.entity.Rules;
import org.example.entity.Users;
import org.example.repository.MainRepository;
import org.example.repository.RuleDataRepository;
import org.example.repository.RulesRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class MainService {
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    MainRepository mainRepository;
    @Autowired
    RulesRepository rulesRepository;

    @Autowired
    RuleDataRepository ruleDataRepository;

    public List<String> getTables(){
        return mainRepository.getTableList();
    }

    public List<String> getTableColumn(String[] tableName){
        List<Object[]> columns= mainRepository.getTableColumns(tableName);
        List<String> columnList = new ArrayList<>();
        for (Object[] row : columns) {
            String table = (String) row[0];
            String column = (String) row[1];
            columnList.add(table+"."+column);
        }
        return columnList;
    }

    public Long saveRule(QueryBuilderDTO queryBuilderDTO){
        Users user = new Users();
        user.setUserId(1l);

        queryBuilderDTO.setCreatedDate(LocalDateTime.now());
        queryBuilderDTO.setUpdatedDate(LocalDateTime.now());
        //Add Rule
        Rules rules = modelMapper.map(queryBuilderDTO, Rules.class);
        rules.setCreatedBy(user);
        rules.setUpdatedBy(user);
        rules= rulesRepository.save(rules);

        //Add Rule Data
        RuleData ruleData = modelMapper.map(queryBuilderDTO, RuleData.class);
        ruleData.setRule(rules);
        ruleData= ruleDataRepository.save(ruleData);

       return rules.getRuleId();
    }
    public List<RulesDTO> getRuleList(){
        List<Rules> rulesList= rulesRepository.findAll();
       return rulesList.stream().map(this::mapEntityToDto)
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
        return queryBuilderDTO;
    }
}
