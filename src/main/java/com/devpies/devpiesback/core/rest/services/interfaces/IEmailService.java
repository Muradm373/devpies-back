package com.devpies.devpiesback.core.rest.services.interfaces;

public interface IEmailService {
    public void sendSimpleMessage(
            String to, String subject, String text);
}
