package ru.kazimir.bortnik.online_market.service;

public interface GenerationRandomEncodePassword {
    String getPassword(int sizeOfPassword, int startPointASCII, int endPointASCII);
}
