package gt.com.ds.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorInicio {
    
    @Value("${index.saludo}")
    private String saludo;
    
    @GetMapping("/")
    public String Inicio(Model model){
        var mensaje = "Hola mundo con Thymeleaf"; 
        log.info("ejecutando controlador spring mvc");
        model.addAttribute("mensaje",mensaje);
        model.addAttribute("saludo",saludo);
        return "index";
    }
       
}
