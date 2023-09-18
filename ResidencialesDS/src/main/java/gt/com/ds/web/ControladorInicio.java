package gt.com.ds.web;

import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @Autowired
    private BuzonService buzonService;
    
    @Autowired
    private ResidencialService residencialService;
    
    @Autowired
    private Varios varios;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/")
    public String Inicio(Model model){
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        var mensaje = "Hola mundo con Thymeleaf"; 
        log.info("ejecutando controlador spring mvc");
        var mensajes = buzonService.buzonPorEstado(1L, usuarioLogueado.getIdUsuario());
        var residencial = usuarioLogueado.getResidencial();
        int nuevos=mensajes.size();
        model.addAttribute("mensajes",mensajes);
        model.addAttribute("nuevos",nuevos);
        model.addAttribute("residencial",residencial);
        return "index";
    }
    
       
}
