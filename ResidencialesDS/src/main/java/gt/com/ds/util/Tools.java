/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.com.ds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author cjbojorquez
 */
@Slf4j
public class Tools {
    
    public static String newName(String nombre){
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        
        Random random = new Random();
        int numeroRandom = random.nextInt(100) + 1; // Esto generará números entre 1 y 100
        
        String nuevoNombre=formatter.format(date)+"_"+numeroRandom+"_"+nombre;
        return nuevoNombre;
    }
    
    public static Date now(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date = new Date();
        Date fecha = null;
        try {
            fecha = formatter.parse(date.toString());
        } catch (ParseException ex) {
            //SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            log.info("error en get fecha" + ex);
        }
        log.info("La fecha es " + date.toString()+ " "+ fecha.toString());
        return fecha;
    }
    
    public static Date getFecha(String fecha, String hora){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        
        try {
            date = formatter.parse(fecha+" "+hora);
        } catch (ParseException ex) {
            //SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            log.info("error en get fecha" + ex);
        }
        log.info("La fecha es " + date.toString());
        return date;
    }
    
    public static boolean cumplePatron(String patron,String cadena){

        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadena);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static String encriptarPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    
    
}
