package gt.com.ds.web;

import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.EstadoTicketService;
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
public class ControladorTicket {

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EstadoTicketService estadoTicketService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/gestion")
    public String Inicio(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        Long tipoTicket = 1L;
        var gestiones = ticketService.listarTicketsAbiertos(tipoTicket,1L);//residencial dinamica
        model.addAttribute("gestiones", gestiones);
        return "gestion";
    }

    @GetMapping("/agregargestion")
    public String agregar(Ticket ticket, Model model) {
        var estadoTicket = estadoTicketService.encontrarEstado(1L);
        model.addAttribute("estadoTicket", estadoTicket);
        return "creargestion";
    }

    @PostMapping("/guardargestion")
    public String guardar(@Valid Ticket ticket, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificargestion";
        }
        if (ticket.getIdTicket() == null) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(1L);
            ticket.setEstado(estadoTicket);
            ticket.setIdResidencial(1L);//cambiar a dinamico
            ticket.setIdTipo(1L);
            
            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us=usuarioService.encontrarUsuario(us);
            ticket.setUsuario(us);
        }

        log.info("Se crea gestion " + ticket);
        ticketService.guardar(ticket);
        return "redirect:/gestion";
    }

    @GetMapping("/editargestion")
    public String editar(Ticket ticket, Model model) {
        ticket = ticketService.encontrarTicket(ticket);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        if (ticket.getEstado().getIdEstado()==1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            ticket.setEstado(estadoTicket);
        }
         model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("ticket", ticket);
        log.info("se envia ticket " + ticket.toString());
        return "modificargestion";
    }

    @GetMapping("/cerrargestion")
    public String eliminar(Ticket ticket, Model model) {
        //ticket = ticketService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //ticket.setEstado(0L);

        //log.info("Eliminando gestion " + rol);
        ticketService.guardar(ticket);
        return "redirect:/gestion";
    }

    /***************
     * 
     *           ANOMALIAS
     * 
     **************/
    
    @GetMapping("/anomalia")
    public String InicioAnomalia(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        Long tipoTicket = 2L;
        var anomalias = ticketService.listarTicketsAbiertos(tipoTicket,1L);//residencial
        model.addAttribute("anomalias", anomalias);
        return "anomalia";
    }

    @GetMapping("/agregaranomalia")
    public String agregarAnomalia(Ticket ticket, Model model) {
        var estadoTicket = estadoTicketService.encontrarEstado(1L);
        model.addAttribute("estadoTicket", estadoTicket);
        return "crearanomalia";
    }

    @PostMapping("/guardaranomalia")
    public String guardarAnomalia(@Valid Ticket ticket, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificaranomalia";
        }
        if (ticket.getIdTicket() == null) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(1L);
            ticket.setEstado(estadoTicket);
            ticket.setIdResidencial(1L);//cambiar a dinamico
            ticket.setIdTipo(2L);
            
            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us=usuarioService.encontrarUsuario(us);
            ticket.setUsuario(us);
        }

        log.info("Se crea anomalia " + ticket);
        ticketService.guardar(ticket);
        return "redirect:/anomalia";
    }

    @GetMapping("/editaranomalia")
    public String editarAnomalia(Ticket ticket, Model model) {
        ticket = ticketService.encontrarTicket(ticket);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        if (ticket.getEstado().getIdEstado()==1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            ticket.setEstado(estadoTicket);
        }
         model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("ticket", ticket);
        log.info("se envia ticket " + ticket.toString());
        return "modificaranomalia";
    }

    @GetMapping("/cerraranomalia")
    public String eliminarAnomalia(Ticket ticket, Model model) {
        //ticket = ticketService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //ticket.setEstado(0L);

        //log.info("Eliminando gestion " + rol);
        ticketService.guardar(ticket);
        return "redirect:/anomalia";
    }

}
