package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.Report;
import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.projection.ReportFile;
import dev.keqing.urbansensor.projection.ReportSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report,String> {

    <T> Page<T> findAllByUser(User user, Pageable pageable, Class<T> type);

    <T> Optional<T> findByIdAndUser(String id, User user, Class<T> type);

    List<ReportFile> findAllByProject_Id(String projectId);

    <T> Page<T> findAllByProject_IdOrderByTimestampDesc(String projectId,Pageable pageable, Class<T> type);



//    <T> Page<T> findAllByProject_IdOrderByTimestampDesc(String projectId,Pageable pageable, Class<T> type);

    <T> List<T> findAllByProject_IdAndTimestampBetweenOrderByTimestampDesc(String projectId, LocalDateTime inDate, LocalDateTime endDate, Class<T> type);




    @Transactional
    void deleteAllByProject_Id(String projectId);

    void deleteReportsByProject_Id(String projectId);


}
