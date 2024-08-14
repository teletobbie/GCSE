# Smart heater project

## Simulator
The extension of the doc file you have received should be changed to a .jar file.
Welcome to SMART-CV project. This repository is only supposed to be used by the teachers or tutors. Students should only get the jar as a black-box.

## What is CV-CGI
Suppose that there is a house with a central heating system (Centrale Verwarming abbreviated as CV in Dutch). This CV can be programmed through an API to automate the heating regulation. The house has a few sensors which are connected with the CV. Simply put, this is what the CV-CGI simulates. The CV-CGI is the simulation of a central heating system which can be programmed through an API to make the CV smarter.

## Usage
The CV simulation can be started as a standalone application both on its dedicated modified Raspberry Pi, or as a standlone application on a machine which has java 8 or higher. On a Pi the CV will change the LED colors. As a standalone the application simulates its 3 Pi leds as three buttons in a small GUI.

### Start the CV
Once started, it makes its API available through a TCP/IP connection. Clients can connect through the API according to CV-CGI protocol and send commands and programs to the CV. Based on the different inputs like sensors, commands and programs, the CV creates output.

1. Development mode: Run the jar as a standalone Smart CV simulator. I can be started from command line by running: java -jar <<CV-CGI-JAR>>
2. Raspberry Pi mode: Run a didicated raspberry Pi which is modified for this CV: Use run-cv--on-pi.sh script

During the startup, the CV is started with a secret number which is required during client connection.

After succesful start, the simulator sends two  Long EPOCH seconds. THe first one shows the simulated timestamp and the second one is the real time. This times can be used to sync the systems which use the simulator.

### Connect
The simulator supports only one client at a time.

#### Connect (Socket protocol)

Client request:`$CV-CONNECT-$-427163`
CV response: `#CONNECT-OK#1488814606#1488813796`

The client request


#### Status request
Client request:   `$CV-STAT?`
CV response:  `#STAT#161#15.62#10.20#1#1488813845#0.03#1488814881`

Respone exists out of:
- keteldruk, in dit geval 1.61 bar
- Binnentemperatuur, in dit geval 15.62 graden Celsius
- Buitentemperatuur, in dit geval 10,20 graden Celsius
- Deursensor, in dit geval Dicht (1), 0 voor open. (er is maar 1 deur in het huisje met een sensor en dat is de (enige) deur naar buiten)
- Bewegingssensor, epoch second (als er een beweging is mag je aannemen dat er iemand in het huis is, het huisje is 1 grote ruimte :P)
- Gasverbruik in m3, in dit geval 0.03
- Tijdstip waarop deze data gegenereerd werd

#### Commands
Het commando(client request) bevat twee gehele getallen. De eerste is de stand (%) van de Water Kraan/Pomp, de tweede de stand van de brander.

Client request:  `$CV-ACT-$10$90`
CV response: `#ACT-OK`

