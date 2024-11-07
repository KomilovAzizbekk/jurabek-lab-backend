package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uz.mediasolutions.jurabeklabbackend.entity.SmsToken;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.res.SmsTokenResDTO;
import uz.mediasolutions.jurabeklabbackend.repository.SmsTokenRepository;

import java.sql.Timestamp;
import java.util.Map;
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
            if (tokenRepository.existsById(1L)) {
                SmsToken smsToken = tokenRepository.findById(1L).get();
                smsToken.setToken(token);
                smsToken.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                tokenRepository.save(smsToken);
            } else {
                tokenRepository.save(
                        SmsToken.builder()
                                .token(token)
                                .updatedAt(new Timestamp(System.currentTimeMillis()))
                                .build());
            }
        }
    }

    // Har 2 kunda bajariladi (millisekundlarda 29 kun = 29 * 24 * 60 * 60 * 1000 ms)
    @Scheduled(fixedRate = 2505600000L) // 29 kun
    @Scheduled(fixedRate = 172800000L) // 2 kun
    public void autoRefreshToken() {
        System.out.println("Tokenni avtomatik yangilash jarayoni boshlandi.");
        try {
            refreshToken();
            System.out.println("Token muvaffaqiyatli yangilandi.");
        } catch (Exception e) {
            System.err.println("Tokenni yangilashda xatolik: " + e.getMessage());
        }
    }


    public RestTemplate createRestTemplate() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

    public void refreshToken() {
        SmsToken smsToken = tokenRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Token not found", HttpStatus.NOT_FOUND)
        );

        String currentToken = smsToken.getToken();

        RestTemplate template = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentToken);  // hozirgi tokenni almashtiring

        // So'rovni yuboring
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<SmsTokenResDTO> response = template.exchange(refreshUrl, HttpMethod.PATCH, requestEntity, SmsTokenResDTO.class);
        currentToken = Objects.requireNonNull(response.getBody()).getData().getToken();  // Yangi tokenni saqlash
        smsToken.setToken(currentToken);
        tokenRepository.save(smsToken);
    }

    public HttpStatusCode sendSms(String mobilePhone, String message, String from, String callbackUrl) {
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

        try {
            ResponseEntity<SmsResponse> response = restTemplate.exchange(smsUrl, HttpMethod.POST, requestEntity, SmsResponse.class);
            return response.getStatusCode();
        } catch (HttpClientErrorException.Unauthorized e) {
            refreshToken();
            return sendSms(mobilePhone, message, from, callbackUrl);
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