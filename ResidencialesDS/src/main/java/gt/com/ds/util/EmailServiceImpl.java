package gt.com.ds.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 *
 * @author cjbojorquez
 */
@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.sender}")
    private String emailUser;

    @Override
    public void sendSimpleMessage(
            String to, String subject, String text, String origin) {
        /*SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("cesar970@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        mailSender.send(message);*/

        System.out.println("Intento de envio de correo");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(origin);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
            System.out.println("correo enviado con descripcion : " + text);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            System.out.println("ex = " + ex);
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendMessage(String to, String subject, String message, File file, String origin) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(origin);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, true);

            if (file != null) {
                boolean isImage = file.getName().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$");

                if (isImage) {
                    // Si es una imagen, incrusta la imagen en el mensaje
                    mimeMessageHelper.addInline("imagen", file);
                } else {
                    // Si no es una imagen, adjunta el archivo
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
                mimeMessageHelper.addAttachment(file.getName(), file);
            }

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
