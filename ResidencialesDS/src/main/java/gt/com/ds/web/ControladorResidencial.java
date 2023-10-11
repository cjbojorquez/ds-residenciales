package gt.com.ds.web;

import gt.com.ds.domain.Contacto;
import gt.com.ds.domain.Residencial;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.ContactoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase permite el manejo de las recidenciales, creación, modificación y eliminación
 *
 * @author cjbojorquez
 * 
 */
@Controller
@Slf4j
public class ControladorResidencial {

    /*@Value("${index.saludo}")
    private String saludo;*/
    
    @Autowired
    private ResidencialService residencialService;

    @Autowired
    private Varios varios;
    
    @Autowired
    private ContactoService contactoService;
    
    //Tools tool = new Tools();
    
    /**
     * Esta función permite listar todas las residenciales creadas y en estado activo 
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las residenciales estas se envian a la pagina residencial.html
     */
    @GetMapping("/residencial")
    public String Inicio(Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        var mensaje = "Hola mundo con Thymeleaf para el home";
        log.info("ejecutando controlador spring mvc " + residenciales);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("residenciales", residenciales);
        //var residencial = new Residencial();
//        residencial.setNombre("residencial1");
//        residencial.setDireccion("Guatemala");
        //model.addAttribute("residencial",residencial);
        return "residencial";
    }

    @GetMapping("/agregarres")
    public String agregar(Residencial residencial) {
        return "modificarres";
    }

    /**
     * Esta funcion es utilizada para guardar una nueva residencial o una residencial editada
     * @param residencial este es el objeto que se persistira en la base de datos
     * @param imagen la imagen es guardada en el servidor y se guarda el path en el objeto
     * @return 
     */
    @PostMapping("/guardarres")
    public String guardar(@Valid Residencial residencial, @RequestParam("file") MultipartFile imagen,Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();
        Usuario us = varios.getUsuarioLogueado();
        if(errors.hasErrors()){
            return "modificarres";
        }
        if(!imagen.isEmpty()){
            Path directorioImagenes = Paths.get("src//main//resources//static//images//logos");
            
            String rutaAbsoluta=directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta "+rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = Tools.newName(imagen.getOriginalFilename()); 
                Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreArchivo);
                residencial.setLogo("images/logos/" + nombreArchivo);
                log.info("Se intenta guardar imagen "+rutaCompleta.toString());
                Files.write(rutaCompleta,byteImg);
            } catch (IOException ex) {
                Logger.getLogger(ControladorResidencial.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        residencial.setStatus(1L);
        if (residencial.getIdResidential() == null) {
            residencial.setCreate_time(Tools.now());
            residencial.setCreate_user(us.getIdUsuario());
        } else {
            residencial.setModify_time(Tools.now());
            residencial.setModify_user(us.getIdUsuario());
        }
        log.info("Se actualiza Residencial " + residencial);
        residencialService.guardar(residencial);
        return "redirect:/residencial";
    }

    /**
     * Cuando se desea editar una residencial se hace por medio de esta funcion, 
     * @param residencial el id de la residencial va almacenada en este objeto por medio del cual
     * se procede a hacer la busqueda y se envia al formulario de modificación
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente, por medio de este se envia al formulario 
     * el objeto
     * @return una vez ubicado el objeto, se envia por medio del modelo a la pagina modificarres.html
     */
    @GetMapping("/editarres")
    public String editar(Residencial residencial, Model model) {
        residencial = residencialService.encontrarResidencial(residencial);
        model.addAttribute("residencial", residencial);
        return "modificarres";
    }

    
    @GetMapping("/getresidenciales")
    @ResponseBody
    public List<Residencial> getResidenciales(Residencial residencial, Model model) {
        var residenciales = residencialService.listarRecidencialesActivas();
        List <Residencial> rs= new ArrayList<>();
        
        for(Residencial res:residenciales){
            Residencial newResidencial=new Residencial();
            newResidencial.setIdResidential(res.getIdResidential());
            newResidencial.setName(res.getName());
            rs.add(newResidencial);
        }
        return rs;
    }
    
    /** 
     * Cuando se desea dar de baja a una residencial, se realiza por medio de esta función
     * @param residencial este objeto es el que contiene el id de la residencial que se desea
     * eliminar
     * @return se redirecciona a la pagina residencial.html
     */
    @GetMapping("/eliminarres")
    public String eliminarContacto(Residencial residencial, Model model) {
        Usuario us = varios.getUsuarioLogueado();
        residencial.setStatus(0L);
        residencial.setModify_time(Tools.now());
        residencial.setModify_user(us.getIdUsuario());
        residencialService.guardar(residencial);
        return "redirect:/residencial";
    }
    
    /**
     * Esta función permite persistir un nuevo contacto o la edicion de uno nuevo
     * @param contacto este objeto contiene la informacion del contacto a guardar
     * @return una vez guardado el contacto, se procede a redireccionar a la pagina
     * donde se visualizan todos los contactos
     */
    @PostMapping("/guardarcontacto")
    public String guardarContacto(@Valid Contacto contacto, Model model, Errors errors) {
        
        Usuario us = varios.getUsuarioLogueado();
        if(errors.hasErrors()){
            return "modificarcontacto";
        }
        
        if (contacto.getIdContacto() == null) {
            contacto.setIdEstado(1L);
            contacto.setFechaCrea(Tools.now());
            contacto.setUsuarioCrea(us.getIdUsuario());
            contacto.setResidencial(us.getResidencial());
        } else {
            contacto.setFechaModifica(Tools.now());
            contacto.setUsuarioModifica(us.getIdUsuario());
        }
        
        model.addAttribute("residencial", us.getResidencial());
        model.addAttribute("contactos",contactoService.buscaPorResidencial(us.getResidencial().getIdResidential()));
        contactoService.guardar(contacto);
        return "redirect:/contacto";
    }
    
    /**
     * En este controlador se muestran todos los contactos creados y activos
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente, por medio de este parametro se 
     * cargan los contactos
     * @return luego de cargar los contactos creados, estos se envian a la pagina contacto.html
     */
    @GetMapping("/contacto")
    public String contacto(Model model) {
        
        Usuario us = varios.getUsuarioLogueado();
        var contactos = contactoService.buscaPorResidencial(us.getResidencial().getIdResidential());
        List<List<Contacto>> contactGroups = new ArrayList<>();
        if(contactos.size()>0){
        for (int i = 0; i < contactos.size(); i += 3) {
            List<Contacto> group = contactos.subList(i, Math.min(i + 3, contactos.size()));
            contactGroups.add(group);
        }
         model.addAttribute("contactGroups", contactGroups);
        }
        model.addAttribute("residencial", us.getResidencial());
       
        
        return "contacto";
    }
    
    @GetMapping("/agregarcontacto")
    public String agregarContacto(Contacto contacto) {
        return "modificarcontacto";
    }
    
    /**
     * Esta función permite la edición de un contacto
     * @param ct esta variable tiene el id del contacto a editarse
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return una vez que se busca el contacto, este se envia al formulario 
     * modificarcontacto.html
     */
    @GetMapping("/editarcontacto")
    public String editarContacto(Contacto ct, Model model) {
        Contacto contacto = contactoService.encontrarContacto(ct.getIdContacto());
        model.addAttribute("contacto", contacto);
        return "modificarcontacto";
    }
    
    /**
     * Si se desea eliminar un contacto, esta es la funcion que se utliza para modificar
     * el estado del mismo
     * 
     * @param ct este objeto es el que contiene el id del objeto a eliminar
     * 
     * @return luego de modificar el objeto en base de datos, se redirecciona a la
     * pagina donde se muestran los contactos
     */
    @GetMapping("/eliminarcontacto")
    public String eliminarContacto(Contacto ct, Model model) {
        Usuario us = varios.getUsuarioLogueado();
        Contacto contacto = contactoService.encontrarContacto(ct.getIdContacto());
        contacto.setIdEstado(0L);
        contacto.setFechaModifica(Tools.now());
        contacto.setUsuarioModifica(us.getIdUsuario());
        contactoService.guardar(contacto);
        return "redirect:/contacto";
    }

}
