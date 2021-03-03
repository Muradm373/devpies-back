package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.core.rest.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;


    public static int noOfQuickServiceThreads = 20;

    private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).

    public void sendASynchronousMail(String from, String toEmail,String subject,String text) throws MailException,RuntimeException{
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(text);
        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    emailSender.send(mail);
                }catch(Exception e){
                    System.out.println("Exception occur while send a mail : "+ e);
                }
            }
        });
    }

    @Override
    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("devpiesmedipack@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

}