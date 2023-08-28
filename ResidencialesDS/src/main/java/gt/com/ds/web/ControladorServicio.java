package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.Rol;
import gt.com.ds.domain.Servicio;
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
import gt.com.ds.util.Tools;

/**
 *
 * @author cjbojorquez
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
    
    @GetMapping("/servicio")
    public String Inicio(Model model) {
        var servicios = servicioService.listarServicios(1L);
        log.info("ejecutando controlador servicio " + servicios);
        model.addAttribute("servicios", servicios);
        return "servicio";
    }
    
    @GetMapping("/agregarserv")
    public String agregar(Servicio servicio, Model model) {
        var empleados = usuarioService.listarEmpleados(1L);
        log.info("Emp desde servicio "+empleados);
        model.addAttribute("empleados", empleados);
        return "modificarserv";
    }

    @PostMapping("/guardarserv")
    public String guardar(@Valid Servicio servicio, Errors errors) {
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        var residencial = residencialService.encontrarPorId(1L);
        
        if (errors.hasErrors()) {
            log.info("errores en guardar serv " + errors);
            return "modificarserv";
        }
        servicio.setEstado(1L);
        if (servicio.getIdServicio() == null) {
            servicio.setFechaCrea(Tools.now());
            servicio.setUsuarioCrea(1L);
            servicio.setResidencial(residencial);
        } else {
            servicio.setFechaModifica(Tools.now());
            servicio.setUsuarioModifica(1L);
        }
        log.info("Se actualiza servicio " + servicio);
        servicioService.guardar(servicio);
        return "redirect:/servicio";
    }

    @GetMapping("/editarserv")
    public String editar(Servicio servicio, Model model) {
        servicio = servicioService.encontrarServicio(servicio);
        model.addAttribute("servicio", servicio);
        var empleados = usuarioService.listarEmpleados(1L);
        log.info("Emp desde user "+empleados);
        model.addAttribute("empleados", empleados);
        return "modificarserv";
    }

    @GetMapping("/eliminarserv")
    public String eliminar(Servicio servicio, Model model) {
        servicio = servicioService.encontrarServicio(servicio);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        servicio.setEstado(0L);
        servicio.setFechaModifica(Tools.now());
        servicio.setUsuarioModifica(1L);
        log.info("Eliminando servicio " + servicio);
        servicioService.guardar(servicio);
        return "redirect:/servicio";
    }

    @GetMapping("/error")
    public String error(Model model,Errors errors) {
        log.info("los errores son:" + errors);
        model.addAttribute("errors", errors);
        
        return "servicio";
    }

}
