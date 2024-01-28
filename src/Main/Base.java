package Main;

import Model.Question;
import Model.Test;
import Model.User;

import java.io.*;
import java.util.ArrayList;

/**
 * Класс создания и загрузки данных из файлов в память
 */
public class Base {
    private final ArrayList<Test> tests =
            new ArrayList<>();//коллекция тестов
    private final ArrayList<User> users
            = new ArrayList<>();//коллекция пользователей
    private final ArrayList<Question> questions
            = new ArrayList<>();//коллекция вопросов

    private final File fQuestions =
            new File(Configuration.BASE_DIRECTORY +
                    "questions.dat");
    private final File fUsers =
            new File(Configuration.BASE_DIRECTORY +
                    "users.dat");
    private final File fTests =
            new File(Configuration.BASE_DIRECTORY +
                    "tests.dat");


    private static Base base;
    private Base() {
        loadBase();//загрузка данных из файлов в коллекции tests, users, questions
    }
    public static synchronized Base getInstance() {// Реализация паттерна Singletone
        if (base == null) {
            base = new Base();
        }
        return base;
    }
    public ArrayList<Test> getTests() {
        return tests;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Question> getQuestions() {
        return questions;
    }
    /**
     * Проверяет существование файлов данных, при отсутствии их создает
     * @param file имя файла данных
     */
    private void createFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Файл " + file.toString() + " не создан");
                e.printStackTrace();
            }
        }
    }
    private void loadBase() {
        loadDataFromFile(tests, fTests);
        loadDataFromFile(users, fUsers);
        loadDataFromFile(questions, fQuestions);
    }
    public void saveBase() {
        saveData(tests, fTests);
        saveData(users, fUsers);
        saveData(questions, fQuestions);
    }
    /**
     * Метод загрузки объектов из файла в память
     * @param records коллекция объектов
     * @param file    путь к фйлу объестов
     * @param <T>     тип загружаемых объектов
     */
    private  <T extends Serializable>
    void loadDataFromFile(ArrayList<T> records, File file) {
        createFile(file);
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(file))) {
            try {
                while (true) {
                    records.add((T) in.readObject());
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Класс не найден");
            } catch (EOFException e) {
                System.out.println("Файл " + file + " прочитан");
            } catch (IOException e) {
                System.out.println("Ошибки ввода-вывод" + e);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл " + file + " не найден");
        } catch (EOFException e) {
            System.out.println("Файл " + file + " пустой");
        } catch (IOException e) {
            System.out.println("Ошибки ввода-вывод" + e);
        }
    }
    /**
     * Метод сохранения объектов из памяти в файл
     *
     * @param records коллекция объектов
     * @param file    путь к фйлу объестов
     * @param <T>     тип объектов
     */
    private <T extends Serializable> void saveData(ArrayList<T> records, File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (T obj : records) {
                out.writeObject(obj);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Ошибки ввода-вывод" + e);
        }
    }
}
