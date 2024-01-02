package com.apirest.apirest.model;

import jakarta.persistence.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @Email(message = "Formato de email incorrecto :C")
    @NotBlank
    @Size(max = 80)
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Este campo debe ser ingresado")
    @Size(max = 30, message = "Solo puede tener max 30 caracteres")
    private String username;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{4,}$",
            message = "La contraseña debe contener al menos una mayúscula, letras minúsculas y dos números"
    )
    @Size(min = 4, message = "La longitud mínima de la contraseña es 4 caracteres")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    @Column(name = "created")
    @Temporal(TemporalType.DATE)
    private Date created;

    @PrePersist
    public void prePersist(){
        created = new Date();
        modified = new Date();
    }

    @Column(name = "modified")
    private Date modified;

    @Column(name = "token")
    private String token;

    @Column(name = "isActive")
    private Boolean isActive = false;
}
