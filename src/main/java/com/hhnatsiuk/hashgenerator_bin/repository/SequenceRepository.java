package com.hhnatsiuk.hashgenerator_bin.repository;

import com.hhnatsiuk.hashgenerator_bin.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, Long> {
}
