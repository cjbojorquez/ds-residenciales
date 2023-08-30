package gt.com.ds.web;

import gt.com.ds.domain.Residencial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gt.com.ds.servicio.ResidencialService;
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
public class ControladorResidencial {

    @Value("${index.saludo}")
    private String saludo;
    @Autowired
    private ResidencialService residencialService;

    Tools tool = new Tools();
    
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

    @PostMapping("/guardarres")
    public String guardar(@Valid Residencial residencial, @RequestParam("file") MultipartFile imagen,Errors errors) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();
        if(errors.hasErrors()){
            return "modificarres";
        }
        if(!imagen.isEmpty()){
            Path directorioImagenes = Paths.get("src//main//resources//static//images//logos");
            
            String rutaAbsoluta=directorioImagenes.toFile().getAbsolutePath();
            log.info("Ruta absoluta "+rutaAbsoluta + " " + directorioImagenes.toString());
            try {
                byte[] byteImg = imagen.getBytes();
                String nombreArchivo = tool.newName(imagen.getOriginalFilename()); 
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
            residencial.setCreate_user(1L);
        } else {
            residencial.setModify_time(Tools.now());
            residencial.setModify_user(1L);
        }
        log.info("Se actualiza Residencial " + residencial);
        residencialService.guardar(residencial);
        return "redirect:/residencial";
    }

    @GetMapping("/editarres")
    public String editar(Residencial residencial, Model model) {
        residencial = residencialService.encontrarResidencial(residencial);
        model.addAttribute("residencial", residencial);
        return "modificarres";
    }

    @GetMapping("/eliminarres")
    public String eliminar(Residencial residencial, Model model) {
        residencial = residencialService.encontrarResidencial(residencial);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();
        residencial.setStatus(0L);
        residencial.setModify_time(Tools.now());
            residencial.setModify_user(1L);
        residencialService.guardar(residencial);
        return "redirect:/residencial";
    }

}
