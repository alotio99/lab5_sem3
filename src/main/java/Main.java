import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Visitor;
import models.Book;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

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
        printSortedBooks(visitors);
        checkForAuthor(visitors);
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

    private static void printSortedBooks(List<Visitor> visitors) {
        System.out.println("\n----- Задание № 3. Сортировка книг по году -----");

        visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream()) // достаем все книги в один поток
                .sorted(Comparator.comparingInt(Book::getPublishingYear)) // сортируем по году
                .distinct() // убираем дубликаты
                .forEach(b -> System.out.println(b.getPublishingYear() + " - " + b.getName() + " (" + b.getAuthor() + ")"));
    }

    private static void checkForAuthor(List<Visitor> visitors) {
        System.out.println("\n----- Задание № 4. Поиск автора -----");

        boolean hasAuthor = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(b -> b.getAuthor().equalsIgnoreCase("Jane Austen"));
        // anyMatch проверяет, есть ли хоть одна подходящая
        if (hasAuthor) {
            System.out.println("Да, у кого-то из посетителей есть книга Jane Austen.");
        } else {
            System.out.println("Нет, книги Jane Austen у посетителей нет.");
        }
    }
}
