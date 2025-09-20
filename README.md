# Boxing Educativo

## Descripción
Boxing Educativo es un juego interactivo que combina elementos de boxeo con preguntas educativas. Los jugadores pueden elegir entre diferentes boxeadores y competir en un entorno de juego que desafía su conocimiento a través de preguntas.

## Estructura del Proyecto
El proyecto está organizado de la siguiente manera con la estructura MVC:

```
BoxeoEducativo
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── controller
│   │   │   ├── main
│   │   │   ├── model
│   │   │   └── view
│   │   └── resources
│   │       ├── boxeadores
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
├── README.md
├── boxing-edu-game.exe         # Ejecutable para usuarios finales (opcional)
├── jdk/                        # JRE portable para ejecutar el juego sin instalar Java
└── (otros archivos y carpetas)
```

## Requisitos
- Java 8 o superior (solo para desarrolladores)
- Maven (solo para desarrolladores)
- Para usuarios finales: **NO es necesario instalar Java** si usas el ejecutable y la carpeta `jdk` incluida.

## Instalación y Ejecución

### Opción 1: Ejecutar como usuario final (sin instalar Java)
1. Descarga el archivo `boxing-edu-game.exe` y la carpeta `jdk` (JRE portable).
2. Coloca ambos en la misma carpeta junto con los recursos del juego.
3. Haz doble clic en `boxing-edu-game.exe` para jugar.
   - **Nota:** Si el juego no abre, asegúrate de que la carpeta `jdk` esté presente y no renombrada.

### Opción 2: Ejecutar como desarrollador (con Maven y Java instalado)
1. Clona el repositorio en tu máquina local.
2. Navega al directorio del proyecto.
3. Compila el proyecto:
   ```
   mvn clean install
   ```
4. Ejecuta el juego:
   ```
   mvn exec:java -Dexec.mainClass="main.Main"
   ```

## Contribuciones
Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o un pull request.

## Licencia
Este proyecto está bajo la Licencia MIT.