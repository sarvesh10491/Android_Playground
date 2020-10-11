// Modules
// var http = require("http");
// const express = require('express');
// const { spawn } = require('child_process');
// var app = express();

var net = require('net');

const hostname = '192.168.1.100';
const port = 3000;
// var script_path = '/home/tesla/android/unscrabble_server/unscrabble_appcompute.py';

// app.get("/:ipword", (req, res) => {
//         //res.send('Connected to Cloud Backend Server!')
//         //Set the response HTTP header with HTTP status and Content type
//         res.statusCode = 200;
//         res.setHeader('Content-Type', 'text/plain');
        
//         console.log("Word received : "+req.params.ipword)

//         const pyProg = spawn('python', [script_path, req.params.ipword]);

//         pyProg.stdout.on('data', (data) => {
//             res.send(data);
//             console.log('Wordlist : ' + `${data}`);
//         });

//         pyProg.on('exit', function (code, signal) {
//                 console.log('child process exited with ' + `code ${code} and signal ${signal}`);
//         });

// });
// app.listen(port, () => console.log(`Server running at http://${hostname}:${port}/`))

net.createServer(function(sock) {
        // We have a connection - a socket object is assigned to the connection automatically
       console.log('CONNECTED: ' + sock.remoteAddress +':'+ sock.remotePort);
        // Add a 'data' event handler to this instance of socket
        sock.on('data', function(data) {
          console.log('DATA ' + sock.remoteAddress + ': ' + data);
          // Write the data back to the socket, the client will receive it as data from the server
          sock.write('You said "' + data + '"');
        });
        // Add a 'close' event handler to this instance of socket
       sock.on('close', function(data) {
         console.log('CLOSED: ' + sock.remoteAddress +' '+ sock.remotePort);
       });
      
      }).listen(port, hostname);
      
console.log('Server listening on ' + hostname +':'+ port);