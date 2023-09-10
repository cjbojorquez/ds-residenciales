/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.com.ds.util;

import io.jsonwebtoken.Claims;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author cjbojorquez
 */
@Slf4j
public class Tools {

    @Autowired
    private static EmailService emailService;

    private static final String SECRET_KEY = "c1@v353CrE74";


    @Autowired
    public Tools(EmailService emailService) {
        this.emailService = emailService;
    }

//    public static void main(String[] args) {
//        System.out.println("Pass 123" + encriptarPassword("123"));
//    }
    
    public static String newName(String nombre) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();

        Random random = new Random();
        int numeroRandom = random.nextInt(100) + 1; // Esto generará números entre 1 y 100

        String nuevoNombre = formatter.format(date) + "_" + numeroRandom + "_" + nombre;
        return nuevoNombre;
    }

    public static Date now() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date = new Date();
        Date fecha = null;
        try {
            fecha = formatter.parse(date.toString());
        } catch (ParseException ex) {
            //SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            log.info("error en get fecha" + ex);
        }
        log.info("La fecha es " + date.toString() + " " + fecha.toString());
        return fecha;
    }

    public static Date getFecha(String fecha, String hora) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();

        try {
            date = formatter.parse(fecha + " " + hora);
        } catch (ParseException ex) {
            //SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            log.info("error en get fecha" + ex);
        }
        log.info("La fecha es " + date.toString());
        return date;
    }

    public static String formateaFecha(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        String date = formatter.format(fecha);
        return date;
    }
    
    public static boolean cumplePatron(String patron, String cadena) {

        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadena);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String encriptarPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    
   public static String encodeTokenForURL(String token) {
    try {
        return URLEncoder.encode(token, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
}
   
 public static String decodeTokenFromURL(String encodedToken) {
    try {
        return URLDecoder.decode(encodedToken, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
}  

    public static String generarToken(String idParametro, Long expiracionMs) {
        Date now = new Date();
        log.info("idParametro = " + idParametro + ":"+expiracionMs );
        Date expirationDate = new Date(now.getTime() + expiracionMs);
        System.out.println("idParametro = " + idParametro);
        log.info("idParametro = " + idParametro +" expiracion en "+expirationDate);
        String token = Jwts.builder()
                .setSubject(idParametro)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .compact();
        System.out.println("token = " + token);
        return token;
    }

    public static String paginaSegura(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            String idParametro = claims.getSubject();
            // Realiza la lógica de tu página segura aquí
            return idParametro;
        } catch (Exception e) {
            // El token no es válido
            return "errores/401";
        }
    }

//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("cesar970@gmail.com");
//        mailSender.setPassword("Joelcesar123");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
}
