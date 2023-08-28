/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.com.ds.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author cjbojorquez
 */
@Slf4j
public class Tools {
    
    public static String newName(String nombre,Long idUsuario){
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        String nuevoNombre=formatter.format(date)+"_"+nombre+"_"+idUsuario;
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
}
