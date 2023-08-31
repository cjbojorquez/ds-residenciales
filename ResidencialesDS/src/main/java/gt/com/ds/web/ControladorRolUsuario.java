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
 *
 * @author cjbojorquez
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

    @GetMapping("/listaUsuarios")
    public String Inicio(Model model) {
        
        var usuarios = usuarioService.listarUsuarios();
        log.info("Usuarios  --------------------    " + usuarios.toString());
        var rolesUsuario = rolUsuarioService.listarRoles();
        var usuariosRol = usuarioRolService.combinarUsuarioConRol(usuarios,rolesUsuario);
        log.info("Roles  --------------------    " + usuariosRol.toString());
        
        model.addAttribute("usuariosRol", usuariosRol);
        return "listaUsuarios";
    }
    
    @GetMapping("/asignarol")
    public String asignar(@RequestParam("idUsuario") Long idUsuario, Model model) {
        Long vEstado = 1L;// Roles estado Activo

        log.info("ingresando a asignarol con id valor =" + idUsuario + ";");
        var rolUsuario=new RolUsuario();
        if(!rolUsuarioService.encontrarRoles(idUsuario).isEmpty()){
            rolUsuario = rolUsuarioService.encontrarRoles(idUsuario).get(0);
        }
        Rol rol = new Rol();
        var roles = rolService.listarRoles(vEstado);
        log.info("Roles encontrados = " + roles.toString());
        var usuario = usuarioService.encontrarUsuario(idUsuario);
        log.info("Usuario encontrado = " + usuario.getNombre());
        model.addAttribute("rolUsuario", rolUsuario);
        model.addAttribute("roles", roles);
        model.addAttribute("usuario", usuario);
        model.addAttribute("rol", rol);
        return "asignarol";
    }

    @PostMapping("/guardarrolus")
    public String guardar(HttpServletRequest request, RolUsuario rolUsuario) {
        String idUsuarioString = request.getParameter("idUsuario");
        Long idUsuario = Long.parseLong(idUsuarioString);
        List<RolUsuario> rolUsuario1= rolUsuarioService.encontrarRoles(idUsuario);
        if(!rolUsuario1.isEmpty()){
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
