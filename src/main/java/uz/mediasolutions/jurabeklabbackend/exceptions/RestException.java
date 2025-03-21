package uz.mediasolutions.jurabeklabbackend.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import uz.mediasolutions.jurabeklabbackend.manual.ErrorData;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestException extends RuntimeException {

    private String userMsg;
    private HttpStatus status;

    private String resourceName;
    private String fieldName;
    private Object fieldValue;
    private List<ErrorData> errors;
    private Integer errorCode;

    private RestException(String resourceName, String fieldName, Object fieldValue, String userMsg) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.userMsg = userMsg;
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = Rest.NO_ITEMS_FOUND;
    }

    private RestException(String resourceName, String fieldName, Object fieldValue, String userMsg, HttpStatus status) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.userMsg = userMsg;
        this.status = status;
    }

    public RestException(String userMsg, HttpStatus status) {
        super(userMsg);
        this.userMsg = userMsg;
        this.status = status;
    }

    private RestException(String userMsg, double fieldValue, HttpStatus status) {
        super(userMsg);
        this.userMsg = userMsg;
        this.fieldValue = fieldValue;
        this.status = status;
    }

    private RestException(String userMsg, int errorCode, HttpStatus status) {
        super(userMsg);
        this.errors = Collections.singletonList(new ErrorData(userMsg, errorCode));
        this.userMsg = userMsg;
        this.status = status;
    }

    private RestException(HttpStatus status, List<ErrorData> errors) {
        this.status = status;
        this.errors = errors;
    }

    public static RestException restThrow(String userMsg, HttpStatus httpStatus) {
        return new RestException(userMsg, httpStatus);
    }

    public static RestException restThrow(String userMsg, double fieldValue, HttpStatus httpStatus) {
        return new RestException(userMsg, fieldValue, httpStatus);
    }

    public static RestException restThrow(String resourceName, String fieldName, Object fieldValue, String userMsg) {
        return new RestException(resourceName, fieldName, fieldValue, userMsg);
    }

    public static RestException restThrow(String resourceName, String fieldName, Object fieldValue, String userMsg, HttpStatus status) {
        return new RestException(resourceName, fieldName, fieldValue, userMsg, status);
    }

    public static RestException restThrow(List<ErrorData> errors, HttpStatus status) {
        return new RestException(status, errors);
    }

    public static RestException restThrow(String userMsg, int errorCode, HttpStatus httpStatus) {

        return new RestException(userMsg, errorCode, httpStatus);
    }

    /**
     * @param resourceKey - {@link org.springframework.context.MessageSource} bo'yicha kelishi kerak. Masalan "GROUP"
     * @return Guruh topilmadi!
     */
    public static RestException notFound(String resourceKey) {
        return new RestException(
                "Not found" + resourceKey,
                HttpStatus.NOT_FOUND
        );
    }
}
