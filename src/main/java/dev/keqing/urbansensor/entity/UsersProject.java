package dev.keqing.urbansensor.entity;

import javax.persistence.*;

@Table
@Entity(name = "users_project")
public class UsersProject {

    @EmbeddedId
    UsersProjectKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    Project project;

    public UsersProjectKey getId() {
        return id;
    }

    public void setId(UsersProjectKey id) {
        this.id = id;
    }

    public Users getUsers() {
        return user;
    }

    public void setUsers(Users users) {
        this.user = users;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
