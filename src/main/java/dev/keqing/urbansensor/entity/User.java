package dev.keqing.urbansensor.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.keqing.urbansensor.utils.ToLowerCaseConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Table
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, unique = true)
    private String id;

    @JsonDeserialize(converter = ToLowerCaseConverter.class)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String profession;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(unique = true)
    private String googleId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String avatar;

    @OneToMany(mappedBy = "user")
    Set<UserProject> userProjects;

    @OneToMany(mappedBy = "user")
    Set<Report> reports;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;


    /** --- TRANSIENTS --- **/

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Transient
    private String token;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
