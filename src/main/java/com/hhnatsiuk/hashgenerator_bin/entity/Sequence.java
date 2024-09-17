package com.hhnatsiuk.hashgenerator_bin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hash_sequence")
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
