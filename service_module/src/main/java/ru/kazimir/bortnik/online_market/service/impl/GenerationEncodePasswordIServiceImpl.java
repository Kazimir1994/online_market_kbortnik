package ru.kazimir.bortnik.online_market.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.service.GenerationEncodePassword;

import java.util.Random;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.DEFAULT_SIZE_OF_PASSWORDS;

@Service
public class GenerationEncodePasswordIServiceImpl implements GenerationEncodePassword {
    private final PasswordEncoder passwordEncoder;

    public GenerationEncodePasswordIServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final int DEFAULT_SIZE_OF_PASSWORD = DEFAULT_SIZE_OF_PASSWORDS;
    private final int DEFAULT_START_POINT_ASCII = 67;
    private final int DEFAULT_END_POINT_ASCII = 124;

    @Override
    public String getEncryptPassword(int sizeOfPassword, int startPointASCII, int endPointASCII) {
        return passwordEncoder.encode(new Random().ints(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII).
                mapToObj(i -> String.valueOf((char) i)).
                collect(Collectors.joining()));
    }

    @Override
    public String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII) {
        return new Random().ints(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII).
                mapToObj(i -> String.valueOf((char) i)).
                collect(Collectors.joining());
    }

    @Override
    public String getPassword() {
        return getPassword(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII);
    }

    @Override
    public String getEncryptPassword() {
        return getEncryptPassword(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII);
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean comparePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
