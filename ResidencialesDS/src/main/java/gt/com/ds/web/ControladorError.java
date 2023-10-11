package gt.com.ds.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorError {

    @GetMapping("/errores/403")
    public String accesoDenegado() {
        return "errores/403"; 
    }
}
