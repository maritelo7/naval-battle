  var io = require("socket.io")(7000);
  var usuarios = [];
  var numUsuarios = 0;



  //Conexión de usuarios y lógica de la partida
  io.on("connection", function(socket){

  	console.log("Jugador conectado");

  	socket.on("registrarDatos", function(nombreUsuario) {
  		var usuario = {nombreUsuario: nombreUsuario, id: socket.id, estado: "disponible"};
  		usuarios.push(usuario);
  		console.log("Nombre de usuario: " + usuarios[numUsuarios].nombreUsuario+"ID "+usuarios[numUsuarios].id);
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
  		console.log("Recibo tablero");
  		io.sockets.connected[encontrarIDAdversario(nombreUsuario)].emit("recibirTablero", tablero);
  		console.log("Envíe tablero");
  	});


  });
  function asignarAdversario(nombreUsuario, idRetado) {
  	for (var i = 0; i < numUsuarios; i++) {
  		if (usuarios[i].nombreUsuario == nombreUsuario) {
  			usuarios[i].idAdversario = idRetado;
  			usuarios[i].estado = "ocupado";
  			console.log("idAdversario"+ usuarios[i].idAdversario)
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
  