package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.EstadoTicketService;
import gt.com.ds.servicio.NotificacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;
import java.text.SimpleDateFormat;

import java.util.Date;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

/**
 *
 * @author cjbojorquez
 */
@Controller
@Slf4j
public class ControladorBuzon {

    @Autowired
    private EstadoTicketService estadoTicketService;

    @Autowired
    private BuzonService buzonService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Varios varios;

    private String varNotiGeneral = "G";
    private String varNotiEspecifica = "E";

    @GetMapping("/buzon")
    public String InicioGeneral(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        var notificaciones = buzonService.buzonPorUsuario(usuarioLogueado.getIdUsuario());//agregar residencial
        log.info("estas son las notificaciones " + notificaciones.toString() + " IdUsuario=" + usuarioLogueado.getIdUsuario());
        model.addAttribute("notificaciones", notificaciones);

        return "verbuzon";
    }

    @GetMapping("/leerbuzon")
    public String agregarGeneral(Notificacion notificacion, Model model) {

        return "leerbuzon";
    }

}
