  var io = require("socket.io")(7000);
  var usuarios = [];
  var numUsuarios = 0;



  //Conexión de usuarios y lógica de la partida
  io.on("connection", function(socket){

  	console.log("Jugador conectado");

  	socket.on("registrarDatos", function(nombreUsuario) {
  		var usuario = {nombreUsuario: nombreUsuario, id: socket.id, estado: "disponible"};
  		usuarios.push(usuario);
  		console.log("Nombre de usuario: " + usuarios[numUsuarios].nombreUsuario+" ID "+usuarios[numUsuarios].id);
  		numUsuarios++; 
  		console.log("total conectados: "+ numUsuarios);
  	});


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

  	socket.on('disconnect', function(socket){
  		eliminarUsuario(socket.id);
  		numUsuarios--;
  		console.log("Jugador desconectado");
  	});
  	socket.on("envioTablero", function (nombreUsuario, tablero) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirTablero", tablero);
  	});
  	socket.on("enviarMisil", function (nombreUsuario, misil) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirMisil", misil);
  	});
  	socket.on("cederTurno", function (nombreUsuario) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("ajustarTurno", function () {});
  	});
  	socket.on("adversarioListo", function (nombreUsuario) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("esperarAdversario", function () {});
  	});
  	socket.on("enviarCasillasALiberar", function (nombreUsuario, tableroActual) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirCasillasALiberar", tableroActual);
  	});
  	socket.on("enviarPuntuacion", function (nombreUsuario, puntuacion) {
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirPuntuacion", puntuacion);
  	});
  	socket.on("adiosAdversario", function (nombreUsuario, nombreRetado) {
  		mostrarDisponible(nombreUsuario);
  		mostrarDisponible(nombreRetado);
  	});
    socket.on("enviarRendicion", function (nombreUsuario) {
      io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirRendicion", function () {});
    });

  });
  function asignarAdversario(nombreUsuario, idRetado) {
  	for (var i = 0; i < numUsuarios; i++) {
  		if (usuarios[i].nombreUsuario == nombreUsuario) {
  			usuarios[i].idAdversario = idRetado;
  			usuarios[i].estado = "ocupado";
  		}
  	}
  }
  function encontrarID(nombreUsuario) {
  	var usuarioEncontrado = usuarios.find(item => item.nombreUsuario == nombreUsuario);
  	return usuarioEncontrado.id;
  }
  function eliminarUsuario(idUsuarioEliminar) {
  	var posicion = usuarios.findIndex(item => item.id == idUsuarioEliminar);
  	usuarios.splice(posicion, 1);
  }
   function encontrarIDAdversario(nombreUsuario) {
  	var usuarioEncontrado = usuarios.find(item => item.nombreUsuario == nombreUsuario);
  	return usuarioEncontrado.idAdversario;
  }
  function mostrarDisponible (nombreUsuario) {
  	for (var i = 0; i < numUsuarios; i++) {
  		if (usuarios[i].nombreUsuario == nombreUsuario) {
  			usuarios[i].estado = "disponible";
  		}
  	}
  }
  
  