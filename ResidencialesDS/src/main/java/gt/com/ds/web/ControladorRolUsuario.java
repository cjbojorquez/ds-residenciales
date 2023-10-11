package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.Rol;
import gt.com.ds.domain.RolUsuario;
import gt.com.ds.domain.RolUsuarioPK;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.RolService;
import gt.com.ds.servicio.RolUsuarioService;
import gt.com.ds.servicio.UsuarioRolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.UsuarioRol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase permite la asignación de roles a los usuarios
 *
 * @author cjbojorquez
 * 
 */
@Controller
@Slf4j
public class ControladorRolUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResidencialService residencialService;

    @Autowired
    private RolUsuarioService rolUsuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioRolService usuarioRolService;
    @Autowired
    private Varios varios;

    /**
     * Esta funcion permite ver todos los usuarios a los que se les puede asignar un rol
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente, por medio de este 
     * se envia el listado de usuarios a la pagina
     * @return listaUsuarios.html es la pagina donde se mostraran los usuarios
     */
    @GetMapping("/listaUsuarios")
    public String Inicio(Model model) {
        Usuario us = varios.getUsuarioLogueado();
        String rol = varios.getRolLogueado();
        List<Usuario> usuarios;
        List<RolUsuario> rolesUsuario;
        List<UsuarioRol> usuariosRol;
        if (rol.equals("ROLE_ADMIN")) {
            usuarios = usuarioService.listarUsuarios();
            rolesUsuario = rolUsuarioService.listarRoles();
            usuariosRol = usuarioRolService.combinarUsuarioConRol(usuarios, rolesUsuario);
        } else {
            usuarios = usuarioService.listarUsuariosxResidencial(1L, us.getResidencial().getIdResidential());
            rolesUsuario = rolUsuarioService.listarRoles();
            usuariosRol = usuarioRolService.combinarUsuarioConRol(usuarios, rolesUsuario);
        }

        model.addAttribute("usuariosRol", usuariosRol);
        return "listaUsuarios";
    }

    /**
     * Esta función permite cargar un usuario en específico al cual se le asignara un rol
     * determinado
     * @param idUsuario este parametro contiene el id del usuario al que se le desea asignar 
     * un rol
     * @param model por meido de este se envian las variables a la vista, asignarol.html
     * @return aqui se redirecciona a la pagina asignarol.html
     */
    @GetMapping("/asignarol")
    public String asignar(@RequestParam("idUsuario") Long idUsuario, Model model) {
        Long vEstado = 1L;// Roles estado Activo
        Usuario us = varios.getUsuarioLogueado();
        String rolLogueado = varios.getRolLogueado();
        log.info("ingresando a asignarol con id valor =" + idUsuario + ";");
        var rolUsuario = new RolUsuario();
        if (!rolUsuarioService.encontrarRoles(idUsuario).isEmpty()) {
            rolUsuario = rolUsuarioService.encontrarRoles(idUsuario).get(0);
        }
        Rol rol = new Rol();
        List<Rol> roles;
        if(rolLogueado.equals("ROLE_ADMIN")){
            roles = rolService.listarRoles();
        }else{
            roles = rolService.listarRolesNoAdmin();
        }
        log.info("Roles encontrados = " + roles.toString());
        var usuario = usuarioService.encontrarUsuario(idUsuario);
        log.info("Usuario encontrado = " + usuario.getNombre());
        model.addAttribute("rolUsuario", rolUsuario);
        model.addAttribute("roles", roles);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        return "asignarol";
    }

    /**
     * una vez seleccionado el rol a asignar a un usuario se usa esta función para
     * persistir la asignacion del rol
     * @param request este parametro nos sirve para obtener el id del usuario al que se le asignará
     * el rol
     * @param rolUsuario este parametro se utilizará para el correcto guardado de la asignacion 
     * @return luego de persistir la informacion se procede con la redireccion hacia la pagina 
     * listaUsuarios.html
     */
    @PostMapping("/guardarrolus")
    public String guardar(HttpServletRequest request, RolUsuario rolUsuario) {
        String idUsuarioString = request.getParameter("idUsuario");
        Long idUsuario = Long.parseLong(idUsuarioString);
        List<RolUsuario> rolUsuario1 = rolUsuarioService.encontrarRoles(idUsuario);
        if (!rolUsuario1.isEmpty()) {
            RolUsuario rolUsuarioOld = rolUsuario1.get(0);
            rolUsuarioService.eliminar(rolUsuarioOld);
        }
        RolUsuarioPK rolUsuarioPK = new RolUsuarioPK();
        RolUsuario rolUsuarioNew = new RolUsuario();
        rolUsuarioPK.setRol(rolUsuario.getRolUsuario().getRol());
        rolUsuarioPK.setUsuario(usuarioService.encontrarUsuario(idUsuario));
        rolUsuarioNew.setRolUsuario(rolUsuarioPK);
        rolUsuarioService.guardar(rolUsuarioNew);
        return "redirect:/listaUsuarios";
    }

}
