package gt.com.ds.servicio;

import gt.com.ds.dao.TicketDao;
import gt.com.ds.domain.Conteo;
import gt.com.ds.domain.Ticket;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class TicketServiceImpl implements TicketService{

    @Autowired
    private TicketDao ticketDao;
    
    /**
     * Esta función lista todos los tickets filtrados por tipo de ticket y por id de residencial
     * @param idTipo
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ticket> ticketsPorTipo(Long idTipo, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorTipo(idTipo,idResidencial);
    }

    /**
     * Esta función lista los tickets filtrados por estado y por id de residencial
     * @param idEstado
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ticket> ticketPorEstado(Long idEstado, Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorEstado(idEstado,idResidencial);
    }

    /**
     * Esta función lista los tickets por tipo y por usuario
     * @param idTipo
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ticket> ticketPorUsuario(Long idTipo, Long idUsuario) {
        return (List<Ticket>)ticketDao.buscarPorUsuario(idTipo,idUsuario);
    }

    /**
     * Esta función lista los tickets abiertos, filtrados por tipo y por residencial
     * @param tipoTicket
     * @param idResidencial
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ticket> listarTicketsAbiertos(Long tipoTicket,Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarPorTipo(tipoTicket,idResidencial);
    }

    /**
     * Esta funcion permite guardar en base de datos el objeto Ticket
     * @param ticket 
     */
    @Override
    @Transactional
    public void guardar(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Override
    @Transactional
    public void eliminar(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket encontrarTicket(Ticket ticket) {
        return ticketDao.findById(ticket.getIdTicket()).orElse(null);
    }

    /**
     * Esta función permite encontrar un ticket buscandolo por su id
     * @param idTicket
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Ticket encontrarTicket(Long idTicket) {
        return ticketDao.findById(idTicket).orElse(null);
    }

    /**
     * Esta función permite listar todos los tickets filtrados por residencial
     * @param idResidencial
     * @return 
     */
    @Override
    public List<Ticket> porResidencial(Long idResidencial) {
        return (List<Ticket>)ticketDao.buscarActivos(idResidencial);
    }

    /**
     * Esta funcion hace un conteo de tickets activos filtrados por residencial
     * @param idResidencial
     * @return 
     */
    @Override
    public List<Object[]> conteoActivos(Long idResidencial) {
        return (List<Object[]>)ticketDao.countActivos(idResidencial);
    }

    /**
     * Esta funcion hace un conteo de los tickets que estan abiertos y cerrados
     * @param mes este parametro nos indica si se quiere la informcaion del mes actual o
     * la informacion del mes anterior
     * @param idResidencial este es el parametro que nos indica sobre que residencial se realizará la consulta
     * @return retorna un array de objetos con la cantidad de tickes agrupada por abiertos y cerrados
     */
    @Override
    public List<Object[]> conteoPorEstado(int mes, Long idResidencial) {
        if(mes==0){
            return (List<Object[]>)ticketDao.conteoPorEstadoMesActual(idResidencial);
        }else {
            return (List<Object[]>)ticketDao.conteoPorEstadoMesAnterior(idResidencial);
        }
    }
    
}
