var io = require("socket.io")(7000);
var cuentasUsuarios = new Array();
var direccionesIpUsuarios = new Array();


//Conexión de usuarios y lógica de la partida
io.on("connection", function(socket){
    console.log("Cliente conectado");

      socket.on("envioDatos", function(nombreUsuario, ipLocalhost) {
  		console.log("Nombre de usuario: " + nombreUsuario + "  Ip: " + ipLocalhost);  		
    });


});