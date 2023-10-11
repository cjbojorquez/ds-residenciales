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
 * Esta clase nos permite manejar las solicitudes de servicio de los usuarios
 *
 * @author cjbojorquez
 * 
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

    /**
     * Este controler mustra todas las solicitudes activas ingresadas por los usuarios, si el usuario logueado
     * tiene rol de usuario solo se le muestran las solicitudes que el ingreso, de lo contrario, se muestran
     * todas las solicitudes asociadas a la recidencial
     */
    @GetMapping("/solicitud")
    public String InicioSolicitud(Model model) {
        Usuario us=varios.getUsuarioLogueado();
        List<SolicitudServicio> solicitudes;
         if(varios.getRolLogueado().equals("ROLE_USER"))
            solicitudes = solicitudServicioService.listarPorUsuario(varEstadoActivo,us.getIdUsuario());
        else
            solicitudes = solicitudServicioService.listarSolicitudes(varEstadoActivo, us.getResidencial().getIdResidential());
        
        model.addAttribute("solicitudes", solicitudes);

        return "solicitud";
    }

    /**
     * Este controlador permite cargar los tipos de servicio al formulario para agregar solicitudes, 
     * modificarsolicitud.html donde se ingresaran los servicios a crear.
     * @return se redirecciona hacia la pagina modificarsolicitud.html
     */
    @GetMapping("/agregarsolicitud")
    public String agregarSolicitud(SolicitudServicio solicitudServicio, Model model) {
        
        Usuario us = varios.getUsuarioLogueado();
        var servicios = servicioService.listarPorResidencial(us.getResidencial().getIdResidential());
        var empleados = usuarioService.listarEmpleadosResidencial(varEstadoActivo, us.getResidencial().getIdResidential());
        model.addAttribute("solicitud", solicitudServicio);
        model.addAttribute("servicios", servicios);
        model.addAttribute("empleados", empleados);
        return "modificarsolicitud";
    }

    /**
     * Este controlador se encarga de guardar las solicitudes creadas y las modificadas,
     * 
     * @param solicitudServicio este parametro contiene la informacion de la solicitud de servicio 
     * a persistir en la base de datos.
     * @param imagen si el usuario adjunta algun archivo este se adjunta en este parametro
     * @param desdeFecha se coloca la fecha en la que se decea que se ejecute el servicio
     * @param desdeHora aqui se coloca la hora en que se decea se lleve a cabo el servicio
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @param errors
     * @return 
     */
    @PostMapping("/guardarsolicitud")
    public String guardarSolicitud(@Valid SolicitudServicio solicitudServicio, @RequestParam("file") MultipartFile imagen,
            @RequestParam("desdeFecha") String desdeFecha, @Valid @RequestParam("desdeHora") String desdeHora,
            BindingResult bindingResult,
            Model model,
            Errors errors) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        //log.info("\n\nNo hay errores " + errors.toString());

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
            if(usuarioLogueado.getIdUsuario()!=solicitudServicio.getUsuarioCrea()){
                Buzon buzon = new Buzon();
                buzon.setAsunto("Actualizacion en Solicitud #" + solicitudServicio.getIdSolicitud());
                buzon.setDescripcion("Se actualiz&oacute; la solicitud de servicio (" + solicitudServicio.getIdSolicitud() 
                        + ") - " + solicitudServicio.getAsunto());
                buzon.setFechaCrea(Tools.now());
                buzon.setUsuarioCrea(usuarioLogueado.getIdUsuario());
                buzon.setUsuario(solicitudServicio.getUsuario());
                buzon.setEstado(1L);
            }
        }

        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//adjunto");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            //log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); // cambiar por dinamica
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                //solicitudServicio.setAdjunto("adjunto/" + nombreArchivo);
                //log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //log.info("Se crea gestion " + solicitudServicio);
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
    
    /**
     * Esta funcion carga las variables necesarias para modificar una solicitud específica
     * @param solicitudServicio este parametro lleva la informacion de la solicitud a modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return aqui se indica la pagina a la que se le cargara la informacion
     */
    @GetMapping("/modificarsolicitud")
    public String editarSolicitud(SolicitudServicio solicitudServicio, Model model) {
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

    
    /**
     * Este controlador es el encargado de dar de baja una solicitud seleccionada,
     * este proceso solo cambia el estado a un estado cerrado y se persiste en la base 
     * de datos
     * @param solicitudServicio este parametro contiene la informacion del objeto a modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return luego de persistir la informacion se redirige a la pagina que lista todas las solicitudes
     */
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

    

}
