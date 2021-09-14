package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.projection.ReportFile;
import dev.keqing.urbansensor.projection.ReportSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,String> {

    Page<ReportSummary> findAllByUser(User user, Pageable pageable);

    Optional<Report> findByIdAndUser(String id, User user);

    List<ReportFile> findAllByProject_Id(String projectId);

    Page<ReportSummary> findAllByProject_IdOrderByTimestampDesc(String projectId,Pageable pageable);

    @Transactional
    void deleteAllByProject_Id(String projectId);

    void deleteReportsByProject_Id(String projectId);
}
