package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findFirstByEmail(String email);
}
