package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uz.mediasolutions.jurabeklabbackend.entity.SmsToken;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.repository.SmsTokenRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${eskiz.account.email}")
    private String email;

    @Value("${eskiz.account.password}")
    private String password;

    @Value("${eskiz.api.login.url}")
    private String loginUrl;

    @Value("${eskiz.api.refresh.url}")
    private String refreshUrl;

    @Value("${eskiz.api.sms.url}")
    private String smsUrl;

    private final RestTemplate restTemplate;
    private final SmsTokenRepository tokenRepository;


    public void obtainToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("email", email);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, TokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String token = Objects.requireNonNull(response.getBody()).getData().getToken();
            tokenRepository.save(SmsToken.builder()
                    .token(token)
                    .build());
        }
    }

    public void refreshToken() {
        SmsToken smsToken = tokenRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Token not found", HttpStatus.NOT_FOUND)
        );

        String oldToken = smsToken.getToken();
        String newToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(oldToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(refreshUrl, HttpMethod.PATCH, requestEntity, TokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            newToken = Objects.requireNonNull(response.getBody()).getData().getToken();
            smsToken.setToken(newToken);
            tokenRepository.save(smsToken);
        }
    }

    public String sendSms(String mobilePhone, String message, String from, String callbackUrl) {
        SmsToken smsToken = tokenRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Token not found", HttpStatus.NOT_FOUND)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(smsToken.getToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("mobile_phone", mobilePhone);
        body.add("message", message);
        body.add("from", from);
        if (callbackUrl != null) {
            body.add("callback_url", callbackUrl);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<SmsResponse> response = restTemplate.exchange(smsUrl, HttpMethod.POST, requestEntity, SmsResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return Objects.requireNonNull(response.getBody()).getId();
        } else {
            throw new RuntimeException("Failed to send SMS: " + response.getStatusCode());
        }
    }

    @Getter
    @Setter
    public static class SmsResponse {
        private String id;
        private String message;
        private String status;
    }

    @Getter
    @Setter
    public static class TokenResponse {
        private String message;
        private TokenData data;
        private String tokenType;


        @Getter
        @Setter
        public static class TokenData {
            private String token;
        }
    }
}