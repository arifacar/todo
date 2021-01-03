package com.arifacar.domain.model.todo;

import com.arifacar.domain.model.BaseEntity;
import com.arifacar.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class TodoItem extends BaseEntity implements Serializable {

    private String title;

    @Type(type = "org.hibernate.type.MaterializedClobType")
    @Lob
    private String detail;

    @JsonIgnore
    @OneToOne
    private User user;

    private Timestamp dueDate;

    private Boolean completed;

    private Integer inOrder;

}
