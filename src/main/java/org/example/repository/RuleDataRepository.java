package org.example.repository;
import org.example.entity.RuleData;
import org.example.entity.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Optional, as Spring automatically detects interfaces extending JpaRepository
public interface RuleDataRepository extends JpaRepository<RuleData, Long> {
    RuleData findByRule(Rules rules);
}
