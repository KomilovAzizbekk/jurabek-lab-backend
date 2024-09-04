package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsUUID;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends AbsUUID implements UserDetails {

    @Column(unique = true)
    private String phoneNumber;

    private String otp;

    private String firstName;

    private String lastName;

    private String password;

    private String username;

    private boolean registered;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    private String language;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
