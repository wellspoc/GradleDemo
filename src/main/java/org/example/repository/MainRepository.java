package org.example.repository;
import org.example.entity.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Optional, as Spring automatically detects interfaces extending JpaRepository
public interface MainRepository extends JpaRepository<Users, Long> {

    @Value("${spring.tableschema}")
    String tableSchema = "public";

    @Query(nativeQuery = true, value = "SELECT table_name FROM information_schema.tables WHERE table_schema = '"+tableSchema+"'")
    List<String> getTableList();

    @Query(nativeQuery = true,value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = :tableName AND TABLE_SCHEMA = '"+tableSchema+"'")
    List<String> getTableColumns(@Param("tableName") String tableName);
}
