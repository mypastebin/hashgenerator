package com.hhnatsiuk.hashgenerator_bin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hash_sequence")
@Data
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
