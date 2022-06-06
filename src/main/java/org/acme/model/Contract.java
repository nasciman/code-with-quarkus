package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Contract extends PanacheEntity {

    @Column(length = 200)
    public String description;
}
