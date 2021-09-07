package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Project;
import dev.keqing.urbansensor.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,String> {
}
