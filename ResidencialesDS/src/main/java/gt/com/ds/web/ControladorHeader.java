/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.com.ds.web;

import gt.com.ds.domain.Buzon;
import gt.com.ds.domain.Comentario;
import gt.com.ds.domain.Mensaje;
import gt.com.ds.domain.Ticket;
import gt.com.ds.domain.Usuario;
import gt.com.ds.servicio.BuzonService;
import gt.com.ds.servicio.ComentarioService;
import gt.com.ds.servicio.ResidencialService;
import gt.com.ds.servicio.TicketService;
import gt.com.ds.servicio.UsuarioService;
import gt.com.ds.servicio.Varios;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * En esta clase se administra la informacion del header
 *
 * @author cjbojorquez
 *
 */
@Controller
@Slf4j
public class ControladorHeader {

    @Autowired
    private BuzonService buzonService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ResidencialService residencialService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private Varios varios;

    @Autowired
    private UsuarioService usuarioService;

    private Long noLeidos = 1L;

    /**
     * Este controlador permite mostrar todos los mensajes nuevos que recibe el
     * usuario en el header
     *
     * @param model
     * @return
     */
    @GetMapping("/obtenerMensajes")
    @ResponseBody
    public List<Mensaje> obtenerMensajes(Model model) {
        Usuario us = varios.getUsuarioLogueado();
        String rol = varios.getRolLogueado();
        var buzon = buzonService.buzonPorEstado(noLeidos, us.getIdUsuario());
        List<Mensaje> mensajes = new ArrayList<>();
        for (Buzon b : buzon) {
            Mensaje mensaje = new Mensaje();
            mensaje.setAsunto(b.getAsunto());
            mensaje.setUrl("/buzon");
            mensajes.add(mensaje);
        }
        System.out.println("mensajes1 = " + mensajes);
        List<Comentario> comentarios = new ArrayList<>();
        if (rol.equals("ROLE_USER")) {
            comentarios = comentarioService.buscaNoLeidos(us.getIdUsuario());
        } else {
            comentarios = comentarioService.buscaNoLeidosR(us.getResidencial().getIdResidential());
            List<Ticket> tickets = ticketService.ticketPorEstado(1L, us.getResidencial().getIdResidential());
            if (tickets != null) {
                for (Ticket t : tickets) {
                    System.out.println("t = " + t);
                    Mensaje mensaje = new Mensaje();
                    mensaje.setAsunto(t.getAsunto());
                    mensaje.setUrl(t.getIdTipo() == 1 ? "/editargestion?idTicket=" + t.getIdTicket() : "/editaranomalia?idTicket=" + t.getIdTicket());

                    mensajes.add(mensaje);
                }
            }
        }
        for (Comentario c : comentarios) {
            Mensaje mensaje = new Mensaje();
            mensaje.setAsunto(c.getComentario().length() > 10 ? c.getComentario().substring(0, 9) + "..." : c.getComentario());
            String url=c.getTicket().getIdTipo() == 1 ? "/editargestion?idTicket=" + c.getTicket().getIdTicket() : "/editaranomalia?idTicket=" + c.getTicket().getIdTicket();
            url=url + "&idcomentario="+c.getIdComentario();
            mensaje.setUrl(url);
            if (!Objects.equals(us.getIdUsuario(), c.getUsuario().getIdUsuario())) {
                mensajes.add(mensaje);

            }
        }
        var residencial = us.getResidencial();
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("residencial", residencial);
        return mensajes;
    }
    
    @GetMapping("/mostrar-imagen/{nombreImagen}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable String nombreImagen) throws IOException {
        // Construye la ruta completa de la imagen
        Path rutaImagen = Paths.get("/home/ubuntu/files/images", nombreImagen);
        
        // Verifica si la imagen existe
        if (Files.exists(rutaImagen)) {
            // Lee la imagen desde su ubicación y la devuelve en la respuesta HTTP
            byte[] imagenBytes = Files.readAllBytes(rutaImagen);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            // Envia una respuesta 404 si la imagen no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/mostrar-perfil/{nombreImagen}")
    public ResponseEntity<byte[]> mostrarPerfil(@PathVariable String nombreImagen) throws IOException {
        // Construye la ruta completa de la imagen
        Path rutaImagen = Paths.get("/home/ubuntu/files/images/perfil", nombreImagen);
        
        // Verifica si la imagen existe
        if (Files.exists(rutaImagen)) {
            // Lee la imagen desde su ubicación y la devuelve en la respuesta HTTP
            byte[] imagenBytes = Files.readAllBytes(rutaImagen);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            // Envia una respuesta 404 si la imagen no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/mostrar-logo/{nombreImagen}")
    public ResponseEntity<byte[]> mostrarLogo(@PathVariable String nombreImagen) throws IOException {
        // Construye la ruta completa de la imagen
        Path rutaImagen = Paths.get("/home/ubuntu/files/images/logos", nombreImagen);
        
        // Verifica si la imagen existe
        if (Files.exists(rutaImagen)) {
            // Lee la imagen desde su ubicación y la devuelve en la respuesta HTTP
            byte[] imagenBytes = Files.readAllBytes(rutaImagen);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            // Envia una respuesta 404 si la imagen no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/mostrar-adjunto/{nombreArchivo}")
    public ResponseEntity<Resource> mostrarAdjunto(@PathVariable String nombreArchivo) throws IOException {
        // Construye la ruta completa de la imagen
        Path filePath  = Paths.get("/home/ubuntu/files/adjunto", nombreArchivo);
        
        // Verifica si la imagen existe
        if (Files.exists(filePath)) {
            // Lee la imagen desde su ubicación y la devuelve en la respuesta HTTP
            /*byte[] imagenBytes = Files.readAllBytes(rutaImagen);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
            */
            Resource resource = new org.springframework.core.io.PathResource(filePath);

        // Configurar las cabeceras de la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // Devolver la respuesta con el archivo y las cabeceras configuradas
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    
        } else {
            // Envia una respuesta 404 si la imagen no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
