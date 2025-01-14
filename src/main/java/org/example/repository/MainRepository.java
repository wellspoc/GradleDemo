package org.example.repository;
import org.example.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Optional, as Spring automatically detects interfaces extending JpaRepository
public interface MainRepository extends JpaRepository<Users, Long> {

    @Query(nativeQuery = true, value = "show tables")
    List<String> getTableList();

    @Query(nativeQuery = true,value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = 'MYJBPMDB'")
    List<String> getTableColumns(@Param("tableName") String tableName);
}
