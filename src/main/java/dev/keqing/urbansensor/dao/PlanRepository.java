package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,String> {


}
