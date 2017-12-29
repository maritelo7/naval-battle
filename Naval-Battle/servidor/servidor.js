  /*
  *Diciembre 2017
  *Servidor para juego Naval-Battle
  *Creado por: 
  * Maribel Tello Rodriguez
  * José Alí Valdivia Ruiz
  */

  var io = require("socket.io")(7000);
  var usuarios = [];
  var numUsuarios = 0;

  //Conexión de usuarios y lógica de la partida
  io.on("connection", function(socket){
  	console.log("Jugador conectado");
  	
  	//Registra los datos del usuario conectado en el arreglo local con el ID y estado 
  	socket.on("registrarDatos", function(nombreUsuario) {
  		var usuario = {nombreUsuario: nombreUsuario, id: socket.id, estado: "disponible"};
  		usuarios.push(usuario);
  		console.log("Nombre de usuario: " + usuarios[numUsuarios].nombreUsuario+" ID "+usuarios[numUsuarios].id);
  		numUsuarios++; 
  	});

  	//Recibe el nombre del usuario y el nombre del usuario al que desea retar. Busca y en caso de ser éxitoso asocia
  	socket.on("envioRetador", function(nombreUsuario, nombreRetado) {
  		var usuarioEncontrado = usuarios.find(item => item.nombreUsuario == nombreRetado);
  		if (usuarioEncontrado == undefined) {
  			socket.emit("sinJugadorRetado", function(){});
  		} else {
  			if (usuarioEncontrado.estado == "disponible") {
  			asignarAdversario(nombreUsuario, usuarioEncontrado.id);
  				asignarAdversario(nombreRetado, encontrarID(nombreUsuario));
  				io.sockets.connected[usuarioEncontrado.id].emit("retado", nombreUsuario);
  				console.log("Usuario retado: "+ nombreRetado +" Con idAdversario"+ usuarioEncontrado.idAdversario);
  				socket.emit("conJugadorRetado",function () {});
  			} else {
  				socket.emit("sinJugadorRetado", function(){});
  			}
  		}
  	});

  	//En caso de desconectarse elimina el usuario del arreglo local
  	socket.on('disconnect', function(socket){
  		eliminarUsuario(socket.id);
  		numUsuarios--;
  		console.log("Jugador desconectado");
  	});

  	//Recibe un tablero listo para el juego, y lo envía al jugador adversario
  	socket.on("envioTablero", function (nombreUsuario, tablero) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirTablero", tablero);
  	});

  	//Recibe un misil con coordenadas a atacar y lo envía al jugador adversario
  	socket.on("enviarMisil", function (nombreUsuario, misil) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirMisil", misil);
  	});

  	//Recibe el nombre del usuario y envía al jugador adversario una notifiación para tomar el turno
  	socket.on("cederTurno", function (nombreUsuario) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("ajustarTurno", function () {});
  	});

  	//Recibe el nombre del usuario y envía al jugador adversario una notifiación para avanzar en el juego
  	socket.on("adversarioListo", function (nombreUsuario) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("esperarAdversario", function () {});
  	});

  	//Recibe el nombre del usuario y envía al jugador adversario una actualización del tablero
  	socket.on("enviarCasillasALiberar", function (nombreUsuario, tableroActual) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirCasillasALiberar", tableroActual);
  	});

  	//Recibe el nombre del usuario y envía al jugador adversario la puntuación del jugador
  	socket.on("enviarPuntuacion", function (nombreUsuario, puntuacion) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirPuntuacion", puntuacion);
  	});

  	//Recibe el nombre del usuario y desasocia la relación entre los usuarios
  	socket.on("adiosAdversario", function (nombreUsuario, nombreRetado) {
  		mostrarDisponible(nombreUsuario);
  		mostrarDisponible(nombreRetado);
  	});

  	//Recibe el nombre del usuario y envía al jugador adversario una notifiación de rendición
    socket.on("enviarRendicion", function (nombreUsuario) {
      io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirRendicion", function () {});
    });

  });

  //Función para asociar a un jugador con el ID del adversario
  function asignarAdversario(nombreUsuario, idRetado) {
  	for (var i = 0; i < numUsuarios; i++) {
  		if (usuarios[i].nombreUsuario == nombreUsuario) {
  			usuarios[i].idAdversario = idRetado;
  			usuarios[i].estado = "ocupado";
  		}
  	}
  }

  //Función para encontrar el ID de un jugador
  function encontrarID(nombreUsuario) {
  	var usuarioEncontrado = usuarios.find(item => item.nombreUsuario == nombreUsuario);
  	return usuarioEncontrado.id;
  }

  //Función para eliminar un usuario del arreglo local mediante su ID
  function eliminarUsuario(idUsuarioEliminar) {
  	var posicion = usuarios.findIndex(item => item.id == idUsuarioEliminar);
  	usuarios.splice(posicion, 1);
  }

  //Función para encontrar el ID del adversario asociado a un jugador
  function encontrarIDAdversario(nombreUsuario) {
  	var usuarioEncontrado = usuarios.find(item => item.nombreUsuario == nombreUsuario);
  	return usuarioEncontrado.idAdversario;
  }

  //Función para liberar a un jugador en el arreglo local. Desasociar y mostrar disponible
  function mostrarDisponible (nombreUsuario) {
  	for (var i = 0; i < numUsuarios; i++) {
  		if (usuarios[i].nombreUsuario == nombreUsuario) {
  			usuarios[i].estado = "disponible";
  		}
  	}
  }
  
  