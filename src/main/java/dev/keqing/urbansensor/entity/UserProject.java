package dev.keqing.urbansensor.entity;

import javax.persistence.*;

@Table
@Entity(name = "users_project")
public class UserProject {

    @EmbeddedId
    UserProjectKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    Project project;

    public UserProject() {
    }

    public UserProject(User user, Project project) {
        this.user = user;
        this.project = project;
        UserProjectKey userProjectKey = new UserProjectKey();
        userProjectKey.setProjectId(project.getId());
        userProjectKey.setUserId(user.getId());
        this.id = userProjectKey;
    }

    public UserProjectKey getId() {
        return id;
    }

    public void setId(UserProjectKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
