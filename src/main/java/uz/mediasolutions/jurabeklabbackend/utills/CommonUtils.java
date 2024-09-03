package uz.mediasolutions.jurabeklabbackend.utills;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.secret.JwtAuthFilter;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class CommonUtils {

    private static JwtAuthFilter jwtAuthFilter;

    /**
     * Getting the User from security context
     */
    public static UserDetails getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals("" + authentication.getPrincipal()))
            return null;
        return (UserDetails) authentication.getPrincipal();
    }

    /**
     * Setting the User to security context
     */
    public static void setUserToSecurityContext(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
    }


    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return Optional.of(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }

//    public static User getUserFromRequest() {
//        String token = currentRequest().getHeader(Rest.AUTHORIZATION_HEADER);
//        if (token != null) {
//            User user = jwtAuthFilter.getUserFromBearerToken(token);
//            return user!=null?user:new User();
//        }
//        return new User();
//    }


//    public static AnonymousUser getAnonymousUserFromRequest() {
//        HttpServletRequest httpServletRequest = currentRequest();
//        String cookie = httpServletRequest.getHeader(Rest.COOKIE_KEY);
//
//        //BU XATO KETGANDA FRONTDAN COOKIE MALUMOTLARI OLISH UCHUN  ** api/v1/cookie/data  GET SOROVINI YUBORIB OLISHI KERAK
//        //TODO yes
//        if (cookie == null)
//            throw RestException.restThrow(Message.EMPTY_COOKIE, HttpStatus.BAD_REQUEST);
//
//        Optional<AnonymousUser> optionalAnonymousUser = anonymousUserRepository.findByCookieId(cookie);
//        return optionalAnonymousUser.orElseThrow(() -> RestException.restThrow(Message.EMPTY_COOKIE, HttpStatus.BAD_REQUEST));
//
//    }


//    public static boolean isUser() {
//        String token = currentRequest().getHeader(Rest.AUTHORIZATION_HEADER);
//        return null != jwtAuthFilter.getUserFromBearerToken(token);
//    }


}
