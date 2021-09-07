package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,String> {
}
