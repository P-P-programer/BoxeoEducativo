# Boxing Educational Game

## Overview
Boxeo Educativo es un juego interactivo en 2D diseñado para enseñar sobre boxeo de manera divertida y didáctica. El jugador controla a un boxeador, responde preguntas educativas y compite contra un oponente en rondas dinámicas.

## Project Structure
```
boxing-edu-game/
├── recursos/
│   ├── boxeadores/     # Sprites y animaciones del jugador
│   ├── enemigos/       # Sprites y animaciones de enemigos
│   ├── fondo/          # Imágenes de escenarios y fondos
│   ├── sonidos/        # Efectos de sonido y música
│   └── UI/ 
├── src/
│   ├── controller
│   │   └── GameController.java
│   ├── model
│   │   ├── Boxer.java
│   │   ├── GameState.java
│   │   └── Question.java
│   ├── view
│   │   ├── GameView.java
│   │   └── MainMenuView.java
│   └── main
│       └── Main.java
└── README.md
```

## Setup Instructions
1. **Clone the repository**:
   ```
   git clone <repository-url>
   ```
2. **Navigate to the project directory**:
   ```
   cd boxing-edu-game
   ```
3. **Compile the project**:
   Use your preferred Java IDE or command line to compile the Java files in the `src` directory.
   ```
   javac src/**/*.java
   ```
4. **Run the game**:
   Execute the main class to start the game.
   ```
   java src/main/Main
   ```

## Reglas del juego
- Controla a un boxeador y responde preguntas educativas para obtener ventajas en el combate.
- Cada respuesta correcta aumenta tu puntuación y salud.
- El juego consta de varias rondas; debes derrotar al oponente reduciendo su salud a cero.
- El juego termina cuando el jugador o el oponente es noqueado.

## Cómo contribuir
1. Haz un fork del repositorio.
2. Crea una nueva rama para tu funcionalidad o corrección.
3. Realiza tus cambios y haz commit.
4. Haz push de tu rama y crea un pull request.


## License
This project is licensed under the MIT License. See the LICENSE file for more details.