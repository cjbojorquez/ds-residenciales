package gt.com.ds.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author cjbojorquez
 */
@Configuration
public class MailConfiguration {

    @Value("${email.sender}")
    private String emailUser;
    
    @Value("${email.host}")
    private String host;
    
    @Value("${email.port}")
    private int port;
    
    @Value("${email.username}")
    private String userName;
    
    @Value("${email.password}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(userName);
        mailSender.setPassword(password);
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls","true");
        props.put("mail.smtp.quitwait","false");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
