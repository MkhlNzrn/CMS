package ru.abolsoft.core.account;

import org.springframework.stereotype.Service;

@Service
public interface EmailSender {


    public void sendSuccessRegistrationEmail(String toEmail);
}


