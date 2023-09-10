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
import gt.com.ds.util.Tools;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

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
        Usuario usuarioLogueado=getUsuarioLogueado();
        var notificaciones = notificacionService.notificacionPorTipo(varNotiGeneral, usuarioLogueado.getResidencial().getIdResidential());//agregar residencial
        log.info("estas son las notificaciones " + notificaciones.toString());
        model.addAttribute("notificaciones", notificaciones);
        return "general";
    }

    @GetMapping("/agregargeneral")
    public String agregarGeneral(Notificacion notificacion, Model model) {

        return "modificargeneral";
    }

    @PostMapping("/guardargeneral")
    public String guardarGeneral(@Valid Notificacion notificacion, @RequestParam("file") MultipartFile imagen,
            @RequestParam("desdeFecha") String desdeFecha, @Valid @RequestParam("desdeHora") String desdeHora,
            @RequestParam("hastaFecha") String hastaFecha, @Valid @RequestParam("hastaHora") String hastaHora,
            BindingResult bindingResult,
            Model model,
            Errors errors) {

        log.info("\n\nNo hay errores " + errors.toString());

        if (notificacion.getAsunto() == null || notificacion.getAsunto().trim().isEmpty()) {
            // Agrega un error personalizado al objeto BindingResult
            bindingResult.rejectValue("asunto", "error.asunto", "El campo asunto no puede estar vacío.");
        }
        if (!Tools.cumplePatron("\\d{2}:\\d{2}", desdeHora)) {
            // Agrega un error personalizado al objeto BindingResult
            bindingResult.rejectValue("desde", "error.desde", "Se debe colocar una hora valida.");
        }
        if (!Tools.cumplePatron("\\d{2}:\\d{2}", hastaHora)) {
            // Agrega un error personalizado al objeto BindingResult
            bindingResult.rejectValue("hasta", "error.hasta", "Se debe colocar una hora valida.");
        }
        if (errors.hasErrors()) {
            log.info("Error en guardar ");
            return "modificargeneral";
        }

        notificacion.setTipo(varNotiGeneral);
        notificacion.setDesde(Tools.getFecha(desdeFecha, desdeHora));
        notificacion.setHasta(Tools.getFecha(hastaFecha, hastaHora));

        if (notificacion.getIdNotificacion() == null) {

            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us = usuarioService.encontrarUsuario(us);
            notificacion.setUsuario(us);

            notificacion.setFechaCrea(Tools.now());
            notificacion.setUsuarioCrea(1L);
            notificacion.setIdResidencial(1L);
            notificacion.setEstado(estadoTicketService.encontrarEstado(1L));

        } else {
            notificacion.setFechaModifica(Tools.now());
            notificacion.setUsuarioModifica(1L);
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//adjunto");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); // cambiar por dinamica
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                notificacion.setAdjunto("adjunto/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        log.info("Se crea gestion " + notificacion);
        Notificacion newNoti = notificacionService.guardar(notificacion);
        model.addAttribute("notificacion", newNoti);
        return "redirect:/vergeneral?idNotificacion=" + newNoti.getIdNotificacion();
    }

    @GetMapping("/modificargeneral")
    public String editarGeneral(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        /*if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }*/
        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "modificargeneral";
    }

    @GetMapping("/vergeneral")
    public String verGeneral(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        /*if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }*/
        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "vergeneral";
    }

    @GetMapping("/enviageneral")
    public String enviaGeneral(Notificacion notificacion, Model model) {
        Long usuarioActivo = 1L;
        Usuario usuarioLogueado=getUsuarioLogueado();
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var usuarios = usuarioService.listarUsuariosResidencial(usuarioActivo, usuarioLogueado.getResidencial().getIdResidential());
        String[] correos = new String[usuarios.size()];
        
        String mensaje=creaMensaje(notificacion);
        String mails="";
        int index = 0;
        for (Usuario us : usuarios) {
            correos[index] = us.getEmail();
            mails=mails+"|"+us.getEmail();
            index++;
        }
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        notificacion.setEstado(estadoTicketService.encontrarEstado(3L));//cerrar notificacion
        
        log.info("Se envia correo con los siguientes datos: Mensaje="+mensaje+" destinatarios=" + mails + " notificacion="+notificacion);
        var notificaciones = notificacionService.notificacionPorTipo(varNotiGeneral, usuarioLogueado.getResidencial().getIdResidential());//agregar residencial
        model.addAttribute("notificaciones", notificaciones);
        return "general";
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
        return "modificarespecifica";
    }

    @PostMapping("/guardarespecifica")
    public String guardarEspecifica(@Valid Notificacion notificacion, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {

        if (errors.hasErrors()) {
            return "modificarespecifica";
        }

        Usuario usuarioLogueado = getUsuarioLogueado();

        notificacion.setTipo(varNotiEspecifica);
        log.info("antes de validar " + notificacion.getIdNotificacion());

        if (notificacion.getIdNotificacion() == null) {

            Usuario us = new Usuario();
            us.setIdUsuario(1L);
            us = usuarioService.encontrarUsuario(us);
            notificacion.setUsuario(us);

            notificacion.setFechaCrea(Tools.now());
            notificacion.setUsuarioCrea(usuarioLogueado.getIdUsuario());
            notificacion.setIdResidencial(usuarioLogueado.getResidencial().getIdResidential());
            notificacion.setEstado(estadoTicketService.encontrarEstado(1L));
        } else {
            log.info("else de validar " + notificacion.getIdNotificacion());
            notificacion.setFechaModifica(Tools.now());
            notificacion.setUsuarioModifica(1L);
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//adjunto");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            String nombreArchivo = Tools.newName(imagen.getOriginalFilename());//agregar usuario dinamico
            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                notificacion.setAdjunto("adjunto/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        log.info("Se crea gestion " + notificacion);
        Notificacion newNoti = notificacionService.guardar(notificacion);
        log.info(" --------- \n \n valor de save + " + newNoti);
        model.addAttribute("notificacion", newNoti);
        return "redirect:/verespecifica?idNotificacion=" + newNoti.getIdNotificacion();
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

    @GetMapping("/verespecifica")
    public String verEspecifica(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        var usuarios = usuarioService.listarUsuarios();
        /*if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }*/
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "verespecifica";
    }

    @GetMapping("/cerrarespecifica")
    public String eliminarEspecifica(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        notificacion.setEstado(estadoTicketService.encontrarEstado(4L));

        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/especifica";
    }

    public Usuario getUsuarioLogueado() {
        // Obtén el objeto Authentication del contexto de seguridad actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("solicita usuario logueado:" + authentication);
        // Verifica si el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = usuarioService.encontrarUsuario(username);
            System.out.println("Nombre de usuario: " + username);
            return usuario;
        } else {
            // El usuario no está autenticado
            System.out.println("Usuario no autenticado");
            return null;
        }

    }

    public String creaMensaje(Notificacion notificacion) {

        String mensaje="";
        if (notificacion.getTipo().equals(varNotiEspecifica)) {

        } else if (notificacion.getTipo().equals(varNotiGeneral)) {
            mensaje = "<h3>" + notificacion.getAsunto() + "</h3><br>";
            boolean isImage = notificacion.getAdjunto().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$");
            if (isImage) {
                mensaje = mensaje + "<img src=\"cid:imagen\" alt=\"imagen\"><br>";
            }
            mensaje = mensaje + notificacion.getDescripcion() + "<br>";
            mensaje = mensaje + "<table><tr>";
            if (!notificacion.getDesde().toString().equals("")) {
                mensaje = mensaje + "<td>Inicia:" + Tools.formateaFecha(notificacion.getDesde()) + "</td>";
            }
            if (!notificacion.getHasta().toString().equals("")) {
                mensaje = mensaje + "<td>Finaliza:" + Tools.formateaFecha(notificacion.getHasta()) + "</td>";
            }
            mensaje = mensaje + "</tr></table>";
        }

        return mensaje;
    }
}
