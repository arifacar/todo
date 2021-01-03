package com.arifacar.domain.model.user;

import com.arifacar.domain.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "T_USER_LOGIN_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class LoginInfo extends BaseEntity implements Serializable {

    private Long userId;

    private String authToken;

    private String deviceInfo;

    private Timestamp date;
}

