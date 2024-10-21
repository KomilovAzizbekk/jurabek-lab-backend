package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Constants;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.AdminDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.UserDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.AdminReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.MeResDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ConstantsRepository;
import uz.mediasolutions.jurabeklabbackend.repository.NotificationRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.UserService;
import uz.mediasolutions.jurabeklabbackend.utills.CommonUtils;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Optional;
import java.util.UUID;

@Service("adminUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationRepository notificationRepository;
    private final ConstantsRepository constantsRepository;

    @Override
    public ResponseEntity<?> getAllUsers(int page, int size, String search) {
        Page<UserDTO> allUsers = userRepository.getAllUsers(PageRequest.of(page, size), search);
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<?> getAllAdmins(int page, int size, String search) {
        Page<AdminDTO> allAdmins = userRepository.getAllAdmins(PageRequest.of(page, size), search);
        return ResponseEntity.ok(allAdmins);
    }

    @Override
    public ResponseEntity<?> addAdmin(AdminReqDTO dto) {
        if (userRepository.existsByUsernameAndDeletedFalse(dto.getUsername())) {
            throw RestException.restThrow("Admin already exists with this username", HttpStatus.CONFLICT);
        }
        if (dto.getRole().equals(RoleName.ROLE_USER.name())) {
            throw RestException.restThrow("You cannot create a user", HttpStatus.BAD_REQUEST);
        }
        try {
            User admin = User.builder()
                    .username(dto.getUsername())
                    .deleted(false)
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .role(RoleName.valueOf(dto.getRole()))
                    .build();
            userRepository.save(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
        } catch (Exception e) {
            throw RestException.restThrow(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> editAdmin(UUID id, AdminReqDTO dto) {
        // Foydalanuvchini topish yoki Exception fırlatish
        User user = userRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Admin not found", HttpStatus.NOT_FOUND)
        );

        // Username o'zgartirish: Optional va qo'shimcha validatsiya bilan
        Optional.ofNullable(dto.getUsername()).ifPresent(username -> {
            if (userRepository.existsByUsernameAndNotId(username, id)) {
                throw RestException.restThrow("Admin already exists with this username", HttpStatus.CONFLICT);
            }
            user.setUsername(username);
        });

        // Parolni yangilash: Optional bilan
        Optional.ofNullable(dto.getPassword()).ifPresent(password ->
                user.setPassword(passwordEncoder.encode(password))
        );

        // Rolni yangilash: Optional bilan
        Optional.ofNullable(dto.getRole()).ifPresent(role ->
                user.setRole(RoleName.valueOf(role))
        );

        // Foydalanuvchini saqlash
        userRepository.save(user);

        // Javob qaytarish
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }


    @Override
    public ResponseEntity<?> deleteAdmin(UUID id) {
        User user = (User) CommonUtils.getUserFromSecurityContext();

        User toBeDeletedUser = userRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Admin not found", HttpStatus.NOT_FOUND)
        );

        assert user != null;

        if (toBeDeletedUser.getRole().equals(RoleName.ROLE_SUPER_ADMIN) &&
                toBeDeletedUser.getCreatedAt().toInstant().isBefore(user.getCreatedAt().toInstant())) {
            throw RestException.restThrow("You cannot delete older SUPER_ADMIN than you", HttpStatus.CONFLICT);
        }

        toBeDeletedUser.setDeleted(true);
        userRepository.save(toBeDeletedUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
    }

    @Override
    public ResponseEntity<?> getMe() {
        User user = (User) CommonUtils.getUserFromSecurityContext();
        assert user != null;

        int notifications = notificationRepository.countAllByUserIdAndViewedFalse(user.getId());

        Constants constants = constantsRepository.findById(1L).orElse(null);

        MeResDTO me = MeResDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .notifications(notifications)
                .version(constants != null ? constants.getVersion() : null)
                .build();

        return ResponseEntity.ok(me);
    }
}

