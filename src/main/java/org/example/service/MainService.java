package org.example.service;

import lombok.RequiredArgsConstructor;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ModelMapper modelMapper;
    private final MainRepository mainRepository;
    private final RulesRepository rulesRepository;
    private final RuleDataRepository ruleDataRepository;
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

        return rules.getRuleId();
    }

    public List<RulesDTO> getRuleList() {
        return rulesRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private RulesDTO mapEntityToDto(Rules entity) {
        return RulesDTO.builder()
                .active(entity.getActive())
                .ruleId(entity.getRuleId())
                .status(Status.values()[entity.getStatus()].toString())
                .updatedDate(entity.getUpdatedDate())
                .createdBy(Math.toIntExact(entity.getCreatedBy().getUserId()))
                .ruleName(entity.getRuleName())
                .build();
    }

    public QueryBuilderDTO getRule(Long ruleId) {
        Rules rules = rulesRepository.findById(ruleId).orElseThrow(() -> new IllegalArgumentException("Rule not found"));
        RuleData ruleData = ruleDataRepository.findByRule(rules);

        return QueryBuilderDTO.builder()
                .ruleName(rules.getRuleName())
                .columnName(ruleData.getColumnName())
                .sqlQuery(ruleData.getSqlQuery())
                .tableName(ruleData.getTableName())
                .ruleId(ruleId)
                .ruleDataId(ruleData.getId())
                .build();
    }
}
