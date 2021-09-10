package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.projection.ReportSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,String> {
    Page<ReportSummary> findAllByUser(User user, Pageable pageable);
    Optional<Report> findByIdAndUser(String id, User user);
}
