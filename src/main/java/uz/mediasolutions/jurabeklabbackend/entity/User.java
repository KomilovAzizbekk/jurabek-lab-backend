package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsUUID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@DynamicInsert
@DynamicUpdate
public class User extends AbsUUID implements UserDetails {

    @Column
    private String phoneNumber;

    private String otp;

    private LocalDateTime lastOtpTime;

    private String firstName;

    private String lastName;

    private String password;

    private String username;

    private boolean registered;

    private BigDecimal balance;

    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    private String language;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
