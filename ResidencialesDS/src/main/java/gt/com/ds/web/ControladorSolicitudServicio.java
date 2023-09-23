package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.SolicitudServicio;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.EstadoTicketService;
import gt.com.ds.servicio.NotificacionService;
import gt.com.ds.servicio.ServicioService;
import gt.com.ds.servicio.SolicitudServicioService;
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
public class ControladorSolicitudServicio {

    @Autowired
    private NotificacionService notificacionService;
    
    @Autowired
    private SolicitudServicioService solicitudServicioService;

    @Autowired
    private ServicioService servicioService;
    
    @Autowired
    private EstadoTicketService estadoTicketService;

    @Autowired
    private BuzonService buzonService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Varios varios;

    private Long varEstadoActivo=1L;

    
    @GetMapping("/solicitud")
    public String InicioGeneral(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        Usuario us=varios.getUsuarioLogueado();
        List<SolicitudServicio> solicitudes;
         if(varios.getRolLogueado().equals("ROLE_USER"))
            solicitudes = solicitudServicioService.listarPorUsuario(varEstadoActivo,us.getIdUsuario());
        else
            solicitudes = solicitudServicioService.listarSolicitudes(varEstadoActivo, us.getResidencial().getIdResidential());
        
        model.addAttribute("solicitudes", solicitudes);

        return "solicitud";
    }

    @GetMapping("/agregarsolicitud")
    public String agregarGeneral(SolicitudServicio solicitudServicio, Model model) {
        
        Usuario us = varios.getUsuarioLogueado();
        var servicios = servicioService.listarPorResidencial(us.getResidencial().getIdResidential());
        var empleados = usuarioService.listarEmpleadosResidencial(varEstadoActivo, us.getResidencial().getIdResidential());
        model.addAttribute("solicitud", solicitudServicio);
        model.addAttribute("servicios", servicios);
        model.addAttribute("empleados", empleados);
        return "modificarsolicitud";
    }

    @PostMapping("/guardarsolicitud")
    public String guardarSolicitud(@Valid SolicitudServicio solicitudServicio, @RequestParam("file") MultipartFile imagen,
            @RequestParam("desdeFecha") String desdeFecha, @Valid @RequestParam("desdeHora") String desdeHora,
            BindingResult bindingResult,
            Model model,
            Errors errors) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        log.info("\n\nNo hay errores " + errors.toString());

        /*if (solicitudServicio.getAsunto() == null || solicitudServicio.getAsunto().trim().isEmpty()) {
            // Agrega un error personalizado al objeto BindingResult
            bindingResult.rejectValue("asunto", "error.asunto", "El campo asunto no puede estar vacío.");
        }*/
        if (!Tools.cumplePatron("\\d{2}:\\d{2}", desdeHora)) {
            // Agrega un error personalizado al objeto BindingResult
            bindingResult.rejectValue("desde", "error.desde", "Se debe colocar una hora valida.");
        }
        
        if (errors.hasErrors()) {
            log.info("Error en guardar ");
            return "modificarsolicitud";
        }
        solicitudServicio.setFecha(Tools.getFecha(desdeFecha, desdeHora));
        //solicitudServicio.setHasta(Tools.getFecha(hastaFecha, hastaHora));

        if (solicitudServicio.getIdSolicitud() == null) {

            
            solicitudServicio.setUsuario(usuarioLogueado);

            solicitudServicio.setFechaCrea(Tools.now());
            solicitudServicio.setUsuarioCrea(usuarioLogueado.getIdUsuario());
            solicitudServicio.setResidencial(usuarioLogueado.getResidencial());
            solicitudServicio.setEstado(1L);

        } else {
            solicitudServicio.setFechaModifica(Tools.now());
            solicitudServicio.setUsuarioModifica(usuarioLogueado.getIdUsuario());
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//adjunto");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); // cambiar por dinamica
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                //solicitudServicio.setAdjunto("adjunto/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        log.info("Se crea gestion " + solicitudServicio);
        solicitudServicioService.guardar(solicitudServicio);
        Usuario us=varios.getUsuarioLogueado();
        List<SolicitudServicio> solicitudes;
         if(varios.getRolLogueado().equals("ROLE_USER"))
            solicitudes = solicitudServicioService.listarPorUsuario(varEstadoActivo,us.getIdUsuario());
        else
            solicitudes = solicitudServicioService.listarSolicitudes(varEstadoActivo, us.getResidencial().getIdResidential());
        
        model.addAttribute("solicitudes", solicitudes);
        //return "redirect:/vergeneral?idNotificacion=" + newNoti.getIdNotificacion();
        return "solicitud";
    }

    @GetMapping("/modificarsolicitud")
    public String editarGeneral(SolicitudServicio solicitudServicio, Model model) {
        Usuario us = varios.getUsuarioLogueado();
        var solicitud = solicitudServicioService.encontrarServicio(solicitudServicio);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        var servicios = servicioService.listarPorResidencial(us.getResidencial().getIdResidential());
        var empleados = usuarioService.listarEmpleadosResidencial(varEstadoActivo, us.getResidencial().getIdResidential());
        model.addAttribute("solicitud", solicitud);
        model.addAttribute("servicios", servicios);
        model.addAttribute("empleados", empleados);
        
        /*if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }*/
        model.addAttribute("estadosTicket", estadosTicket);
        //model.addAttribute("solicitud", solicitud);
        //log.info("se envia ticket " + notificacion.toString());
        return "modificarsolicitud";
    }

    /*@GetMapping("/vergeneral")
    public String verGeneral(Notificacion notificacion, Model model) {
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        /*if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }                                                                                     una vez enviado cambiar el estado 
        model.addAttribute("estadosTicket", estadosTicket);
        model.addAttribute("notificacion", notificacion);
        //log.info("se envia ticket " + notificacion.toString());
        return "vergeneral";
    }*/

    /*@GetMapping("/enviageneral")
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

            Logger.getLogger(ControladorSolicitudServicio.class.getName()).log(Level.SEVERE, null, ex);
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
                //buzonService.guardar(buzon);
            }
        }
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket a: " + mails);
        notificacion.setEstado(estadoTicketService.encontrarEstado(3L));//cerrar notificacion

        log.info("Se envio correo con los siguientes datos: Mensaje=" + mensaje + " destinatarios=" + mails + " notificacion=" + notificacion);

        var notificaciones = notificacionService.notificacionPorTipo(varNotiGeneral, usuarioLogueado.getResidencial().getIdResidential());//agregar residencial
        model.addAttribute("notificaciones", notificaciones);
        return "general";
    }*/

    @GetMapping("/cerrarsolicitud")
    public String eliminarSolicitud(SolicitudServicio solicitudServicio, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //ticket.setEstado(0L);
        solicitudServicio.setEstado(4L);
        //log.info("Eliminando gestion " + rol);
        solicitudServicioService.guardar(solicitudServicio);
        return "redirect:/solicitud";
    }

    
/*
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
    }*/
}
