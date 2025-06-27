package com.metodologia.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "persona")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", unique = true)
    private String username;
    @Column(name = "contrasena")
    private String password;
    @Column(name = "nombre", length = 15)
	private String nombre;
	@Column(name = "apellido", length = 15)
	private String apellido;
	@Column(name = "dni", length = 8)
	private Integer dni;
    @Column(name = "numero")
    private Integer numero;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;
	public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
        .collect(Collectors.toList());
    }
    @OneToMany(mappedBy = "solicitante")
    @JsonIgnore
    private List<ServicioSolicitado> serviciosSolicitados;

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private List<ServicioSolicitado> serviciosBrindados;    
}
