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
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.EmailService;
import gt.com.ds.util.Tools;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private Varios varios;
    
    @Value("${host.name}")
    String dominio;

    @Value("${jwt.expiracion}")
    private Long expiracionMs;

    //////////////////////////////////////////////////////////////////////
    //     USUARIOS
    /////////////////////////////////////////////////////////////////////
    @GetMapping("/registro")
    public String registrar(@RequestParam("token") String token, Model model) {
        logoff();
        token = Tools.decodeTokenFromURL(token);
        System.out.println("token = " + token);
        String parametro = Tools.paginaSegura(token);
        if (parametro.equals("errores/401")) {
            return parametro;
        } else {
            System.out.println("parametro = " + parametro);
            Usuario us = usuarioService.encontrarUsuario(Long.parseLong(parametro));
            System.out.println("us = " + us);
            model.addAttribute("usuario", us);
            return "/userconf";
        }
    }

    @GetMapping("/cambiapass")
    public String cambiaContrasena(Model model) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        System.out.println("Usuario cambia contraseña = ");
        Usuario us = usuarioService.encontrarUsuario(usuarioLogueado.getIdUsuario());
        System.out.println("us = " + us);
        model.addAttribute("usuario", us);
        return "/userconfauth";

    }

    /*@PostMapping("/perfil")
    public String perfil(Usuario usuario, Model model) {

        model.addAttribute("usuario", usuario);
        log.info("se cargara perfil :" + usuario);
        return "perfil";
    }*/
    @PostMapping("/guardarperfil")
    public String guardarPerfil(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("newpassword") String newPassword, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {

        System.out.println("newPassword = " + newPassword);
        log.info("newPassword = " + newPassword);
        if (!"".equals(newPassword)) {
            usuario.setPassword(newPassword);
        }
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                usuario.setFoto("images/perfil/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        usuario.setEstado(1L);

        usuario.setFechaModifica(Tools.now());
        log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

        usuario.setUsuarioModifica(usuario.getIdUsuario());

        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        model.addAttribute("usuario", usuario);
        return "perfil";
        //return "redirect:/";
    }

    @PostMapping("/guardarcontrasena")
    public String guardarContrasena(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("newpassword") String newPassword,
            @RequestParam("newpasswordconfirm") String newPasswordConfirm, Model model, Errors errors) {

        if ("".equals(newPasswordConfirm) || "".equals(newPassword)) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("contraseñas en blanco para usuario:" + usuario);
            bindingResult.rejectValue("nombre", "error.nombre", "Las contraseñas pueden estar en blanco");
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("contraseñas diferentes para usuario:" + usuario);
            bindingResult.rejectValue("nombre", "error.nombre", "Las contraseñas no coinciden!");
        }
        if (errors.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "userconf";
        }
        usuario.setPassword(Tools.encriptarPassword(newPassword));

        usuario.setEstado(1L);

        usuario.setFechaModifica(Tools.now());
        log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

        usuario.setUsuarioModifica(usuario.getIdUsuario());

        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        model.addAttribute("usuario", usuario);
        //return "/";
        return "redirect:/";
    }

    ////////////////////////////////////////////////////////////////////
    //                      ADMINISTRACION
    ///////////////////////////////////////////////////////////////////
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
        model.addAttribute("residenciales", residenciales);
        return "crearus";
    }

    @PostMapping("/guardarus")
    public String guardar(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        if (usuarioService.encontrarUsuario(usuario.getNombreUsuario()) != null && usuario.getIdUsuario() == null) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("Existe usuario");
            bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "El nombre de usuairo ya existe!");
        }
        if (errors.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "crearus";
        }
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                usuario.setFoto("images/perfil/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        usuario.setEsEmpleado(0L);
        usuario.setEstado(1L);

        log.info("Guarda usuario " + Tools.now() + " " + usuarioLogueado.getIdUsuario());
        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(Tools.now());
            usuario.setUsuarioCrea(1L);
            usuario.setResidencial(residencialService.encontrarPorId(1L));
        } else {

            usuario.setFechaModifica(Tools.now());
            log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

            usuario.setUsuarioModifica(usuarioLogueado.getIdUsuario());
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
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificarus";
    }

    @GetMapping("/eliminarus")
    public String eliminar(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
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
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "crearemp";
    }

    @PostMapping("/guardaremp")
    public String guardarEmpleado(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {
        if (usuarioService.encontrarUsuario(usuario.getNombreUsuario()) != null && usuario.getIdUsuario() == null) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("Existe usuario");
            bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "El nombre de usuairo ya existe!");
        }
        if (errors.hasErrors()) {
            var residenciales = residencialService.listarRecidencialesActivas();
            model.addAttribute("residenciales", residenciales);
            return "crearemp";
        }
        if (!imagen.isEmpty()) {
            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");

            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                usuario.setFoto("images/perfil/" + nombreArchivo);
                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
                Files.write(rutaCompleta, byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        usuario.setEsEmpleado(1L);
        usuario.setEstado(1L);

        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(Tools.now());
            usuario.setUsuarioCrea(1L);

            //usuario.setResidencial(residencialService.encontrarPorId(1L));
        } else {
            usuario.setFechaModifica(Tools.now());
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
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificaremp";
    }

    @GetMapping("/eliminaremp")
    public String eliminarEmpleado(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(1L);
        usuarioService.guardar(usuario);
        return "redirect:/empleado";
    }

    ////////////////////////////////////////////////////////////////////
    //                      USUARIO DE RESIDENCIAL
    ///////////////////////////////////////////////////////////////////
    @GetMapping("/usuariores")
    public String usuarioRes(Model model) {
        var usuarios = usuarioService.listarUsuariosResidencial(1L, getResidencial().getIdResidential());
        model.addAttribute("usuarios", usuarios);
        return "usuariores";
    }

    @GetMapping("/agregarusres")
    public String agregarUsres(Usuario Usuario, Model model) {
        //model.addAttribute("idRes", getIdResidencial());
        return "crearusres";
    }

    @PostMapping("/guardarusres")
    public String guardarusres(@Valid Usuario usuario, BindingResult bindingResult, Model model, Errors errors) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        int nuevo = 0;
        if (usuarioService.encontrarUsuario(usuario.getNombreUsuario()) != null && usuario.getIdUsuario() == null) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("Existe usuario");
            bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "El nombre de usuairo ya existe!");
        }
        if (errors.hasErrors()) {
            var residenciales = residencialService.listarRecidencialesActivas();
            model.addAttribute("residenciales", residenciales);
            return "crearusres";
        }

        usuario.setEsEmpleado(0L);
        usuario.setEstado(1L);

        log.info("Guarda usuario " + Tools.now() + " " + usuario.getIdUsuario());
        if (usuario.getIdUsuario() == null) {
            nuevo = 1;
            usuario.setFechaCrea(Tools.now());
            usuario.setUsuarioCrea(usuarioLogueado.getIdUsuario());
            usuario.setResidencial(usuarioLogueado.getResidencial());
        } else {

            usuario.setFechaModifica(Tools.now());
            log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

            usuario.setUsuarioModifica(usuarioLogueado.getIdUsuario());
        }
        log.info("Se actualiza usuario " + usuario);
        Long idUs = usuarioService.guardar(usuario);

        return "redirect:/usuariores";
    }

    @GetMapping("/editarusres")
    public String editarusres(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "modificarusres";
    }

    @GetMapping("/eliminarusres")
    public String eliminarusres(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(1L);
        usuarioService.guardar(usuario);
        return "redirect:/usuariores";
    }

    //////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/invitacion")
    public String enviaInvitacion(Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        usuario = usuarioService.encontrarUsuario(usuario);

        log.info("en invitados se busca al usuario:" + usuario);
        String token = Tools.generarToken(usuario.getIdUsuario().toString(), expiracionMs);
        token = Tools.decodeTokenFromURL(token);
        String url = dominio + "registro?token=" + token;
        String mensaje = "<h2> Estimad@:  " + usuario.getNombre() + "</h2><br><br>"
                + "<p>Te enviamos este link para que puedas finalizar la configuracion de tu usuario:<br> "
                + url + "</p>"
                + "<p>Atte. " + usuario.getResidencial().getName() + "</p>";
        varios.sendEmail(usuario.getEmail(), "Resgitro de usuario", mensaje, usuario.getResidencial().getEmail());
        redirectAttributes.addFlashAttribute("mensajeExito", "La invitación se ha enviado exitosamente.");

        return "redirect:/usuariores";
    }

    @PostMapping("/recuperacontrasena")
    public String recuperaContrasena(@RequestParam("username") String nombreUsuario, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.encontrarUsuario(nombreUsuario);
        log.info("buscando usuario:" + nombreUsuario + " usuario encontrado:" + usuario);
        if (usuario != null) {
            log.info("Reseteo de contraseña, usuario encontrado:" + usuario);
            String token = Tools.generarToken(usuario.getIdUsuario().toString(), expiracionMs);
            token = Tools.decodeTokenFromURL(token);
            String url = dominio + "registro?token=" + token;
            String mensaje = "<h2> Estimad@:  " + usuario.getNombre() + "</h2><br><br>"
                    + "<p>Te enviamos este link para que puedas recuperar tu contraseña: <br> "
                    + url + "</p>"
                    + "<p>Atte. " + usuario.getResidencial().getName() + "</p>";
            varios.sendEmail(usuario.getEmail(), "Resgitro de usuario", mensaje, usuario.getResidencial().getEmail());
            redirectAttributes.addFlashAttribute("mensajeExito", "Te enviamos un link a tu correo.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "¡Ocurrio un error intente mas tarde!");

        }
        return "redirect:/recupera";

    }

    public Residencial getResidencial() {
        // Obtén el objeto Authentication del contexto de seguridad actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica si el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = usuarioService.encontrarUsuario(username);
            System.out.println("Nombre de usuario: " + username);
            return usuario.getResidencial();
        } else {
            // El usuario no está autenticado
            System.out.println("Usuario no autenticado");
            return null;
        }
    }

    

    /*public ResponseEntity<?> sendEmail(String[] to, String asunto, String mensaje, String origen) {
        try {

            emailService.sendSimpleMessage(to, asunto, mensaje, origen);
            System.out.println("mensaje = " + mensaje + " to: " + to + " asunto: " + asunto);
            return ResponseEntity.ok("Correo electrónico enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
        }
    }*/

    public void logoff() {
        // Invalida la sesión actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
