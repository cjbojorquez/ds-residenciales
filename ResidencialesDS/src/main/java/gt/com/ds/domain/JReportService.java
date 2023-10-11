package gt.com.ds.domain;

import gt.com.ds.servicio.SolicitudServicioService;
import gt.com.ds.servicio.TicketService;
import gt.com.ds.servicio.Varios;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author cjbojorquez
 */
@Service
public class JReportService {
    /*@Autowired
    private TicketService ticketService;
    
    @Autowired
    private SolicitudServicioService solicitudService;
    
    @Autowired
    private Varios varios;*/
    
    public void exportJReportTicket(HttpServletResponse response) throws  JRException,IOException, SQLException {
        /*Usuario us = varios.getUsuarioLogueado();
        List<Ticket> address = ticketService.porResidencial(us.getResidencial().getIdResidential());
        System.out.println("address = " + address);
        List<Object[]> temp = ticketService.conteoActivos(us.getResidencial().getIdResidential());
        System.out.println("temp = " + temp);
         //Map<Long,Object> sumatoriaPorTipo = new HashMap<>();
         Map<String,Long> sumatoriaPorTipo= new HashMap<>();
         int i=0;
        for(Object[] ct:temp){
            
            System.out.println("ct = " + ct[0] + " - "+ ct[1]);
            ct[0]=ct[0].toString().equals("1")?(Object)"Gestion":(Object)"Anomalia";
            System.out.println("ct = " + ct[0] + " - "+ ct[1]);
            sumatoriaPorTipo.put(ct[0].toString(),Long.valueOf(ct[1].toString()));
            System.out.println("ssss " + sumatoriaPorTipo);
            i++;
        }
        
        File file = ResourceUtils.getFile("classpath:reporte_tickets.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(address);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUMATORIA_POR_TIPO", sumatoriaPorTipo);
        
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());*/
    }
    
}
