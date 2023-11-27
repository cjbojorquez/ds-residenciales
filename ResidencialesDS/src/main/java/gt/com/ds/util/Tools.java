package gt.com.ds.util;

import gt.com.ds.web.ControladorUsuario;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author cjbojorquez
 * 
 * La clase Tools es una clase estatica para facilitar el llamado de la misma 
 * dentro de todo el proyecto.
 */
@Slf4j
public class Tools {

    @Autowired
    private static EmailService emailService;

    private static final String SECRET_KEY = "c1@v353CrE74";

    @Value("${static.ruta}")
    static String stRuta;

    @Autowired
    public Tools(EmailService emailService) {
        this.emailService = emailService;
    }

//    public static void main(String[] args) {
//        System.out.println("Pass 123" + encriptarPassword("123"));
//    }
    
    /**
     * 
     * Esta función nos permite generar un nuevo nombre para los archivos adjuntados,
     * los archivos se renombran en base a un numero random seguido por la fecha de carga
     * y el nombre original del archivo.
     * 
     * @param nombre este parametro recibe el nombre original del archivo
     * @return se retorna el nuevo nombre del archivo con el cual se almacenara en el servidor
     */
    public static String newName(String nombre) {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();

        Random random = new Random();
        int numeroRandom = random.nextInt(100) + 1; // Esto generará números entre 1 y 100

        String nuevoNombre = formatter.format(date) + "_" + numeroRandom + "_" + nombre;
        return nuevoNombre;
    }

    /**
     * Esta función nos permite obtener la fecha actual
     * 
     * @return  retorna la fecha actual con el siguiente formato "EEE MMM dd HH:mm:ss zzz yyyy"
     */
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

    /**
     * Esta función nos permite unificar una fecha y una hora para convertirla en una 
     * fecha del tipo fecha y hora
     * 
     * @param fecha recibe una fecha en formato dd/MM/yyyy
     * @param hora  recibe una hora en formato HH:mm
     * 
     * @return unifica los dos campos y los unifica en una fecha con formato dd/MM/yyyy HH:mm
     */
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

    /**
     * Recibe una fecha tipo Date y se le da un formato específico
     * @param fecha campo de tipo Date que recibe una fecha
     * @return 
     */
    public static String formateaFecha(Date fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        String date = formatter.format(fecha);
        return date;
    }
    
    /**
     * Esta función se utiliza para validar si la cadena provista cumple con un patron en específico.
     * @param patron aqui se coloca un patrón, el cual se buscara dentro de la cadena.
     * @param cadena este es la cadena sobre la cual se validará si cumple con el patron provisto.
     * @return el resultado de la funcion será true o false, resultado que indicará si se cumple o no con el 
     * patron.
     */
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
   
   /**
    * Esta función se utiliza para codificar el token que se enviara a los usuarios 
    * al momento de enviarseles la invitación, o cuando quieran reestablecer su contraseña 
    * esto para evitar que dentro del token vayan caracteres invalidos.
    * 
    * @param token este campo lleva el valor del token que se le enviará al usuario.
    * @return  retorna el valor del token codificado.
    */ 
   public static String encodeTokenForURL(String token) {
    try {
        return URLEncoder.encode(token, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return null;
    }
}

/**
 * 
 * @param encodedToken
 * @return 
 */   
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
    
    
    public static String saveArchivo(String stArchivo,MultipartFile imagen,String ubicacion){
       
        if (!imagen.isEmpty()) {
            String ruta= ubicacion;
            log.info(ruta);
            Path directorioImagenes = Paths.get(ruta);

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                //Path rutaCompleta = Paths.get("src//main//resources//static//images//perfil//" + nombreArchivo);
                
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                //Files.write(rutaCompleta, byteImg);
                Files.write(rutaCompleta, byteImg);
                return stArchivo + nombreArchivo;
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }

        }
        return"";
    }

    public static String obtenerNombreArchivo(String ruta) {
        // Encuentra el índice del último carácter '/' en la ruta
        int indiceUltimaBarra = ruta.lastIndexOf('/');

        // Si se encuentra la barra, extrae el substring después de la última barra
        if (indiceUltimaBarra != -1) {
            return ruta.substring(indiceUltimaBarra + 1);
        } else {
            // Si no se encuentra la barra, devuelve la ruta completa
            return ruta;
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
