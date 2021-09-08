package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, String> {


    @Query(
            value = "select p from Project p join p.reports ad group by p Order By created_at desc",
            countQuery = "select count(p) from Project p"
    )
    List<Project> findFirst2ByOrderByProject_ReportsCountDesc();

}
