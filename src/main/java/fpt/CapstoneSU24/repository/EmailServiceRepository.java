package fpt.CapstoneSU24.repository;

import fpt.CapstoneSU24.dto.DataMailDTO;
import jakarta.mail.MessagingException;

//@Service
//public class EmailService
//{
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendSimpleEmail(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        mailSender.send(message);
//    }
public interface EmailServiceRepository {
    void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException;

    //String generateOTP() throws MessagingException;
}

