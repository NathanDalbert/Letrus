package br.com.letrus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = " Usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario implements UserDetails {

    @Id
    @Column(name = "idUsario", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUsuario;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is obligatory")
    private String name;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is obligatory")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is obligatory")
    private String password;

    private  Usuario(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public static Usuario newUsuario(String name, String email, String password) {
        return new  Usuario(name, email, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
