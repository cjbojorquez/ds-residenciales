package gt.com.ds.servicio;

import gt.com.ds.dao.BuzonDao;
import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.EstadoTicket;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.util.Tools;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Esta clase es la utlizada para acceder a la informacion de los buzones 
 * 
 * @author cjbojorquez
 * 
 */
@Service
public class BuzonServiceImpl implements BuzonService{

    @Autowired
    private BuzonDao buzonDao;
    
   /**
    * Por medio de esta función se obtienen los objetos filtrados por estado
    * y por recidencial
    * @param idEstado
    * @param idResidencial
    * @return 
    */
    @Override
    @Transactional(readOnly = true)
    public List<Buzon> buzonPorEstado(Long idEstado, Long idResidencial) {
        return (List<Buzon>)buzonDao.buscarPorEstado(idEstado,idResidencial);
    }

    /**
     * Esta función permite listar los objetos filtrados por usuario
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Buzon> buzonPorUsuario(Long idUsuario) {
        return (List<Buzon>)buzonDao.buscarPorUsuario(idUsuario);
    }

    /**
     * Esta función permite persistir el objeto a base de datos
     * @param buzon 
     */
    @Override
    @Transactional
    public void guardar(Buzon buzon) {
        buzonDao.save(buzon);
    }

    /**
     * Esta función permite eliminar el objeto de base de datos, funcion que no se utiliza en esta aplicacion
     * pero se configura para su uso si fuera necesario
     * @param buzon 
     */
    @Override
    @Transactional
    public void eliminar(Buzon buzon) {
        buzonDao.delete(buzon);
    }

    /**
     * Esta función retorna un objeto en especifico filtrado por el objeto
     * @param buzon
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public Buzon encontrarBuzon(Buzon buzon) {
        return buzonDao.findById(buzon.getIdBuzon()).orElse(null);
    }
    
    /**
     * Esta función retorna un array de mensajes no leidos filtrados por usuario
     * @param idUsuario
     * @return 
     */
    @Override
    @Transactional(readOnly = true)
    public List<Buzon> buzonNoLeidos(Long idUsuario) {
        return (List<Buzon>)buzonDao.buscarNoLeidos(idUsuario);
    }

    /**
     * Esta funcion crea un mensaje notificando el cambio de estado de un ticket
     * @param actual estado actual del ticket
     * @param nuevo nuevo estado del ticket
     * @param destino a quien se envio el mensaje 
     * @param remite quien crea el mensaje
     */
    @Override
    @Transactional
    public void cambioEstadoTicket(EstadoTicket actual, Ticket nuevo,Usuario destino, Usuario remite) {
        Buzon buzon = new Buzon();
        buzon.setAsunto("Cambio de estado Ticket #" + nuevo.getIdTicket());
        buzon.setUsuarioCrea(remite.getIdUsuario());
        buzon.setUsuario(destino);
        buzon.setEstado(1L);
        buzon.setDescripcion("El ticket (" + nuevo.getIdTicket() + ") - " + nuevo.getAsunto() + " : Cambio del estado " + actual.getNombre() 
                + " al estado " + nuevo.getEstado().getNombre() + " ");
        buzon.setFechaCrea(Tools.now());
        buzonDao.save(buzon);
    }
}
