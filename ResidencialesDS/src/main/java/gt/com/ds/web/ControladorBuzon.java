package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.Notificacion;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.EstadoTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;

/**
 * Esta clase permite el manejo del buzon de los usuarios, en este llegan las
 * notificaciones para que el usuario pueda visualizar las notificaciones
 *
 * @author cjbojorquez
 *
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
    private Long noLeidos = 1L;

    /**
     * Esta función permite listar todos los servicios creados y en estado
     * activo
     *
     * @param model es una interfaz en Spring Framework que se utiliza en el
     * contexto de aplicaciones web basadas en Spring MVC
     * (Model-View-Controller) para pasar datos desde el controlador a la vista
     * (la plantilla HTML) de una manera organizada y eficiente
     * @return al cargar todas las solicitudes estas se envian a la pagina
     * servicio.html
     */
    @GetMapping("/buzon")
    public String InicioGeneral(Model model) {
        // Tipo de ticket 1 = Gestion ; 2 = Anomalias
        //  G = Notificacion General
        Usuario usuarioLogueado = varios.getUsuarioLogueado();
        var notificaciones = buzonService.buzonPorUsuario(usuarioLogueado.getIdUsuario());
        var notificacionesNoLeidas = buzonService.buzonPorEstado(noLeidos, usuarioLogueado.getIdUsuario());
        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("notificacionesNoLeidas", notificacionesNoLeidas);
        for (Buzon noti : notificacionesNoLeidas) {
            noti.setEstado(2L);
            buzonService.guardar(noti);
        }
        return "verbuzon";
    }

    @GetMapping("/leerbuzon")
    public String leerBuzon(Notificacion notificacion, Model model) {
        //var buzon = buzonService.
        return "leerbuzon";
    }

}
