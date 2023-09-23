package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
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
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.io.ClassPathResource;
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
    private BuzonService buzonService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Varios varios;

    private String varNotiGeneral = "G";
    private String varNotiEspecifica = "E";

    @GetMapping("/general")
    public String InicioGeneral(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
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
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
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

            
            notificacion.setUsuario(usuarioLogueado);

            notificacion.setFechaCrea(Tools.now());
            notificacion.setUsuarioCrea(usuarioLogueado.getIdUsuario());
            notificacion.setIdResidencial(usuarioLogueado.getResidencial().getIdResidential());
            notificacion.setEstado(estadoTicketService.encontrarEstado(1L));

        } else {
            notificacion.setFechaModifica(Tools.now());
            notificacion.setUsuarioModifica(usuarioLogueado.getIdUsuario());
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
        }*///                                                                                      una vez enviado cambiar el estado 
        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        //log.info("se envia ticket " + notificacion.toString());
        return "vergeneral";
    }

    @GetMapping("/enviageneral")
    public String enviaGeneral(Notificacion notificacion, Model model) {
        Long estdoActivoUs = 1L;
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var usuarios = usuarioService.listarUsuariosResidencial(estdoActivoUs, usuarioLogueado.getResidencial().getIdResidential());

        String nombreArchivo = notificacion.getAdjunto();
        ClassPathResource resource = new ClassPathResource("static/" + nombreArchivo);
        System.out.println("resource = " + resource);
        File file = null;
        try {

            file = resource.getFile();
        } catch (IOException ex) {

            Logger.getLogger(ControladorNotificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        var mensaje = creaMensaje(notificacion);
        String mails = "";
        int index = 0;
        for (Usuario us : usuarios) {
            if (!us.getNombreUsuario().equals(usuarioLogueado.getNombreUsuario())) {
                Buzon buzon = new Buzon();
                buzon.setAsunto(notificacion.getAsunto());
                buzon.setDescripcion(mensaje.get(1));
                buzon.setAdjunto(notificacion.getAdjunto());
                buzon.setEstado(1L);
                buzon.setUsuario(us);
                buzon.setFechaCrea(Tools.now());
                buzon.setUsuarioCrea(usuarioLogueado.getIdUsuario());
                varios.sendEmail(us.getEmail(), notificacion.getAsunto(), mensaje.get(0), file, "no-replay@residencial.com");
                buzonService.guardar(buzon);
            }
        }
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket a: " + mails);
        notificacion.setEstado(estadoTicketService.encontrarEstado(3L));//cerrar notificacion

        log.info("Se envio correo con los siguientes datos: Mensaje=" + mensaje + " destinatarios=" + mails + " notificacion=" + notificacion);

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
        Usuario us = varios.getUsuarioLogueado();
        var notificaciones = notificacionService.notificacionPorTipo(varNotiEspecifica, us.getResidencial().getIdResidential());//agregar residencial

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

        Usuario usuarioLogueado = varios.getUsuarioLogueado();

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
            notificacion.setUsuarioModifica(usuarioLogueado.getIdUsuario());
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

    @GetMapping("/enviaespecifica")
    public String enviaEspecifica(Notificacion notificacion, Model model) {
        Long estadoActivoUs = 1L;
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var us = usuarioService.encontrarUsuario(notificacion.getUsuario());

        String nombreArchivo = notificacion.getAdjunto();
        ClassPathResource resource = null;
        if (!"".equals(nombreArchivo)) {
            resource = new ClassPathResource("static/" + nombreArchivo);
        }
        System.out.println("resource = " + resource);
        File file = null;
        if (resource != null) {
            try {

                file = resource.getFile();
            } catch (IOException ex) {

                Logger.getLogger(ControladorNotificacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        var mensaje = creaMensaje(notificacion);
        String mails = "";

        Buzon buzon = new Buzon();
        buzon.setAsunto(notificacion.getAsunto());
        buzon.setDescripcion(mensaje.get(1));
        buzon.setAdjunto(notificacion.getAdjunto());
        buzon.setEstado(1L);
        buzon.setUsuario(us);
        buzon.setFechaCrea(Tools.now());
        buzon.setUsuarioCrea(usuarioLogueado.getIdUsuario());
        System.out.println("mensaje.get(0) = " + mensaje.get(0));
        varios.sendEmail(us.getEmail(), notificacion.getAsunto(), mensaje.get(0), file, "no-replay@residencial.com");
        buzonService.guardar(buzon);

        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket a: " + mails);
        notificacion.setEstado(estadoTicketService.encontrarEstado(3L));//cerrar notificacion

        log.info("Se envio correo con los siguientes datos: Mensaje=" + mensaje + " destinatarios=" + mails + " notificacion=" + notificacion);

        var notificaciones = notificacionService.notificacionPorTipo(varNotiEspecifica, usuarioLogueado.getResidencial().getIdResidential());//agregar residencial
        model.addAttribute("notificaciones", notificaciones);
        return "especifica";
    }

    @GetMapping("/cerrarespecifica")
    public String eliminarEspecifica(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        notificacion.setEstado(estadoTicketService.encontrarEstado(4L));

        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/especifica";
    }

    public List<String> creaMensaje(Notificacion notificacion) {

        ArrayList mensaje = new ArrayList();
        String msg = "";
        String mensajeHTML = "";
        if (notificacion.getTipo().equals(varNotiEspecifica)) {
            msg = "<h3>" + notificacion.getAsunto() + "</h3><br>";
            msg = msg + "<table>";
            if (notificacion.getAdjunto().length() > 0) {
                boolean isImage = notificacion.getAdjunto().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$");
                if (isImage) {
                    msg = msg + "<tr><td colspan=\"2\"><img src=\"$$$$&&\" alt=\"imagen\" style=\"max-width: 100%; max-height: 100%;\"></td></tr>";
                }
            }
            msg = msg + "<tr><td colspan=\"2\">" + notificacion.getDescripcion() + "</td></tr>";
            msg = msg + "<tr>";
            
            
            msg = msg + "</tr></table>";
        } else if (notificacion.getTipo().equals(varNotiGeneral)) {
            msg = "<h3>" + notificacion.getAsunto() + "</h3><br>";
            msg = msg + "<table>";
            if (notificacion.getAdjunto().length() > 0) {
                boolean isImage = notificacion.getAdjunto().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$");
                if (isImage) {
                    msg = msg + "<tr><td colspan=\"2\"><img src=\"$$$$&&\" alt=\"imagen\" style=\"max-width: 100%; max-height: 100%;\"></td></tr>";
                }
            }
            msg = msg + "<tr><td colspan=\"2\">" + notificacion.getDescripcion() + "</td></tr>";
            msg = msg + "<tr>";
            if (notificacion.getDesde() != null && !notificacion.getDesde().toString().equals("")) {
                msg = msg + "<td>Inicia:" + Tools.formateaFecha(notificacion.getDesde()) + "</td>";
            }
            if (notificacion.getHasta() != null && !notificacion.getHasta().toString().equals("")) {
                msg = msg + "<td>Finaliza:" + Tools.formateaFecha(notificacion.getHasta()) + "</td>";
            }
            msg = msg + "</tr></table>";
        }
        String mensajeMail = msg.replace("$$$$&&", "cid:imagen");
        mensajeHTML = msg.replace("$$$$&&", notificacion.getAdjunto());
        mensaje.add(mensajeMail);
        mensaje.add(mensajeHTML);
        return mensaje;
    }
}
