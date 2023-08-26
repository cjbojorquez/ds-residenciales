package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ResidencialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gt.com.ds.servicio.UsuarioService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResidencialService residencialService;
    
    private Residencial residencial;
    
    @GetMapping("/usuario")
    public String Inicio(Model model) {
        var usuarios = usuarioService.listarUsuarios(1L);
        
        log.info("ejecutando controlador usuario " + usuarios);
        model.addAttribute("usuarios", usuarios);
        return "usuario";
    }
    
        
    @GetMapping("/agregarus")
    public String agregar(Usuario Usuario, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user "+residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificarus";
    }

    @PostMapping("/guardarus")
    public String guardar(@Valid Usuario usuario, @RequestParam("file") MultipartFile imagen, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificarus";
        }
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
                usuario.setFoto("images/perfil/" + imagen.getOriginalFilename());
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        usuario.setEsEmpleado(0L);
        usuario.setEstado(1L);
        
        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(formatter.format(date));
            usuario.setUsuarioCrea(1L);
            usuario.setResidencial(residencialService.encontrarPorId(1L));
        } else {
            usuario.setFechaModifica(formatter.format(date));
            usuario.setUsuarioModifica(1L);
        }
        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        return "redirect:/usuario";
    }

    @GetMapping("/editarus")
    public String editar(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user "+residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificarus";
    }

    @GetMapping("/eliminarus")
    public String eliminar(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(formatter.format(date));
        usuario.setUsuarioModifica(1L);
        usuarioService.guardar(usuario);
        return "redirect:/usuario";
    }

    /**
     * EMPLEADOS
     */
    @GetMapping("/empleado")
    public String InicioEmpleado(Model model) {
        var usuarios = usuarioService.listarEmpleados(1L);
        var mensaje = "Hola mundo con Thymeleaf para el home";
        log.info("ejecutando controlador empleado spring mvc " + usuarios);
        
        model.addAttribute("usuarios", usuarios);
        return "empleado";
    }
    
    @GetMapping("/agregaremp")
    public String agregarEmpleado(Usuario Usuario, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user "+residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificaremp";
    }

    @PostMapping("/guardaremp")
    public String guardarEmpleado(@Valid Usuario usuario, @RequestParam("file") MultipartFile imagen, Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        if (errors.hasErrors()) {
            return "modificaremp";
        }
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
                usuario.setFoto("images/perfil/" + imagen.getOriginalFilename());
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        usuario.setEsEmpleado(0L);
        usuario.setEstado(1L);
        
        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(formatter.format(date));
            usuario.setUsuarioCrea(1L);
            
            usuario.setResidencial(residencialService.encontrarPorId(1L));
        } else {
            usuario.setFechaModifica(formatter.format(date));
            usuario.setUsuarioModifica(1L);
        }
        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        return "redirect:/empleado";
    }

    @GetMapping("/editaremp")
    public String editarEmpleado(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user "+residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificaremp";
    }

    @GetMapping("/eliminaremp")
    public String eliminarEmpleado(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(formatter.format(date));
        usuario.setUsuarioModifica(1L);
        usuarioService.guardar(usuario);
        return "redirect:/empleado";
    }

}
