var io = require("socket.io")(7000);
var usuarios = [];
var numUsuarios = 0;



//Conexión de usuarios y lógica de la partida
io.on("connection", function(socket){

    console.log("Jugador conectado");

      socket.on("envioDatos", function(nombreUsuario, ipLocalhost) {
      	var usuario = {nombreUsuario:nombreUsuario, ip:ipLocalhost};
      	usuarios.push(usuario);
      	var checkUsu = usuarios[numUsuarios];
  		console.log("Nombre de usuario: " + checkUsu["nombreUsuario"]);
  		numUsuarios = numUsuarios+1; 
  		console.log("total conectados: "+ numUsuarios);
    });

       socket.on("envioDatosRetador", function(nombreUsuario, ipLocalhost, nombreRetado) {
      var usuario = {nombreUsuario:nombreUsuario, ip:ipLocalhost};
      	usuarios.push(usuario);
      var checkUsu = usuarios[numUsuarios];
  		console.log("Nombre de usuario: " + checkUsu["nombreUsuario"]);
  		numUsuarios = numUsuarios+1; 
  		for (var i = 0; i < usuarios.lenght; i++) {
  			var usuario = usuarios[i];
  			if (usuario.nombreUsuario  == nombreRetado) {
    			socket.emit("encontroJugadorRetado", function(){});
    			console.log("Usuario encontrado");
			}			 
  	};
  	console.log("total conectados: "+ numUsuarios);
    });

      socket.on('disconnect', function(socket){
      	console.log("Jugador desconectado");
      });


      socket.on('')

});