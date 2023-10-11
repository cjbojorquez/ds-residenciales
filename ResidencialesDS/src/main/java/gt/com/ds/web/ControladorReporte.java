package gt.com.ds.web;

import gt.com.ds.domain.JReportService;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.SolicitudServicioService;
import gt.com.ds.servicio.TicketService;
import gt.com.ds.servicio.Varios;
import gt.com.ds.util.Tools;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * La reporteria se maneja desde esta clase
 *
 * @author cjbojorquez
 * 
 */
@Controller
@Slf4j
public class ControladorReporte {

    @Autowired
    private JReportService service;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SolicitudServicioService solicitudService;

    @Autowired
    private Varios varios;

    @GetMapping("/reportetickets")
    public void reporteTickets(HttpServletResponse response) throws IOException, JRException, SQLException {
        /*System.out.println("ingresa a repore");
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        System.out.println("response = " + response);
        service.exportJReportTicket(response);*/
    }

    /**
     * Este controler permite visualizar l a cantidad de tickets abiertos
     * @param model por medio de esta interfaz se envian las variables a la vista
     * @return luego de obtener la informacion necesaria, esta se envia a la pagina reporte1.html
     */
    @GetMapping("/reporte1")
    public String reporte1(Model model) {
        Usuario us = varios.getUsuarioLogueado();
        var tickets = ticketService.conteoActivos(us.getResidencial().getIdResidential());

        Object[] obj = new Object[2];
        obj[0] = "Solicitud";
        obj[1] = solicitudService.conteoActivos(us.getResidencial().getIdResidential());
        tickets.add(obj);
        
        int total = 0;
        for(Object[] tk:tickets){
            total=total + Integer.parseInt(tk[1].toString());
        }
        List<Object[]> datos = new ArrayList<Object[]>();
        List<Object[]> datosAnt = new ArrayList<Object[]>();
        
        var solicitudMesActual = solicitudService.conteoPorEstado(0,us.getResidencial().getIdResidential());
        var solicitudMesAnterior = solicitudService.conteoPorEstado(1,us.getResidencial().getIdResidential());
        
        var ticketMesActual = ticketService.conteoPorEstado(0,us.getResidencial().getIdResidential());
        var ticketMesAnterior = ticketService.conteoPorEstado(1,us.getResidencial().getIdResidential());
        
        
        
        ticketMesActual.addAll(solicitudMesActual);
        ticketMesAnterior.addAll(solicitudMesAnterior);
        
        int abiertos=0,cerrados=0;
        for(Object[] tk:ticketMesActual){
            if(tk[0].equals("Abierto")){
                abiertos=abiertos+Integer.parseInt(tk[1].toString());
            }
            if(tk[0].equals("Cerrado")){
                cerrados=cerrados+Integer.parseInt(tk[1].toString());
            }
            System.out.println("tk = " + tk);
        }
        
        int abiertosAnt=0,cerradosAnt=0;
        for(Object[] tk:ticketMesAnterior){
            if(tk[0].equals("Abierto")){
                abiertosAnt=abiertosAnt+Integer.parseInt(tk[1].toString());
            }
            if(tk[0].equals("Cerrado")){
                cerradosAnt=cerradosAnt+Integer.parseInt(tk[1].toString());
            }
        }
        
        Object[] tmpOpen = new Object[2];
        Object[] tmpClose = new Object[2];
        
        tmpOpen[0]="Abierto";
        tmpOpen[1]=abiertos;
        tmpClose[0]="Cerrado";
        tmpClose[1]=cerrados;
        datos.add(tmpOpen);
        datos.add(tmpClose);
        
        Object[] tmpOpenA = new Object[2];
        Object[] tmpCloseA = new Object[2];
        
        tmpOpenA[0]="Abierto";
        tmpOpenA[1]=abiertosAnt;
        tmpCloseA[0]="Cerrado";
        tmpCloseA[1]=cerradosAnt;
        
        datosAnt.add(tmpOpenA);
        datosAnt.add(tmpCloseA);
        
        String strFecha = Tools.formateaFecha(Tools.now());
        model.addAttribute("residencial",us.getResidencial());
        model.addAttribute("strFecha", strFecha);
        model.addAttribute("total", total);
        model.addAttribute("tickets", tickets);
        model.addAttribute("datos", datos);
        model.addAttribute("ticketMesActual", ticketMesActual);
        model.addAttribute("ticketMesAnterior", datosAnt);
        model.addAttribute("totalMes",abiertos+cerrados);
        model.addAttribute("totalAnterior",abiertosAnt+cerradosAnt);
        
        return "reporte1";
    }

}
