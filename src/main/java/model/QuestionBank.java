package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionBank {
    private List<Question> questions = new ArrayList<>();
    private Random random = new Random();

    public QuestionBank() {
        addQuestion(new Question("¿Cuál es la capital de Francia?", new String[]{"Madrid", "París", "Roma", "Berlín"}, 1, 10));
        addQuestion(new Question("¿Cuánto es 7 x 8?", new String[]{"54", "56", "64", "58"}, 1, 10));
        addQuestion(new Question("¿Quién escribió 'El Quijote'?", new String[]{"Miguel de Cervantes", "Gabriel García Márquez", "Pablo Neruda", "Mario Vargas Llosa"}, 0, 10));
        addQuestion(new Question("¿Cuál es el elemento químico H?", new String[]{"Helio", "Hidrógeno", "Hierro", "Hafnio"}, 1, 10));
        addQuestion(new Question("¿En qué año llegó el hombre a la luna?", new String[]{"1965", "1969", "1972", "1959"}, 1, 10));
        // ...agrega más preguntas aquí...
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public Question getRandomQuestion() {
        if (questions.isEmpty()) return null;
        int idx = random.nextInt(questions.size());
        return questions.get(idx);
    }

    public List<Question> getAllQuestions() {
        return questions;
    }
}