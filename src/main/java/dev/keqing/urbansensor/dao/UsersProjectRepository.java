package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.UsersProject;
import dev.keqing.urbansensor.entity.UsersProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersProjectRepository extends JpaRepository<UsersProject,UsersProjectId> {
}
