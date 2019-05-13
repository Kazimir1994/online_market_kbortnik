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

    @Override
    public String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII) {
        return passwordEncoder.encode(new Random().ints(sizeOfPassword, startPointASCII, endPointASCII).
                mapToObj(i -> String.valueOf((char) i)).
                collect(Collectors.joining()));
    }
}
