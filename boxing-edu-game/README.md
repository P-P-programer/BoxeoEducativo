# Boxing Educational Game

## Overview
The Boxing Educational Game is a 2D interactive game designed to teach players about boxing while engaging them in a fun and educational experience. Players will control a boxer, answer quiz questions, and compete against an opponent.

## Project Structure
```
boxing-edu-game
├── src
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

## Game Rules
- Players control a boxer and must answer quiz questions to gain advantages in the boxing match.
- Each correct answer increases the player's score and health.
- The game consists of multiple rounds, and the player must defeat the opponent by reducing their health to zero.
- The game ends when either the player or the opponent is knocked out.

## How to Contribute
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them.
4. Push your branch and create a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.