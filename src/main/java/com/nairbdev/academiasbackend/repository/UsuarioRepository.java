package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByAuth0Sub(String auth0Sub);

    List<Usuario> findByAcademiaId(Long academiaId);
    Optional<Usuario> findByIdAndActivoTrue(Long id);

}
