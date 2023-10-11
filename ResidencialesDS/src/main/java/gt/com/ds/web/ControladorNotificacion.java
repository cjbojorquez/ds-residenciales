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
 * Esta clase permite el manejo de notificaciones, tanto las generales como las específicas,
 * las notificaciones se identifican de la siguiente forma:  
 *          G = Notificación General
 *          E = Notificación Específica
 *
 * @author cjbojorquez
 * 
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

    /**
     * Esta función permite listar todas las notificaciones creadas y en estado activo 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las notificaciones estas se envian a la pagina general.html
     */
    @GetMapping("/general")
    public String InicioGeneral(Model model) {
        
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        var notificaciones = notificacionService.notificacionPorTipo(varNotiGeneral, usuarioLogueado.getResidencial().getIdResidential());//agregar residencial
        log.info("estas son las notificaciones " + notificaciones.toString());
        model.addAttribute("notificaciones", notificaciones);

        return "general";
    }

    /**
     * Esta función redirige la creacion de una notificacion  general hacia el formulario de 
     * modificación
     * @param notificacion este parametro se utiliza para enviarse al formulario de modificación
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return aqui se redirige hacia el formulario modificageneral.html
     */
    @GetMapping("/agregargeneral")
    public String agregarGeneral(Notificacion notificacion, Model model) {

        return "modificargeneral";
    }

    /**
     * Esta función permite guardar las notificaciones creadas como modificadas
     * @param notificacion este parametro recibe las notificaciones para ser persistidas en base
     * de datos
     * @param imagen este parametro permite que se puedan adjuntar archivos 
     * @param desdeFecha este parametro indica la fecha inicial en la que aplica la notificación
     * @param desdeHora este parametro indica la hora inicial en la que aplica la notificación
     * @param hastaFecha este parametro indica la fecha final en la que aplica la notificación
     * @param hastaHora este parametro indica la hora final en la que aplica la notificación
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return luego de la persistencia del objeto en la base de datos se procede a redirigir  a la pagina de visualizacion de la
     * notificacion
     */
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

    /**
     * Al acceder a la modificación de la notificacion se cargan las variables necesarias 
     * para ser cargadas al formulario
     * @param notificacion este parametro contiene la informacion del objeto a modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return las variables que se manejan dentro de la funcion son enviadas al formulario
     * modificargeneral.html
     */
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

    /**
     * Este controlador permite la visualizacion de la notificacion, ya sea para editar como para 
     * enviarse a los usuarios
     * @param notificacion este parametro contiene la informacion del objeto a mostrar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return las variables que se manejan dentro de la funcion son enviadas al formulario
     * vergeneral.html
     */
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

    /**
     * Este controlador permite el envio de la notificacion, tanto por correo como por la 
     * misma aplicacion
     * @param notificacion este parametro contiene la informacion del objeto a enviar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return luego de enviarse el mensaje, se redirecciona al listado general de notificaciones
     */
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

    /**
     * Esta función permite la eliminacion de la notificación 
     * @param notificacion este parametro contiene la informacion del objeto a eliminar
     */
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
    
    /**
     * Esta función permite listar todas las notificaciones creadas y en estado activo 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las notificaciones estas se envian a la pagina especifica.html
     */
    @GetMapping("/especifica")
    public String InicioEspecifica(Model model) {
        
        Usuario us = varios.getUsuarioLogueado();
        var notificaciones = notificacionService.notificacionPorTipo(varNotiEspecifica, us.getResidencial().getIdResidential());//agregar residencial

        model.addAttribute("notificaciones", notificaciones);
        return "especifica";
    }

    /**
     * Esta función redirige la creacion de una notificacion  específica hacia el formulario de 
     * modificación
     * @param notificacion este parametro se utiliza para enviarse al formulario de modificación
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return aqui se redirige hacia el formulario modificaespecifica.html
     */
    @GetMapping("/agregarespecifica")
    public String agregarEspecifica(Notificacion notificacion, Model model) {
        var usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "modificarespecifica";
    }

    /**
     * Esta función permite guardar las notificaciones creadas como modificadas
     * @param notificacion este parametro recibe las notificaciones para ser persistidas en base
     * de datos
     * @param imagen este parametro permite que se puedan adjuntar archivos 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return luego de la persistencia del objeto en la base de datos se procede a redirigir  a la pagina de visualizacion de la
     * notificacion
     */
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

    /**
     * Al acceder a la modificación de la notificacion se cargan las variables necesarias 
     * para ser cargadas al formulario
     * @param notificacion este parametro contiene la informacion del objeto a modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return las variables que se manejan dentro de la funcion son enviadas al formulario
     * modificarespecifica.html
     */
    @GetMapping("/modificarespecifica")
    public String editarEspecifica(Notificacion notificacion, Model model) {
        Usuario us =  varios.getUsuarioLogueado();
        notificacion = notificacionService.encontrarNotificacion(notificacion);
        var estadosTicket = estadoTicketService.listarEstadoTicket();
        var usuarios = usuarioService.listarEmpleadosResidencial(1L, us.getIdUsuario());
        if (notificacion.getEstado().getIdEstado() == 1L) {
            EstadoTicket estadoTicket = estadoTicketService.encontrarEstado(2L);
            notificacion.setEstado(estadoTicket);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("notificacion", notificacion);
        log.info("se envia ticket " + notificacion.toString());
        return "modificarespecifica";
    }

    /**
     * Este controlador permite la visualizacion de la notificacion, ya sea para editar como para 
     * enviarse a los usuarios
     * @param notificacion este parametro contiene la informacion del objeto a mostrar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return las variables que se manejan dentro de la funcion son enviadas al formulario
     * verespecifica.html
     */
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

    /**
     * Este controlador permite el envio de la notificacion, tanto por correo como por la 
     * misma aplicacion
     * @param notificacion este parametro contiene la informacion del objeto a enviar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return luego de enviarse el mensaje, se redirecciona al listado general de notificaciones
     */
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

    /**
     * Esta función permite la eliminacion de la notificación 
     * @param notificacion este parametro contiene la informacion del objeto a eliminar
     */
    @GetMapping("/cerrarespecifica")
    public String eliminarEspecifica(Notificacion notificacion, Model model) {
        //ticket = notificacionService.encontrarTicket(ticket);
        notificacion.setEstado(estadoTicketService.encontrarEstado(4L));

        //log.info("Eliminando gestion " + rol);
        notificacionService.guardar(notificacion);
        return "redirect:/especifica";
    }

    /**
     * esta función permite la creacion de los mensajes que se enviaran por correo como los que se enviaran
     * al buzon del usuario en esta aplicacion
     * @param notificacion este parametro contiene la informacion del objeto a enviar en el mensaje
     * @return se retornan un array de string que contiene el mensaje con formato para enviarse por correo, como 
     * el mensaje que se enviara al buzon en esta aplicacion
     */
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
