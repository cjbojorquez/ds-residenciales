/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.Comentario;
import gt.com.ds.domain.Mensaje;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.ComentarioService;
import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.TicketService;
import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * En esta clase se administra la informacion del header
 *
 * @author cjbojorquez
 *
 */
@Controller
@Slf4j
public class ControladorHeader {

    @Autowired
    private BuzonService buzonService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ResidencialService residencialService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private Varios varios;

    @Autowired
    private UsuarioService usuarioService;

    private Long noLeidos = 1L;

    /**
     * Este controlador permite mostrar todos los mensajes nuevos que recibe el
     * usuario en el header
     *
     * @param model
     * @return
     */
    @GetMapping("/obtenerMensajes")
    @ResponseBody
    public List<Mensaje> obtenerMensajes(Model model) {
        Usuario us = varios.getUsuarioLogueado();
        String rol = varios.getRolLogueado();
        var buzon = buzonService.buzonPorEstado(noLeidos, us.getIdUsuario());
        List<Mensaje> mensajes = new ArrayList<>();
        for (Buzon b : buzon) {
            Mensaje mensaje = new Mensaje();
            mensaje.setAsunto(b.getAsunto());
            mensaje.setUrl("/buzon");
            mensajes.add(mensaje);
        }
        System.out.println("mensajes1 = " + mensajes);
        List<Comentario> comentarios = new ArrayList<>();
        if (rol.equals("ROLE_USER")) {
            comentarios = comentarioService.buscaNoLeidos(us.getIdUsuario());
        } else {
            comentarios = comentarioService.buscaNoLeidosR(us.getResidencial().getIdResidential());
            List<Ticket> tickets = ticketService.ticketPorEstado(1L, us.getResidencial().getIdResidential());
            if (tickets != null) {
                for (Ticket t : tickets) {
                    System.out.println("t = " + t);
                    Mensaje mensaje = new Mensaje();
                    mensaje.setAsunto(t.getAsunto());
                    mensaje.setUrl(t.getIdTipo() == 1 ? "/editargestion?idTicket=" + t.getIdTicket() : "/editaranomalia?idTicket=" + t.getIdTicket());

                    mensajes.add(mensaje);
                }
            }
        }
        for (Comentario c : comentarios) {
            Mensaje mensaje = new Mensaje();
            mensaje.setAsunto(c.getComentario().length() > 10 ? c.getComentario().substring(0, 9) + "..." : c.getComentario());
            String url=c.getTicket().getIdTipo() == 1 ? "/editargestion?idTicket=" + c.getTicket().getIdTicket() : "/editaranomalia?idTicket=" + c.getTicket().getIdTicket();
            url=url + "&idcomentario="+c.getIdComentario();
            mensaje.setUrl(url);
            if (!Objects.equals(us.getIdUsuario(), c.getUsuario().getIdUsuario())) {
                mensajes.add(mensaje);

            }
        }
        var residencial = us.getResidencial();
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("residencial", residencial);
        return mensajes;
    }
}
