  var io = require("socket.io")(7000);
  var usuarios = [];
  var numUsuarios = 0;



  //Conexión de usuarios y lógica de la partida
  io.on("connection", function(socket){

    console.log("Jugador conectado");

    socket.on("registrarDatos", function(nombreUsuario) {
      var usuario = {nombreUsuario:nombreUsuario, id:socket.id};
      usuarios.push(usuario);
      console.log("Nombre de usuario: " + usuarios[numUsuarios].nombreUsuario);
      numUsuarios++; 
      console.log("total conectados: "+ numUsuarios);
    });

    socket.on("envioRetador", function(nombreUsuario, nombreRetado, idUsuario) {
      
     
      for (var i = 0; i < numUsuarios; i++) {
        var usuarioRetado = usuarios[i];
        if (usuarioRetado.nombreUsuario == nombreRetado) {
          asignarAdversario(nombreUsuario, usuarioRetado.id);
          asignarAdversario(nombreRetado, idUsuario);

          io.sockets.connected[usuarioRetado.id].emit("retado", function(nombreUsuario){});
          console.log("Usuario retado: "+ nombreRetado +" Con idAdversario")+ usuarios[i].idAdversario;
          check = true;
          break;
        }       
      };
      if (!check) {
        socket.emit("sinJugadorRetado", function(){});
      }
      console.log("total conectados: "+ numUsuarios);
    });

    socket.on('disconnect', function(socket){
      console.log("Jugador desconectado");

    });


  });
  function asignarAdversario(nombreUsuario, idRetado) {
    for (var i = 0; i < numUsuarios; i++) {
      if (usuarios[i].nombreUsuario == nombreUsuario) {
        usuarios[i].idAdversario = idRetado;
        console.log("idRetado asig"+ usuarios[i].idAdversario)
      }
    }
  }