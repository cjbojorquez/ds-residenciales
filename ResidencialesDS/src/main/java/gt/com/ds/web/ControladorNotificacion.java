package gt.com.ds.web;

import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.EstadoTicketService;
import gt.com.ds.servicio.NotificacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;
import java.text.SimpleDateFormat;

import java.util.Date;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import gt.com.ds.servicio.UsuarioService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorNotificacion {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private EstadoTicketService estadoTicketService;

    @Autowired
    private UsuarioService usuarioService;

    private String varNotiGeneral = "G";
    private String varNotiEspecifica = "E";

    @GetMapping("/general")
    public String InicioGeneral(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        var notificaciones = notificacionService.notificacionPorTipo(varNotiGeneral, 1L);//agregar residencial
        log.info("estas son las notificaciones " + notificaciones.toString());
        model.addAttribute("notificaciones", notificaciones);
        return "general";
    }

    @GetMapping("/agregargeneral")
    public String agregarGeneral(Notificacion notificacion, Model model) {

        return "modificargeneral";
    }

    @PostMapping("/guardargeneral")
    public String guardarGeneral(@Valid Notificacion notificacion, @RequestParam("file") MultipartFile imagen, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        Date fecha = null;
        try {
            fecha = formatter.parse(date.toString());
        } catch (ParseException ex) {
            log.info("error en get fecha");
        }
        if (errors.hasErrors()) {
            return "modificargeneral";
        }

        notificacion.setTipo(varNotiGeneral);
        log.info("antes de validar " + notificacion.getIdNotificacion());

        if (notificacion.getIdNotificacion() == null) {
            log.info("\n\n--------------------dentro de validar " + notificacion.getIdNotificacion());

            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us = usuarioService.encontrarUsuario(us);
            notificacion.setUsuario(us);

            notificacion.setFechaCrea(date);
            notificacion.setUsuarioCrea(1L);
            notificacion.setIdResidencial(1L);
            notificacion.setEstado(estadoTicketService.encontrarEstado(1L));
        } else {
            log.info("else de validar " + notificacion.getIdNotificacion());
            notificacion.setFechaModifica(date);
            notificacion.setUsuarioModifica(1L);
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//adjunto");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
                notificacion.setAdjunto("adjunto/" + imagen.getOriginalFilename());
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        log.info("Se crea gestion " + notificacion);
        notificacionService.guardar(notificacion);
        return "redirect:/general";
    }

    @GetMapping("/modificargeneral")
    public String editarGeneral(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }
        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "modificargeneral";
    }

    @GetMapping("/cerrargeneral")
    public String eliminarGeneral(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //ticket.setEstado(0L);
        notificacion.setEstado(estadoTicketService.encontrarEstado(4L));
        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/general";
    }

    /**
     * *************
     *
     * ESPECIFICAS
     *
     *************
     */
    @GetMapping("/especifica")
    public String InicioEspecifica(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        var notificaciones = notificacionService.notificacionPorTipo(varNotiEspecifica, 1L);//agregar residencial

        model.addAttribute("notificaciones", notificaciones);
        return "especifica";
    }

    @GetMapping("/agregarespecifica")
    public String agregarEspecifica(Notificacion notificacion, Model model) {
        var usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        log.info(" -------------------------" + usuarios.toString());
        return "modificarespecifica";
    }

    @PostMapping("/guardarespecifica")
    public String guardarEspecifica(@Valid Notificacion notificacion, @RequestParam("file") MultipartFile imagen, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();
        Date fecha = null;
        try {
            fecha = formatter.parse(date.toString());
        } catch (ParseException ex) {
            log.info("error en get fecha");
        }
        if (errors.hasErrors()) {
            return "modificarespecifica";
        }

        notificacion.setTipo(varNotiEspecifica);
        log.info("antes de validar " + notificacion.getIdNotificacion());

        if (notificacion.getIdNotificacion() == null) {
            log.info("\n\n--------------------dentro de validar " + notificacion.getIdNotificacion());

            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us = usuarioService.encontrarUsuario(us);
            notificacion.setUsuario(us);

            notificacion.setFechaCrea(date);
            notificacion.setUsuarioCrea(1L);
            notificacion.setIdResidencial(1L);
            notificacion.setEstado(estadoTicketService.encontrarEstado(1L));
        } else {
            log.info("else de validar " + notificacion.getIdNotificacion());
            notificacion.setFechaModifica(date);
            notificacion.setUsuarioModifica(1L);
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
                notificacion.setAdjunto("adjunto/" + imagen.getOriginalFilename());
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        log.info("Se crea gestion " + notificacion);
        notificacionService.guardar(notificacion);
        return "redirect:/especifica";
    }

    @GetMapping("/modificarespecifica")
    public String editarEspecifica(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        var usuarios = usuarioService.listarUsuarios();
        if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "modificarespecifica";
    }

    @GetMapping("/cerrarespecifica")
    public String eliminarEspecifica(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        notificacion.setEstado(estadoTicketService.encontrarEstado(4L));

        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/especifica";
    }

}
