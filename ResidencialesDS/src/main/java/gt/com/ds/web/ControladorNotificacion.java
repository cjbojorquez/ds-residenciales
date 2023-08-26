package gt.com.ds.web;

import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Ticket;
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
import gt.com.ds.servicio.TicketService;
import gt.com.ds.servicio.UsuarioService;

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

    private String varNotGeneral="G";
    private String varNotEspecifica="E";
    @GetMapping("/notificacion")
    public String Inicio(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        var notificaciones = notificacionService.notificacionPorTipo(varNotGeneral,1L);//agregar residencial
        model.addAttribute("notificaciones", notificaciones);
        return "gestion";
    }

    @GetMapping("/agregargestion")
    public String agregar(Notificacion notificacion, Model model) {
        var estadoTicket = estadoTicketService.encontrarEstado(1L);
        model.addAttribute("estadoTicket", estadoTicket);
        return "creargestion";
    }

    @PostMapping("/guardargestion")
    public String guardar(@Valid Notificacion notificacion, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificargestion";
        }
        if (notificacion.getIdNotificacion() == null) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(1L);
            notificacion.setEstado(estadoTicket);
            notificacion.setIdResidencial(1L);//cambiar a dinamico
            notificacion.setTipo(varNotGeneral);
            
            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us=usuarioService.encontrarUsuario(us);
            notificacion.setUsuario(us);
        }

        log.info("Se crea gestion " + notificacion);
        notificacionService.guardar(notificacion);
        return "redirect:/gestion";
    }

    @GetMapping("/editargestion")
    public String editar(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        if (notificacion.getEstado().getIdEstado()==1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }
         model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "modificargestion";
    }

    @GetMapping("/cerrargestion")
    public String eliminar(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //ticket.setEstado(0L);

        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/gestion";
    }

    /***************
     * 
     *           ANOMALIAS
     * 
     **************/
    
    
}
