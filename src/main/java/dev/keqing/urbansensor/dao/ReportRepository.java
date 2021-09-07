package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Project;
import dev.keqing.urbansensor.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,String> {
}
