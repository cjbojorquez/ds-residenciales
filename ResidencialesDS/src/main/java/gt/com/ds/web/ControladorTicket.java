package gt.com.ds.web;

import gt.com.ds.domain.Comentario;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ComentarioService;
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
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;

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
    private ComentarioService comentarioService;

    @Autowired
    private EstadoTicketService estadoTicketService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Varios varios;

    @GetMapping("/gestion")
    public String Inicio(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        Long tipoTicket = 1L;
        var gestiones = ticketService.listarTicketsAbiertos(tipoTicket, 1L);//residencial dinamica
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
            us = usuarioService.encontrarUsuario(us);
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
        if (ticket.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            ticket.setEstado(estadoTicket);
        }
        var comentarios = comentarioService.comentarioPorTicket(ticket.getIdTicket());
        model.addAttribute("comentarios", comentarios);
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

    /**
     * *************
     *
     * ANOMALIAS
     *
     *************
     */
    @GetMapping("/anomalia")
    public String InicioAnomalia(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        Long tipoTicket = 2L;
        var anomalias = ticketService.listarTicketsAbiertos(tipoTicket, 1L);//residencial
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
            us = usuarioService.encontrarUsuario(us);
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
        if (ticket.getEstado().getIdEstado() == 1L) {
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

    /**
     * COMENTARIOS
     */
    @PostMapping("/guardarcomentario")
    public String guardarComentario(Comentario comentario, @RequestParam("txtcomentario") String txtComentario, @RequestParam("idticket") String idTicket, Model model, @RequestParam("file") MultipartFile imagen, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        System.out.println("usuarioLogueado = " + usuarioLogueado);
        System.out.println("txtComentario = " + txtComentario);
        System.out.println("idTicket = " + idTicket);
        System.out.println("imagen = " + imagen);
        Ticket tk = ticketService.encontrarTicket(Long.parseLong(idTicket));
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificargestion";
        }
        if (txtComentario != "" && !imagen.isEmpty()) {
            comentario.setTicket(tk);
            comentario.setFecha(date);
            comentario.setUsuario(usuarioLogueado);
            comentario.setComentario(txtComentario);
            if (!imagen.isEmpty()) {
                Path directorioImagenes = Paths.get("src//main//resources//static//adjunto");

                String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
                log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
                try {
                    byte[] byteImg = imagen.getBytes();
                    String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); // cambiar por dinamica
                    Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                    //notificacion.setAdjunto("adjunto/" + nombreArchivo);
                    comentario.setAdjunto("adjunto/" + nombreArchivo);
                    log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                    Files.write(rutaCompleta, byteImg);
                } catch (IOException ex) {
                    Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            log.info("Se crea comentario " + comentario);
            comentarioService.guardar(comentario);
        }
        var comentarios = comentarioService.comentarioPorTicket(tk.getIdTicket());
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("ticket", tk);
        return "redirect:/editargestion?idTicket=" + idTicket;
    }

    /*@GetMapping("/download/{commentId}")
    public void downloadFile(@PathVariable Long commentId, HttpServletResponse response) throws IOException {
        Comentario comentario = comentarioService.comentarioPorTicket(commentId);

        if (comentario != null && comentario.getAdjunto() != null) {
            // Configurar la respuesta HTTP para la descarga del archivo adjunto
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + comentario.getAdjunto());

            // Escribir el contenido del archivo adjunto en la respuesta
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(comentario.getAdjuntoBytes()); // Reemplaza 'getAdjuntoBytes' con el método apropiado para obtener los bytes del archivo adjunto
            outputStream.close();
        }
    }*/
}
