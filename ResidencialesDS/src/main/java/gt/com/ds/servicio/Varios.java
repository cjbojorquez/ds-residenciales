package gt.com.ds.servicio;

import gt.com.ds.dao.UsuarioDao;
import gt.com.ds.domain.Usuario;
import gt.com.ds.util.EmailService;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cjbojorquez
 */
@Service
public class Varios {

    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private EmailService emailService;
    
public Usuario getUsuarioLogueado() {
        // Obtén el objeto Authentication del contexto de seguridad actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = usuarioDao.buscarUsuario(username);
            System.out.println("Nombre de usuario: " + username);
            return usuario;
        } else {
            // El usuario no está autenticado
            System.out.println("Usuario no autenticado");
            return null;
        }

    } 
public ResponseEntity<?> sendEmail(String to, String asunto, String mensaje, String origen) {
        try {
            emailService.sendSimpleMessage(to, asunto, mensaje, origen);
            //emailService.sendMessage(to, asunto, mensaje, file, origen);
            System.out.println("mensaje = " + mensaje + " to: " + to + " asunto: " + asunto);
            return ResponseEntity.ok("Correo electrónico enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
        }
    }
public ResponseEntity<?> sendEmail(String to, String asunto, String mensaje, File file,String origen) {
        try {
            //emailService.sendSimpleMessage(to, asunto, mensaje, origen);
            emailService.sendMessage(to, asunto, mensaje, file, origen);
            System.out.println("mensaje = " + mensaje + " to: " + to + " asunto: " + asunto);
            return ResponseEntity.ok("Correo electrónico enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
        }
    }
}
