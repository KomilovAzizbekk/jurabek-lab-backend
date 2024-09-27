package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProfileReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.UserService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Optional;
import java.util.UUID;

@Service("userUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> edit(UUID id, ProfileReqDTO dto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("User not found", HttpStatus.NOT_FOUND)
        );

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getId() != user.getId()) {
            throw RestException.restThrow("You do not have permission to edit this user", HttpStatus.FORBIDDEN);
        }

        Optional.ofNullable(dto.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(user::setLastName);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    public ResponseEntity<?> delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("User not found", HttpStatus.NOT_FOUND)
        );

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getId() != user.getId()) {
            throw RestException.restThrow("You do not have permission to delete this user", HttpStatus.FORBIDDEN);
        }
        user.setDeleted(true);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
    }
}
