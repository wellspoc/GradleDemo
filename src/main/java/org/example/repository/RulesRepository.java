package org.example.repository;
import org.example.entity.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Optional, as Spring automatically detects interfaces extending JpaRepository
public interface RulesRepository extends JpaRepository<Rules, Long> {
}
