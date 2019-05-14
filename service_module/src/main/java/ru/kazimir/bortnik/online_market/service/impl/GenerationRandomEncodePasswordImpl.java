package ru.kazimir.bortnik.online_market.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.service.GenerationRandomEncodePassword;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GenerationRandomEncodePasswordImpl implements GenerationRandomEncodePassword {
    private final PasswordEncoder passwordEncoder;

    public GenerationRandomEncodePasswordImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private final int DEFAULT_SIZE_OF_PASSWORD = 10;
    private final int DEFAULT_START_POINT_ASCII = 66;
    private final int DEFAULT_END_POINT_ASCII = 142;

    @Override
    public String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII) {
        return passwordEncoder.encode(new Random().ints(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII).
                mapToObj(i -> String.valueOf((char) i)).
                collect(Collectors.joining()));
    }

    @Override
    public String getPassword() {
        return getPassword(DEFAULT_SIZE_OF_PASSWORD, DEFAULT_START_POINT_ASCII, DEFAULT_END_POINT_ASCII);
    }
}
