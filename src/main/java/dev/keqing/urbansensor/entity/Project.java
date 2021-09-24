package dev.keqing.urbansensor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    Set<UserProject> userProjects;

    @OneToMany(mappedBy = "project")
    Set<Report> reports;


    /**
     * --- TRANSIENTS ---
     **/
    @Transient
    @Column(table = "reportsCount")
    private Long reportsCount = 0L;


    public Project(Project p, Long reportsCount) {
        this.id = p.getId();
        this.name = p.getName();
        this.createdAt = p.getCreatedAt();
        this.reportsCount = reportsCount;
    }

    public Project() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getReportsCount() {
        return  NumberFormat.getNumberInstance(Locale.GERMAN).format(reportsCount);
    }

    public void setReportsCount(Long reportsCount) {
        this.reportsCount = reportsCount;
    }

    public static class Create {
        @NotNull
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    public static class Update {
        @NotNull
        private String id;
        @NotNull
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
