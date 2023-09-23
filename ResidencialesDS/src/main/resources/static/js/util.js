function confirmarEliminacion(idUsuario,url) {
        const confirmacion = confirm('¿Seguro que quiere eliminar este item?');
        if (confirmacion) {
            // Si el usuario confirma, redirige a la URL de eliminación
            window.location.href = url + idUsuario;
        }
    }

function obtenerFechaYHora() {
    // Obtener los campos de fecha y hora seleccionados
    const fechaInputDesde = document.getElementById("desde");
    const fechaInputHasta = document.getElementById("hasta");

    // Convertir los valores de los campos en objetos Date

    const fechaDesde = new Date(fechaInputDesde.value);
    const fechaHasta = new Date(fechaInputHasta.value);
    if (fechaInputDesde.value != "" && fechaInputHasta.value != "") {
        // Formatear las fechas en dd/MM/yyyy
        const optionsFecha = {year: 'numeric', month: '2-digit', day: '2-digit'};
        const fechaDesdeFormateada = fechaDesde.toLocaleDateString('es-ES', optionsFecha);
        const fechaHastaFormateada = fechaHasta.toLocaleDateString('es-ES', optionsFecha);

        // Formatear las horas en HH:mm
        const optionsHora = {hour: '2-digit', minute: '2-digit', hour12: false};
        const horaDesdeFormateada = fechaDesde.toLocaleTimeString('es-ES', optionsHora);
        const horaHastaFormateada = fechaHasta.toLocaleTimeString('es-ES', optionsHora);

        // Establecer las partes de fecha y hora en los campos de texto
        document.getElementById("desdeFecha").value = fechaDesdeFormateada;
        document.getElementById("desdeHora").value = horaDesdeFormateada;
        document.getElementById("hastaFecha").value = fechaHastaFormateada;
        document.getElementById("hastaHora").value = horaHastaFormateada;
    }
}

function obtenerFechaYHoraServ() {
    // Obtener los campos de fecha y hora seleccionados
    const fechaInputDesde = document.getElementById("desde");

    // Convertir los valores de los campos en objetos Date

    const fechaDesde = new Date(fechaInputDesde.value);
    if (fechaInputDesde.value != "") {
        // Formatear las fechas en dd/MM/yyyy
        const optionsFecha = {year: 'numeric', month: '2-digit', day: '2-digit'};
        const fechaDesdeFormateada = fechaDesde.toLocaleDateString('es-ES', optionsFecha);

        // Formatear las horas en HH:mm
        const optionsHora = {hour: '2-digit', minute: '2-digit', hour12: false};
        const horaDesdeFormateada = fechaDesde.toLocaleTimeString('es-ES', optionsHora);

        // Establecer las partes de fecha y hora en los campos de texto
        document.getElementById("desdeFecha").value = fechaDesdeFormateada;
        document.getElementById("desdeHora").value = horaDesdeFormateada;
    }
}

/*window.addEventListener('load', function () {
    obtenerFechaYHoraServ();
});

window.addEventListener('load', function () {
    obtenerFechaYHora();
});*/

$(document).ready(function () {
    // Configura el Datepicker
    $("#desdeFecha").datepicker({
        dateFormat: "dd/mm/yy",
        language: "es",
        monthNames: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    });
    // Configura el Timepicker
    $("#desdeHora").timepicker({
        timeFormat: 'H:i', // Formato de hora (puedes personalizarlo)
        interval: 30, // Intervalo de tiempo en minutos (opcional)
        dropdown: true, // Muestra un menú desplegable para seleccionar la hora (opcional)
        minTime: '07:00', // Hora mínima (7:00 AM)
        maxTime: '19:00'  // Hora máxima (7:00 PM)
    });
    // Abre el calendario emergente al hacer clic en el botón
   

    $("#hastaFecha").datepicker({
        dateFormat: "dd/mm/yy",
        language: "es",
        monthNames: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    });
    // Configura el Timepicker
    $("#hastaHora").timepicker({
        timeFormat: 'H:i', // Formato de hora (puedes personalizarlo)
        interval: 30, // Intervalo de tiempo en minutos (opcional)
        dropdown: true, // Muestra un menú desplegable para seleccionar la hora (opcional)
        minTime: '07:00', // Hora mínima (7:00 AM)
        maxTime: '19:00'  // Hora máxima (7:00 PM)
    });
    // Abre el calendario emergente al hacer clic en el botón
    
});