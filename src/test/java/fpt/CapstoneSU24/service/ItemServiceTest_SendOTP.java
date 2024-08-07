package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.CurrentOwnerCheckDTO;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.sdi.ClientSdi;
import fpt.CapstoneSU24.exception.LogService;
import fpt.CapstoneSU24.model.Authorized;
import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.repository.ItemLogRepository;
import fpt.CapstoneSU24.repository.ItemRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.cloudinary.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.thymeleaf.context.Context;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest_SendOTP {
    @InjectMocks
    private EmailService emailService; // Thay thế bằng lớp chứa phương thức sendHtmlMail

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private LogService logService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void testSendHtmlMail_Success() throws MessagingException {
        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo("recipient@example.com");
        dataMail.setSubject("Test Subject");
        dataMail.setProps(Collections.singletonMap("key", "value"));

        Context context = new Context();
        context.setVariables(dataMail.getProps());
        String html = "<html><body>Test Email</body></html>";

        when(templateEngine.process("templateName", context)).thenReturn(html);

        emailService.sendHtmlMail(dataMail, "templateName");

        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testSendHtmlMail_Failure() throws MessagingException {
        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo("test@example.com");
        dataMail.setSubject("Test Subject");
        dataMail.setProps(Map.of("key", "value"));

        String templateName = "testTemplate";

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("<html></html>");
        doThrow(new MessagingException("Mail sending failed")).when(mailSender).send(mimeMessage);

        try {
            emailService.sendHtmlMail(dataMail, templateName);
        } catch (MessagingException e) {
            // Handle exception
        }

        verify(mailSender, times(1)).send(mimeMessage);
    }
}