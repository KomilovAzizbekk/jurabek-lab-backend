package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.util.UUID;

public interface UserDTO {

    UUID getId();

    String getPhoneNumber();

    String getFirstName();

    String getLastName();

}
