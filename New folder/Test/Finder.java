import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Finder {

    public static void main(String[] args) throws IOException {
        // Crea una lista para almacenar los sitios web encontrados
        List<String> websites = new ArrayList<>();

        // Usa la API de Google Search para buscar sitios web que contengan la palabra "pet shop"
        URL searchUrl = new URL("https://derecho.cloud/?="+args[0]);
        HttpURLConnection connection = (HttpURLConnection) searchUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Obtén el contenido de la respuesta de búsqueda
        String response = connection.getInputStream().toString();

        // Usa una expresión regular para encontrar los enlaces a los sitios web en la respuesta de búsqueda
        String regex = "<a href=\"(.*?)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            // Agrega el sitio web encontrado a la lista
            websites.add(matcher.group(1));
        }

        // Imprime la lista de sitios web encontrados
        for (String website : websites) {
            System.out.println(website);
        }
    }
}