package gt.com.ds.web;

import gt.com.ds.domain.Rol;
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
import gt.com.ds.servicio.RolService;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorRol {

    @Autowired
    private RolService rolService;

     
    
    @GetMapping("/rol")
    public String Inicio(Model model) {
        var roles = rolService.listarRoles(1L);
        log.info("ejecutando controlador rol " + roles);
        model.addAttribute("roles", roles);
        return "rol";
    }
    
    @GetMapping("/agregarrol")
    public String agregar(Rol rol, Model model) {
        
        return "modificarrol";
    }

    @PostMapping("/guardarrol")
    public String guardar(@Valid Rol rol, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificarrol";
        }
        rol.setEstado(1L);
        if (rol.getIdRol() == null) {
            rol.setFechaCrea(formatter.format(date));
            rol.setUsuarioCrea(1L);
        } else {
            rol.setFechaModifica(formatter.format(date));
            rol.setUsuarioModifica(1L);
        }
        log.info("Se actualiza rol " + rol);
        rolService.guardar(rol);
        return "redirect:/rol";
    }

    @GetMapping("/editarrol")
    public String editar(Rol rol, Model model) {
        rol = rolService.encontrarRol(rol);
        model.addAttribute("rol", rol);
        
        return "modificarrol";
    }

    @GetMapping("/eliminarrol")
    public String eliminar(Rol rol, Model model) {
        rol = rolService.encontrarRol(rol);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        rol.setEstado(0L);
        rol.setFechaModifica(formatter.format(date));
        rol.setUsuarioModifica(1L);
        log.info("Eliminando rol " + rol);
        rolService.guardar(rol);
        return "redirect:/rol";
    }

    

}
