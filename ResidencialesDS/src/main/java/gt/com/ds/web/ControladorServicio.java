package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.Rol;
import gt.com.ds.domain.Servicio;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ResidencialService;
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
import gt.com.ds.servicio.ServicioService;
import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;

/**
 * Esta clase permite la creacion, modificacion y eliminacion de tipos de servicios que 
 * podra manejar una residencial
 *
 * @author cjbojorquez
 * 
 */
@Controller
@Slf4j
public class ControladorServicio {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ResidencialService residencialService;
    
    @Autowired
    private Varios varios; 
    
    /**
     * Esta función permite listar todos los servicios creados y en estado activo 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las solicitudes estas se envian a la pagina servicio.html
     */
    @GetMapping("/servicio")
    public String Inicio(Model model) {
        var servicios = servicioService.listarServicios(1L);
        log.info("ejecutando controlador servicio " + servicios);
        model.addAttribute("servicios", servicios);
        return "servicio";
    }
    
    /**
     * La creación de nuevos servicos se hace por medio de esta función, se procede a la 
     * redirección para permitir crear los servicios
     * 
     * @param servicio este parametro aunque no lleva informacion, permite que el formulario
     * se pueda manejar de una mejor forma
     * @return se redirecciona al formulario modificarserv.html para ingresar los datos
     * y posteriormente proceder con el guardado
     */
    @GetMapping("/agregarserv")
    public String agregar(Servicio servicio, Model model) {
        Usuario us = varios.getUsuarioLogueado();
        var empleados = usuarioService.listarEmpleados(1L);
        
        model.addAttribute("empleados", empleados);
        return "modificarserv";
    }

    /**
     * Esta funcion recibe el objeto servico para ser guardado en la base de datos
     * @param servicio este es el parametro que contiene la informacion del servicio a crear
     * @param errors
     * @return una vez guardado el servicio, se redirige hacia el listado de todos los servicios
     * activos
     */
    @PostMapping("/guardarserv")
    public String guardar(@Valid Servicio servicio, Errors errors) {
        Usuario us = varios.getUsuarioLogueado();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        var residencial = residencialService.encontrarPorId(us.getResidencial().getIdResidential());
        
        if (errors.hasErrors()) {
            log.info("errores en guardar serv " + errors);
            return "modificarserv";
        }
        servicio.setEstado(1L);
        if (servicio.getIdServicio() == null) {
            servicio.setFechaCrea(Tools.now());
            servicio.setUsuarioCrea(us.getIdUsuario());
            servicio.setResidencial(residencial);
        } else {
            servicio.setFechaModifica(Tools.now());
            servicio.setUsuarioModifica(us.getIdUsuario());
        }
        log.info("Se actualiza servicio " + servicio);
        servicioService.guardar(servicio);
        return "redirect:/servicio";
    }

    /**
     * Esta función recibe un servico editado y lo persiste en la base de datos
     * @param servicio este es el objeto que contiene la informacion del servico editado
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return 
     */
    @GetMapping("/editarserv")
    public String editar(Servicio servicio, Model model) {
        servicio = servicioService.encontrarServicio(servicio);
        model.addAttribute("servicio", servicio);
        var empleados = usuarioService.listarEmpleados(1L);
        log.info("Emp desde user "+empleados);
        model.addAttribute("empleados", empleados);
        return "modificarserv";
    }

    /**
     * Por medio de esta función se procede a dar de baja a los servicos
     * @param servicio en este parametro va el id del servicio que se desea dar de baja
     * @return una vez dado de baja el servico, se redirecciona hacia la pagina servicio.html
     * que es la que muestra el listado de servicios activos
     */
    @GetMapping("/eliminarserv")
    public String eliminar(Servicio servicio, Model model) {
        Usuario us = varios.getUsuarioLogueado();
        servicio = servicioService.encontrarServicio(servicio);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        servicio.setEstado(0L);
        servicio.setFechaModifica(Tools.now());
        servicio.setUsuarioModifica(us.getIdUsuario());
        log.info("Eliminando servicio " + servicio);
        servicioService.guardar(servicio);
        return "redirect:/servicio";
    }


}
