package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "academia")
public class Academia extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 50, unique = true)
    private String codigo;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
