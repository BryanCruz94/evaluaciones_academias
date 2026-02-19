package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "usuario",
        indexes = {
                @Index(name = "idx_usuario_academia", columnList = "academia_id")
        }
)
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth0_sub", nullable = false, unique = true, length = 255)
    private String auth0Sub;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 150)
    private String nombres;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "rol_usuario")
    private RolUsuario rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academia_id")
    private Academia academia;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public String getAuth0Sub() { return auth0Sub; }
    public String getEmail() { return email; }
    public String getNombres() { return nombres; }
    public RolUsuario getRol() { return rol; }
    public Academia getAcademia() { return academia; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setAuth0Sub(String auth0Sub) { this.auth0Sub = auth0Sub; }
    public void setEmail(String email) { this.email = email; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
