package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.RolUsuario;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.RolUsuarioService;
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
 * Esta clase es el controler para todo lo relacionado con la creacio y
 * mantenimiento de usuarios y empleados.
 *
 * @author cjbojorquez
 *
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
    
    @Value("${static.ruta}")
    String stRuta;
    
    @Value("${static.imagen}")
    String stImagen;
    
    @Value("${static.perfil}")
    String stPerfil;
    
    @Value("${static.adjunto}")
    String stAdjunto;
    
    @Value("${static.logo}")
    String stLogo;
    
    /*@Value("${host.name}")
    String dominio;
    */
    @Value("${jwt.expiracion}")
    private Long expiracionMs;

    @Autowired
    private RolUsuarioService rolUsuarioService;

    private Long estadoActivo = 1L;
    private Long estadoInactivo = 0L;

    //////////////////////////////////////////////////////////////////////
    //     USUARIOS
    /////////////////////////////////////////////////////////////////////
    /**
     * la función registrar es la que se ejecuta cuando un usuario recibe la
     * invitacion para registrarse
     *
     * @param token es el token que se le envia por medio de enlace al correo y
     * que le permitira autenticarse para finalizar su registro
     *
     * @param model el parametro model permite enviar variables hacia las
     * paginas html
     * @return en el return se indica hacia donde se va a redirigir al usuario
     */
    @GetMapping("/registro")
    public String registrar(@RequestParam("token") String token, Model model) {
        System.out.println("token = " + token);
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
            return "userconf";
        }
    }

    /**
     * esta funcion nos permite cargar la informacion del usuario que realizará
     * un cambio de contraseña
     *
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return
     */
    @GetMapping("/cambiapass")
    public String cambiaContrasena(Model model) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        System.out.println("Usuario cambia contraseña = ");
        Usuario us = usuarioService.encontrarUsuario(usuarioLogueado.getIdUsuario());
        System.out.println("us = " + us);
        model.addAttribute("usuario", us);
        return "userconfauth";

    }

    /**
     * Esta función carga la informacion del perfil del usuario al formulario
     * html para que el usuario pueda actualizar sus datos
     *
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return
     */
    @GetMapping("/perfil")
    public String perfil(Model model) {
        var usuario = varios.getUsuarioLogueado();
        model.addAttribute("usuario", usuario);
        log.info("se cargara perfil :" + usuario);
        return "perfil";
    }

    /**
     * Esta función realiza el guardado del perfil de usuario
     *
     * @param usuario este parametro es el que trae la informacion del usuario
     * que se cargo en el formulario y que el usuario pudo haber actualizado,
     * este objeto es enviado desde el form.
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC donde
     * esta el error.
     *
     * @param imagen este parametro es el que se encarga de manejar las imagenes
     * al momento que el usuario decida adjuntar una.
     *
     * @return se retorna al formulario perfil
     */
    @PostMapping("/guardarperfil")
    public String guardarPerfil(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {

        /*System.out.println("newPassword = " + newPassword);
        log.info("newPassword = " + newPassword);
        if (!"".equals(newPassword)) {
            usuario.setPassword(newPassword);
        }*/
        
//        if (!imagen.isEmpty()) {
//            Path directorioImagenes = Paths.get("src//main//resources//static//images//perfil");
//
//            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
//            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
//            try {
//                byte[] byteImg = imagen.getBytes();
//                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
//                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
//                usuario.setFoto(stPerfil + nombreArchivo);
//                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
//                Files.write(rutaCompleta, byteImg);
//            } catch (IOException ex) {
//                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
        //usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        usuario.setFechaModifica(Tools.now());
        log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

        usuario.setUsuarioModifica(usuario.getIdUsuario());

        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        model.addAttribute("usuario", usuario);
        return "perfil";
        //return "redirect:/";
    }

    /**
     * Esta función guarda la nueva contraseña ingresada por el usuario
     *
     * @param usuario el parametro usuario es de tipo Usuario y contiene toda la
     * informacion del usuario que esta cambiando su contraseña.
     *
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param newPassword este parametro contiene la nueva contraseña que
     * actualizará el usuario
     * @param newPasswordConfirm el usuario confirma su contraseña en este
     * parametro
     * @param model nos permite enviar variables hacia el formulario o pagina
     * @return al finalizar el cambio de contraseña se redirige al usuario al
     * home
     */
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
    /**
     * la función Inicio, carga el listado de usuarios, esta funcion es la
     * utilizada por el Administrador ya que muestra todos los usuarios
     * existentes
     *
     * @param model
     * @return se realiza la redireccion al formulario usuario.html
     */
    @GetMapping("/usuario")
    public String Inicio(Model model) {
        var usuarios = usuarioService.listarUsuarios(1L);

        log.info("ejecutando controlador usuario " + usuarios);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("dominio", dominio);
        model.addAttribute("stperfil", stPerfil);
        return "usuario";
    }

    /**
     * Esta funcion permite crear un usuario nuevo
     *
     * @param usuario se inicializa el objeto usuario
     * @param model
     * @return
     */
    @GetMapping("/agregarus")
    public String agregar(Usuario usuario, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        model.addAttribute("residenciales", residenciales);
        return "crearus";
    }

    /**
     * desde el formulario crearus.html se envia la informacion del usuario a
     * guardar
     *
     * @param usuario este parametro contiene la informacion del usuario a
     * guardar, ya sea por creacion de uno nuevo como por la edición de uno
     * existente.
     *
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param imagen parametro que permite la carga de imagenes
     * @param model
     * @param errors
     * @return despues de guardado el usuario, se procede a redireccionar hacia
     * la pagina que muestra todos los usuarios creados
     */
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
//        if (!imagen.isEmpty()) {
//            Path directorioImagenes = Paths.get("/home/ubuntu/files/images/perfil");
//
//            String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
//            log.info("Ruta absoluta " + rutaAbsoluta + " " + directorioImagenes.toString());
//            try {
//                byte[] byteImg = imagen.getBytes();
//                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); //cambiar por dinamico
//                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
//                //Path rutaCompleta = Paths.get("src//main//resources//static//images//perfil//" + nombreArchivo);
//                usuario.setFoto("images/perfil/" + nombreArchivo);
//                log.info("Se intenta guardar imagen " + rutaCompleta.toString());
//                //Files.write(rutaCompleta, byteImg);
//                Files.write(rutaCompleta, byteImg);
//            } catch (IOException ex) {
//                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
        usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        usuario.setEsEmpleado(0L);
        usuario.setEstado(1L);

        //log.info("Guarda usuario " + Tools.now() + " " + usuarioLogueado.getIdUsuario());
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
        Long nuevoUsuario = usuarioService.guardar(usuario);

        return "redirect:/usuario";
    }

    /**
     * por medio de esta función se carga la informacion de un usuario
     * específico se envia el id para buscarlo por este y redireccionarlo hacia
     * el formulario de edición
     *
     * @param usuario parametro que lleva el id del usuairo a modificar
     * @param model
     * @return se redirecciona al formulario modificarus.html para la edicion
     * del usuario
     */
    @GetMapping("/editarus")
    public String editar(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        model.addAttribute("dominio", dominio);
        model.addAttribute("stperfil", stPerfil);
        return "modificarus";
    }

    /**
     * la función de eliminacion cambia el estado del registro
     *
     * @param usuario este parametro contiene el id del usuario a eliminar
     * @param model
     * @return redirecciona a la pagina usuario.html donde se muestran todos los
     * usuarios
     */
    @GetMapping("/eliminarus")
    public String eliminar(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        Usuario us = varios.getUsuarioLogueado();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(us.getIdUsuario());
        usuarioService.guardar(usuario);
        return "redirect:/usuario";
    }

    /**
     * EMPLEADOS
     */
    /**
     * en esta pagina se muestan todos los empleados activos
     *
     * @param model
     * @return los empleados son enviados a la pagina empleado.html
     */
    @GetMapping("/empleado")
    public String InicioEmpleado(Model model) {
        var usuarios = usuarioService.listarEmpleados(1L);
        var mensaje = "Hola mundo con Thymeleaf para el home";
        log.info("ejecutando controlador empleado spring mvc " + usuarios);

        model.addAttribute("usuarios", usuarios);
        return "empleado";
    }

    /**
     * por medio de esta función se preparan las variables necesarias para la
     * creacion de empleados
     *
     * @param Usuario
     * @param model
     * @return se redirecciona hacia la pagina crearemp.html
     */
    @GetMapping("/agregaremp")
    public String agregarEmpleado(Usuario Usuario, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "crearemp";
    }

    /**
     *
     * Esta función se utiliza para guardar empleados.
     *
     * @param usuario este parametro tiene la información del empleado a crear
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param imagen con este parametro podemos manejar las imagenes cargadas
     * @param model
     * @param errors
     * @return una vez guardado el empleado, se redirecciona hacia la pagina
     * empleado.html
     */
    @PostMapping("/guardaremp")
    public String guardarEmpleado(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {
        Usuario us = varios.getUsuarioLogueado();
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
        /*if (!imagen.isEmpty()) {
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

        }*/
        usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        usuario.setEsEmpleado(1L);
        usuario.setEstado(1L);

        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(Tools.now());
            usuario.setUsuarioCrea(us.getIdUsuario());

            //usuario.setResidencial(residencialService.encontrarPorId(1L));
        } else {
            usuario.setFechaModifica(Tools.now());
            usuario.setUsuarioModifica(us.getIdUsuario());
        }
        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        return "redirect:/empleado";
    }

    /**
     * al seleccionar un empleado para editar, se ingresa a esta funcion la cual
     * prepra la información del empleado
     *
     * @param usuario este parametro contiene el id del empleado para buscarlo
     * en base de datos y enviarlo al formulario para su edición
     * @param model
     * @return se redirecciona hacia el formulario modificaremp.html y se
     * prepara el objeto usuario
     */
    @GetMapping("/editaremp")
    public String editarEmpleado(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        model.addAttribute("dominio", dominio);
        model.addAttribute("stperfil", stPerfil);
        return "modificaremp";
    }

    /**
     * esta función cambia el estado del empleado, para darlo de baja
     *
     * @param usuario este parametro contiene el id del empleado a dar de baja
     * @param model
     * @return una vez dado de baja se redirecciona a la pagina empleado.html
     */
    @GetMapping("/eliminaremp")
    public String eliminarEmpleado(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        Usuario us = varios.getUsuarioLogueado();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(us.getIdUsuario());
        usuarioService.guardar(usuario);
        return "redirect:/empleado";
    }

    
    
    
    
    ////////////////////////////////////////////////////////////////////
    //                      USUARIO DE RESIDENCIAL
    ///////////////////////////////////////////////////////////////////
    /**
     * Esta función prepara los usuarios de una residencial a mostrar, al igual
     * que el anterior muesta todos los usuarios activos, pero este es el
     * específico para el administrador de la residencial, ya que muestra
     * unicamente a los usuarios de la recidencial a la que pertenece el usuario
     * logueado.
     *
     * @param model
     * @return el listado de usuarios se redirecciona a la pagina
     * usuariores.html
     */
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

    /**
     * Esta función guarda a los usuarios creados y los modificados.
     *
     * @param usuario este parametro tiene la información del usuario a guardar
     * o editar
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @param errors
     * @return despues de guardado el usuario se procede con la redireccion
     * hacia la pagina usuariores.html
     */
    @PostMapping("/guardarusres")
    public String guardarusres(@Valid Usuario usuario, BindingResult bindingResult,@RequestParam("file") MultipartFile imagen, Model model, Errors errors) {
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        String pagina = "/usuariores";
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
            pagina = "/asignarol?idUsuario=";
        } else {

            usuario.setFechaModifica(Tools.now());
            log.info("Modifica Usuario " + usuario + " fecha " + Tools.now());

            usuario.setUsuarioModifica(usuarioLogueado.getIdUsuario());
        }
        
        usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        log.info("Se actualiza usuario " + usuario);
        Long idUs = usuarioService.guardar(usuario);
        log.info("el valor de pagina es:" + pagina);
        if (!pagina.equals("/usuariores")) {
            pagina = pagina + idUs;
        }

        return "redirect:" + pagina;
    }

    /**
     * Esta función permite buscar un usuario por su id y cargarlo a la variable
     * que corresponde para su modificación
     *
     * @param usuario este parametro contiene el id del usuario a modificar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return una vez cargada la informacion del usuario se redirecciona a la
     * pagina modificarusres.html que contiene el formulario donde se carga la
     * data del usuario para su edición
     */
    @GetMapping("/editarusres")
    public String editarusres(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        model.addAttribute("dominio", dominio);
        model.addAttribute("stperfil", stPerfil);
        return "modificarusres";
    }

    /**
     * Esta función permite buscar un usuario específico y cambiar su estado
     * para su eliminación
     *
     * @param usuario esta variable es la que contiene el id del usuario a
     * eliminar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return una vez eliminado el usuario se retorna a la pagina principal de
     * usuarios.
     */
    @GetMapping("/eliminarusres")
    public String eliminarusres(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        Usuario us = varios.getUsuarioLogueado();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(us.getIdUsuario());
        usuarioService.guardar(usuario);
        return "redirect:/usuariores";
    }

    ///////////
    /**
     *
     * Esta función redirecciona a la pagina que contiene el formulario para
     * crear empleados
     */
    @GetMapping("/agregarempres")
    public String agregarEmpleadoRes(Usuario Usuario, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        return "crearempres";
    }

    /**
     * Esta función se utiliza para guardar nuevos empleados o editados
     *
     * @param usuario este parametro contiene la información del usuario a
     * editar o crear
     * @param bindingResult es una clase en Spring Framework que se utiliza en
     * conjunto con la validación de datos en formularios web. Es una parte
     * fundamental del proceso de validación de formularios en Spring MVC
     * @param imagen permite cargar imagen desde la vista
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @param errors
     * @return una vez guardado se redirecciona a la pagina empleadores.html
     */
    @PostMapping("/guardarempres")
    public String guardarEmpleadoRes(@Valid Usuario usuario, BindingResult bindingResult, @RequestParam("file") MultipartFile imagen, Model model, Errors errors) {
        Usuario us = varios.getUsuarioLogueado();
        if (usuarioService.encontrarUsuario(usuario.getNombreUsuario()) != null && usuario.getIdUsuario() == null) {
            // Agrega un error personalizado al objeto BindingResult
            log.info("Existe usuario");
            bindingResult.rejectValue("nombreUsuario", "error.nombreUsuario", "El nombre de usuairo ya existe!");
        }
        if (errors.hasErrors()) {

            return "crearempres";
        }
        /*if (!imagen.isEmpty()) {
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

        }*/
        usuario.setFoto(Tools.saveArchivo(stPerfil,imagen,stRuta+"/images/perfil"));
        usuario.setEsEmpleado(1L);
        usuario.setEstado(1L);

        if (usuario.getIdUsuario() == null) {
            usuario.setFechaCrea(Tools.now());
            usuario.setUsuarioCrea(us.getIdUsuario());
            usuario.setResidencial(us.getResidencial());

        } else {
            usuario.setFechaModifica(Tools.now());
            usuario.setUsuarioModifica(us.getIdUsuario());
        }
        log.info("Se actualiza usuario " + usuario);
        usuarioService.guardar(usuario);
        return "redirect:/empleadores";
    }

    @GetMapping("/empleadores")
    public String InicioEmpleadoRes(Model model) {
        Usuario us = varios.getUsuarioLogueado();
        var usuarios = usuarioService.listarEmpleadosResidencial(estadoActivo, us.getResidencial().getIdResidential());
        //log.info("ejecutando controlador empleado spring mvc " + usuarios);

        model.addAttribute("usuarios", usuarios);
        return "empleadores";
    }

    /**
     * Cuando se procede a editar un empleado, esta funcion permite ubicarlo y
     * cargar la información desde la base de datos
     *
     * @param usuario este parametro contiene el id del empleado a editar
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return una vez ubicado el empleado y cargado a la variable indicada se
     * redirecciona hacia la pagina modificarempres.html
     */
    @GetMapping("/editarempres")
    public String editarEmpleadoRes(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        var residenciales = residencialService.listarRecidencialesActivas();
        log.info("Res desde user " + residenciales);
        model.addAttribute("residenciales", residenciales);
        model.addAttribute("dominio", dominio);
        model.addAttribute("stperfil", stPerfil);
        return "modificarempres";
    }

    /**
     * Con esta función se procede a cambiar el estado del empleado, para
     * registra la eliminacion del mismo
     *
     * @param usuario este parametro contiene el id del empleado a eliminar
     *
     */
    @GetMapping("/eliminarempres")
    public String eliminarEmpleadoRes(Usuario usuario, Model model) {
        usuario = usuarioService.encontrarUsuario(usuario);
        Usuario us = varios.getUsuarioLogueado();
        usuario.setEstado(0L);
        usuario.setFechaModifica(Tools.now());
        usuario.setUsuarioModifica(us.getIdUsuario());
        usuarioService.guardar(usuario);
        return "redirect:/empleadores";
    }

    //////////////////////////////////////////////////////////////////////////////////////
    /**
     * Por medio de esta función se realiza la invitacion a los usuarios
     *
     * @param usuario contiene la informacion del usuario al que se le enviará
     * la invitacion para que finalice su registro en la aplicacion
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @param redirectAttributes es una interfaz en Spring Framework que se
     * utiliza para pasar datos entre controladores y redirecciones en
     * aplicaciones web basadas en Spring MVC.
     * @return
     */
    @GetMapping("/invitacion")
    public String enviaInvitacion(Usuario usuario, Model model, RedirectAttributes redirectAttributes) {
        usuario = usuarioService.encontrarUsuario(usuario);
        var rolUs = rolUsuarioService.encontrarRoles(usuario.getIdUsuario());
        System.out.println("rolUs = " + rolUs);
        if (!rolUs.isEmpty()) {
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
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "El usuario no tiene rol Asignado, asignele uno, antes de enviar una invitación");
            return "redirect:/asignarol?idUsuario=" + usuario.getIdUsuario();
        }

    }

    /**
     * Si un usuario olvida su contraseña, esta funcion es la que se ejecuta
     * para enviar un correo al correo del usuario para que este proceda con la
     * recuperación de la misma
     *
     * @param nombreUsuario este parametro contiene el nombre del usuario el
     * cual permite ubicar al usuario al que pertenece el username
     * @param redirectAttributes es una interfaz en Spring Framework que se
     * utiliza para pasar datos entre controladores y redirecciones en
     * aplicaciones web basadas en Spring MVC.
     * @return
     */
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
