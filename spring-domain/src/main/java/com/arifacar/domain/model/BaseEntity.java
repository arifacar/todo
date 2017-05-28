package com.arifacar.domain.model;

import javax.persistence.*;

/**
 * Entity sınıflarınızda orta özellikler var ise bu özellikleri her
 * Entity class'ına ayrı olarak yazmak yerine super bir class'a yazabilirsiniz.
 *
 * @MappedSuperClass notasyonuna kullanıldığı sınıflar entitiy olarak sayılmazlar ,
 * veritabanında tablo olarak karşılıkları yoktur.
 */

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITY_SEQ")
    @SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "ENTITY_SEQUENCE", initialValue = 1, allocationSize = 10)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
