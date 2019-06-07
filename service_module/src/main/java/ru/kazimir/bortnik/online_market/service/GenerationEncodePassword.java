package ru.kazimir.bortnik.online_market.service;

public interface GenerationEncodePassword {

    String getPassword();

    String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII);

    String encryptPassword(String password);

    boolean comparePassword(String rawPassword, String encodedPassword);
}
