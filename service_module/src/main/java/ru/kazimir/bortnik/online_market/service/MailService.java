package ru.kazimir.bortnik.online_market.service;

public interface MailService {

    void sendMessage(String recipient, String subject, String message);
}
