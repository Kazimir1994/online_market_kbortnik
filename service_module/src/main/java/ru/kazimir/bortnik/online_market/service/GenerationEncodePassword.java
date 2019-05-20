package ru.kazimir.bortnik.online_market.service;

public interface GenerationEncodePassword {

    String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII);

    String getPassword();

    String encryptPassword(String password);

    boolean comparePassword(String rawPassword, String encodedPassword);
}
