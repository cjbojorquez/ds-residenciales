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
 * Esta clase permite el manejo de roles, creacion, modificación y eliminación
 *
 * @author cjbojorquez
 * 
 */
@Controller
@Slf4j
public class ControladorRol {

    @Autowired
    private RolService rolService;

    @Autowired
    private Varios varios; 
    
    /**
     * Esta función permite listar todos los roles creados y en estado activo 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las solicitudes estas se envian a la pagina rol.html
     */
    @GetMapping("/rol")
    public String Inicio(Model model) {
        var roles = rolService.listarRoles(1L);
        log.info("ejecutando controlador rol " + roles);
        model.addAttribute("roles", roles);
        return "rol";
    }
    
    /**
     * Esta función únicamente se utiliza para redireccionar la creación de un nuevo rol
     * hacia el formulario en el que se deberá ingresar los datos solicitados
     * @param rol Este parametro solamente prepara el objeto para ser llenado en el
     * formulario
     * @return se redirecciona al formulario modificarrol.html
     */
    @GetMapping("/agregarrol")
    public String agregar(Rol rol, Model model) {
        
        return "modificarrol";
    }

    /**
     * Esta función permite el guardado de nuevos roles, asi como el guardado de roles
     * que han sido editados
     * @param rol este parametro contiene el objeto Rol que tiene la informacion del rol
     * que se persistira en base de datos
     * @return luego de guardarse la información se redirecciona a la pagina rol.html
     * que muestra todos los roles activos
     */
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

    /**
     * Esta función permite la edición de un rol
     * @param rol este parametro contiene el id del rol que se desea modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return una vez cargado el rol, se procede con la redireccion y envio hacia
     * la pagina modificarrol.html
     */
    @GetMapping("/editarrol")
    public String editar(Rol rol, Model model) {
        rol = rolService.encontrarRol(rol);
        model.addAttribute("rol", rol);
        
        return "modificarrol";
    }

    /**
     * Esta función permite la eliminacion de un rol determinado
     * @param rol este objeto contiene el id del rol que se desea eliminar
     * @return luego de eliminarse el rol se redirecciona al listado de roles
     */
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
