package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Academia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademiaRepository extends JpaRepository<Academia, Long> {

    Optional<Academia> findByCodigo(String codigo);
    List<Academia> findByActivoTrue();
}
