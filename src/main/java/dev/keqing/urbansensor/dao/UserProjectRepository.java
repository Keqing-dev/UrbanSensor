package dev.keqing.urbansensor.dao;

import dev.keqing.urbansensor.entity.User;
import dev.keqing.urbansensor.entity.UserProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, String> {

    List<UserProject> findFirst3ByUser_IdOrderByProject_CreatedAtDesc(String id);

    Optional<UserProject> findByProject_Id(String id);

    Optional<List<UserProject>> findAllByProject_Id(String id);

    @Transactional
    void deleteAllByProject_Id(String id);

    Page<UserProject> findAllByUser_IdOrderByProject_CreatedAtDesc(String id, Pageable pageable);


    Page<UserProject> findAllByProject_NameContainsIgnoreCaseAndUser(String name, User id, Pageable pageable);


}
