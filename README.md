# Prova Finale di Ingegneria del Software - AA 2020-2021 : Masters of Renaissance
![alt text](src/main/resources/LogoMastersGIT.png)

This project is the JAVA implemantation of the board game: [Masters of Renaissance](http://www.craniocreations.it/prodotto/masters-of-renaissance/).

## Documentation

### UML
These are the diagrams of the classes, the first one is the UML created at the beginning of the project, the last one is the final UML created at the end of the project. 

- [Initial UML](deliveries/UML/Model_initial_UML.jpg)
- [Final UML](deliveries/UML/Model_final_UML.png)

### Coverage report
Numerous tests have been performed using JUnit, you can consult the coverage report at the following link: [Report](https://github.com/chinifabio/ingswAM2021-Chini-Colabene-Curreri/tree/master/deliveries/Report/Test%20report)


### Plugins and Libraries
|Libreria/Plugin|
|---------------|
|__Maven__|
|__JUnit__|

## Implemented Functionalities
| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Basic rules | ![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b)|
| Complete rules | ![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b)|
| Socket |![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b) |
| GUI | ![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b) |
| CLI |![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b) |
| Multiple games | ![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b)|
| Persistence | ![RED](https://dummyimage.com/20x20/ff0015/ff0015) |
| Parameter editor | ![RED](https://dummyimage.com/20x20/ff0015/ff0015) |
| Local game | ![RED](https://dummyimage.com/20x20/ff0015/ff0015) |
| Resilience to disconnections | ![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b)|
#### Legend
[![RED](https://dummyimage.com/20x20/ff0015/ff0015)]() Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;[![GREEN](https://dummyimage.com/20x20/00ff7b/00ff7b)]() Implemented


## Software

**StarUML** - UML diagrams and sequence diagrams

**Intellij IDEA Ultimate** - main IDE 


## Jar

The Jar of this project can be found here: [JAR](deliveries/Jar/masters.java)

### Execution of Jar

### Client
The following instructions describe how to successfully start the application in GUI and CLI.</br>
Due to the limitations of the Windows command line, it is necessary to use WSL in order to play with CLI or to run the server with this operating system, instead the CLI works perfectly with Linux and macOS operating systems.

### Parameters
The JAR can receives three paramters: what you want to exectue, server address and port you want to connect to.</br>
If you don't specify any of this parameters, the application will ask you a value for them.</br>
To set the value of a parameter you need to type the name of the parameter followed by ":" and then by the value of the parameter.</br>
Admitted value for **core** are: **server**, **cli** or **gui**. </br>


Below you can see how to run **Server**, **CLI** and **GUI**, connecting to **localhost:4444**.

#### Server
To run the server of the game you can digit in the command line: 
```
java -jar masters.java core:server address:localhost port:4444
```

#### CLI
To play properly with the CLI it is recommended to use the full screen window.</br>
To run the cli of the game you can digit in the command line: 
```
java -jar masters.java core:cli address:localhost port:4444
```

#### GUI
To play properly with the GUI it is advisable to use the windows zoom at 100%.</br>
To run the gui of the game you can digit in the command line: 
```
java -jar masters.java core:gui address:localhost port:4444
```

## Team members
- [__Fabio Chini__](https://github.com/chinifabio)
- [__Franscesco Colabene__](https://github.com/FrancescoColabene)
- [__Vincenzo Curreri__](https://github.com/Vinz-z)
