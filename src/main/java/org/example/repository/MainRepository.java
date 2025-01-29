package org.example.repository;

import org.example.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainRepository extends JpaRepository<Users, Long> {

    @Query(nativeQuery = true, value = "SELECT table_name FROM information_schema.tables WHERE table_schema = :schema")
    List<String> getTableList(@Param("schema") String tableSchema);

    @Query(nativeQuery = true, value = "SELECT table_name, column_name FROM information_schema.columns WHERE table_name IN (:tableName) AND table_schema = :schema")
    List<Object[]> getTableColumns(@Param("tableName") String[] tableName, @Param("schema") String tableSchema);
}