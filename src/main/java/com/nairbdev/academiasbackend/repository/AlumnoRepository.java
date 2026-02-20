package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Alumno;
import com.nairbdev.academiasbackend.entity.EstadoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    // Trae todos los alumnos con academia y programaActual cargados para evitar LazyInitializationException
    @Query("select a from Alumno a join fetch a.academia left join fetch a.programaActual")
    List<Alumno> findAllConAcademia();

    // Trae alumnos por academia y estado (usar ESTE para ACTIVO, BAJA, GRADUADO, etc.)
    // IMPORTANTE: aquí el estado es parámetro ENUM, para que funcione con PostgreSQL enum nativo.
    @Query("""
            select a from Alumno a
            join fetch a.academia
            left join fetch a.programaActual
            where a.academia.id = :academiaId and a.estado = :estado
            """)
    List<Alumno> findPorAcademiaYEstado(@Param("academiaId") Long academiaId,
                                        @Param("estado") EstadoAlumno estado);

    // Trae alumno por id con relaciones cargadas (para editar/ver)
    @Query("""
            select a from Alumno a
            join fetch a.academia
            left join fetch a.programaActual
            where a.id = :id
            """)
    Optional<Alumno> findByIdConTodo(@Param("id") Long id);

    // Trae alumno por academia + cédula (cédula ya no es única global)
    @Query("""
            select a from Alumno a
            join fetch a.academia
            left join fetch a.programaActual
            where a.academia.id = :academiaId and a.cedula = :cedula
            """)
    Optional<Alumno> findByAcademiaIdAndCedula(@Param("academiaId") Long academiaId,
                                               @Param("cedula") String cedula);

    // Verifica duplicado dentro de la misma academia (uq: academia_id + cedula)
    boolean existsByAcademiaIdAndCedula(Long academiaId, String cedula);

    // ✅ Atajo opcional: si todavía quieres un método "findActivosPorAcademia" para legibilidad
    default List<Alumno> findActivosPorAcademia(Long academiaId) {
        return findPorAcademiaYEstado(academiaId, EstadoAlumno.ACTIVO);
    }
}
