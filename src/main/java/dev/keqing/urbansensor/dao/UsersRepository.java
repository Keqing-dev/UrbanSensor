package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,String> {
    Optional<Users> findFirstByEmail(String email);
}
