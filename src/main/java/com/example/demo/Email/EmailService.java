package com.example.demo.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void MailSender(String code, String emailAddress, String userName ){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailAddress);
        message.setFrom("newpersonwithoutname@gmail.com");
        message.setSubject("Подтверждение авторизации");
        message.setText(String.format("Доброго времени суток, %s! \n"
                            + "Для подтвержения вашей почты на сервисе PastryShop перейдите по сслыке: http://localhost:7777/activate/%s ", userName, code));

        javaMailSender.send(message);
    }
}
