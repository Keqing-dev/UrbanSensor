package dev.keqing.urbansensor.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Entity(name = "users_project")
public class UsersProject {

    @EmbeddedId
    UsersProjectId id;

    @ManyToOne
    @MapsId("usersId")
    @JoinColumn(name = "users_id")
    private Users user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    public UsersProjectId getId() {
        return id;
    }

    public void setId(UsersProjectId id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
