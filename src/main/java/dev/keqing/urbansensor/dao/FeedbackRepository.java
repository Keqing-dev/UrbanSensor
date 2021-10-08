package dev.keqing.urbansensor.dao;


import dev.keqing.urbansensor.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {


}
