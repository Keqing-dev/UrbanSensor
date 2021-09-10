package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Project;
import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface ProjectRepository extends JpaRepository<Project, String> {
    @Query(
            value = "SELECT new Project(p ,COUNT(r))" +
                    "FROM Project p, Report  r " +
                    "WHERE p.id = r.project.id " +
                    "GROUP BY p, p.createdAt " +
                    "ORDER BY COUNT(r) DESC,  p.createdAt DESC",
            countQuery = "SELECT COUNT(r) " +
                    "FROM Project p, Report  r " +
                    "WHERE  p.id = r.project.id " +
                    "GROUP BY p, p.createdAt  " +
                    "ORDER BY COUNT(r) DESC,  p.createdAt DESC"
    )
    Page<Project> findAllAndCountTheirReportsMoreThan0(Pageable pageable);


    Page<Project> findAllByUserProjects_UserOrderByCreatedAtDesc(User user, Pageable pageable);


    @Query(
            value = "SELECT new Project(p,count(report)) " +
                    "FROM Project p  " +
                    "LEFT JOIN Report as report on p.id = report.project.id " +
                    "GROUP BY p.id " +
                    "ORDER BY count(report) DESC, p.createdAt DESC ",
            countQuery = "SELECT count(report) " +
                    "FROM Project p  " +
                    "LEFT JOIN Report as report on p.id = report.project.id " +
                    "GROUP BY p.id " +
                    "ORDER BY count(report) DESC, p.createdAt DESC "
    )
    Page<Project> findAllProjectAndCountTheirReports(Pageable pageable);

    @Query(
            value = "SELECT new Project(p.project,count(report)) " +
                    "FROM users_project p " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1  " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY count(report) DESC, p.project.createdAt DESC ",

            countQuery = "SELECT count(report) " +
                    "FROM users_project p  " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1  " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY count(report) DESC, p.project.createdAt DESC "
    )
    Page<Project> findAllProjectByUser_IdAndCountTheirReports(String userId, Pageable pageable);


    @Query(
            value = "SELECT new Project(p.project,count(report)) " +
                    "FROM users_project p " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1 AND  LOWER(p.project.name)  LIKE lower(concat('%', ?2,'%')) " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY count(report) DESC, p.project.createdAt DESC ",

            countQuery = "SELECT count(report) " +
                    "FROM users_project p  " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1 AND  LOWER(p.project.name)  LIKE lower(concat('%', ?2,'%')) " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY count(report) DESC, p.project.createdAt DESC "
    )
    Page<Project> searchAllProjectByUserAndCountTheirReports(String userId, String search, Pageable pageable);

    @Query(
            value = "SELECT new Project(p.project,count(report)) " +
                    "FROM users_project p " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1  " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY p.project.createdAt DESC ",

            countQuery = "SELECT count(report) " +
                    "FROM users_project p  " +
                    "LEFT JOIN Report as report on p.project.id = report.project.id " +
                    "WHERE p.user.id = ?1  " +
                    "GROUP BY p.project.id, p.project.createdAt " +
                    "ORDER BY p.project.createdAt DESC "
    )
    Page<Project> findLast3ProjectByUser_IdAndCountTheirReports(String userId, Pageable pageable);


}
