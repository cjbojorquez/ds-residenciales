function actualizarMensajes() {
    $.get("/obtenerMensajes", function (data) {
        // Limpiar la lista de mensajes existente
        $("#mensajeList").empty();

        // Iterar sobre los nuevos mensajes y agregarlos a la lista
        /*$.each(data, function (index, mensaje) {
            $("#mensajeList").append(
                '<li>' + mensaje.asunto + '</li>'
            );
        });*/
        $.each(data, function (index, mensaje) {
           var miIcono = document.getElementById("alerta");
            miIcono.classList.add("fa-shake");
            var enlace = $('<a>').text(mensaje.asunto).attr('href', mensaje.url).addClass('list-group-item').addClass('list-group-item-action');
            
            var listItem = $('<div>').addClass('list-group').append(enlace);
            
            $("#mensajeList").append(listItem);
        });
    });
}

// Llamar a la función para cargar los mensajes al cargar la página
$(document).ready(function () {
    actualizarMensajes();

    // Actualizar los mensajes cada cierto intervalo de tiempo (por ejemplo, cada 10 segundos)
    setInterval(actualizarMensajes, 360000);
});

