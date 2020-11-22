package com.arifacar.domain.model.user;

import com.arifacar.domain.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

@Entity
@Table(name = "T_USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends BaseEntity implements Serializable {

    private String name;

    private String surname;

    @Column(unique = true)
    private String username;

    private String profilePic;

    private String email;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isEmailVerified;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Lob
    private String profileStatus;

    private boolean isActive;

    @Column(nullable = false)
    private boolean privateProfile;

    @CreatedDate
    private Timestamp createDate;

    public void setUsername(String username) {
        this.username = username.toLowerCase(Locale.ENGLISH);
    }

    public User(Long id) {
        this.setId(id);
    }
}
