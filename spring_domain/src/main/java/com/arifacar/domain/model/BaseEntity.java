package com.arifacar.domain.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Entity sınıflarınızda orta özellikler var ise bu özellikleri her
 * Entity class'ına ayrı olarak yazmak yerine super bir class'a yazabilirsiniz.
 *
 * @MappedSuperClass notasyonuna kullanıldığı sınıflar entitiy olarak sayılmazlar ,
 * veritabanında tablo olarak karşılıkları yoktur.
 */

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
