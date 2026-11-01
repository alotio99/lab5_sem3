import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Visitor;
import models.Book;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // читаем файл
        List<Visitor> visitors = readVisitors("books.json");

        if (visitors == null || visitors.isEmpty()) {
            System.out.println("Данные не загружены");
            return;
        }

        System.out.println("----- Задание № 1. Посетители библиотеки -----");

        // вывод через Stream API, та штука которая делает вывод проще,без циклов
        visitors.stream()
                .map(v -> v.getName() + " " + v.getSurname())
                .forEach(System.out::println); // .map это метод, который берет
        // каждый элемент потока и преобразует его в другой объект

        System.out.println("Всего посетителей: " + visitors.size());
        printUniqueBooks(visitors);
    }

    private static List<Visitor> readVisitors(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Visitor>>() {}.getType(); // говорим какой тип данных хотим получить из JSON
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return null;
        }
    }

    private static void printUniqueBooks(List<Visitor> visitors) {
        System.out.println("\n----- Задание № 2. Книги без повторений -----");

        List<Book> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream()) // flatMap собирает все списки книг в один общий поток
                .distinct().collect(Collectors.toList()); // distinct убирает дубликаты
        // collect собирает в список

        // выводим список книг
        uniqueBooks.forEach(b -> System.out.println(b.getName() + " - " + b.getAuthor()));

        System.out.println("Уникальных книг: " + uniqueBooks.size());
    }
}
