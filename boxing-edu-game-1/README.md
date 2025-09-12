# Boxing Educativo

## Descripción
Boxing Educativo es un juego interactivo que combina elementos de boxeo con preguntas educativas. Los jugadores pueden elegir entre diferentes boxeadores y competir en un entorno de juego que desafía su conocimiento a través de preguntas.

## Estructura del Proyecto
El proyecto está organizado de la siguiente manera:

```
boxing-edu-game
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── controller
│   │   │   │   ├── GameController.java
│   │   │   │   └── MainMenuController.java
│   │   │   ├── main
│   │   │   │   └── Main.java
│   │   │   ├── model
│   │   │   │   ├── Boxer.java
│   │   │   │   ├── GameState.java
│   │   │   │   ├── MenuState.java
│   │   │   │   └── Question.java
│   │   │   └── view
│   │   │       ├── GameView.java
│   │   │       └── MainMenuView.java
│   │   └── resources
│   │       ├── boxeadores
│   │       │   ├── Boxeador_0001.png
│   │       │   ├── Boxeador_0002.png
│   │       │   └── Boxeador_0003.png
│   │       ├── enemigos
│   │       ├── fondo
│   │       ├── sonidos
│   │       └── UI
│   │           ├── Menu.png
│   │           └── barra de vida
│   │               ├── no-heart-standard-empty-1.png
│   │               ├── no-heart-standard-empty-2.png
│   │               ├── no-heart-standard-empty-3.png
│   │               ├── no-heart-standard-full-1.png
│   │               ├── no-heart-standard-full-2.png
│   │               ├── no-heart-standard-full-3.png
│   │               ├── standard-1.png
│   │               ├── standard-2.png
│   │               ├── standard-3.png
│   │               ├── standard-empty-1.png
│   │               ├── standard-empty-2.png
│   │               ├── standard-empty-3.png
│   │               ├── standard-full-1.png
│   │               ├── standard-full-2.png
│   │               └── standard-full-3.png
├── pom.xml
└── README.md
```

## Requisitos
- Java 8 o superior
- Maven

## Instalación
1. Clona el repositorio en tu máquina local.
2. Navega al directorio del proyecto.
3. Ejecuta el siguiente comando para compilar el proyecto:
   ```
   mvn clean install
   ```
4. Para ejecutar el juego, utiliza el siguiente comando:
   ```
   mvn exec:java -Dexec.mainClass="main.Main"
   ```

## Contribuciones
Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o un pull request.

## Licencia
Este proyecto está bajo la Licencia MIT.