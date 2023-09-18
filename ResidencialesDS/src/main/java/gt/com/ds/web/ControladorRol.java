package gt.com.ds.web;

import gt.com.ds.domain.Rol;
import gt.com.ds.domain.Usuario;
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
import gt.com.ds.servicio.RolService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorRol {

    @Autowired
    private RolService rolService;

    @Autowired
    private Varios varios; 
    
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
        Usuario us = varios.getUsuarioLogueado();
        if (errors.hasErrors()) {
            return "modificarrol";
        }
        rol.setEstado(1L);
        if (rol.getIdRol() == null) {
            rol.setFechaCrea(Tools.now());
            rol.setUsuarioCrea(us.getIdUsuario());
        } else {
            rol.setFechaModifica(Tools.now());
            rol.setUsuarioModifica(us.getIdUsuario());
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
        Usuario us = varios.getUsuarioLogueado();
        rol.setEstado(0L);
        rol.setFechaModifica(Tools.now());
        rol.setUsuarioModifica(us.getIdUsuario());
        log.info("Eliminando rol " + rol);
        rolService.guardar(rol);
        return "redirect:/rol";
    }

    

}
