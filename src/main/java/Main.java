import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Visitor;
import models.Book;
import models.SmsMessage;

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
        printMaxBooks(visitors);
        printSmsMessages(visitors);
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

    private static void printMaxBooks(List<Visitor> visitors) {
        System.out.println("\n----- Задание № 5. Максимальное число книг -----");

        // mapToInt превращает посетителя в размер его списка книг
        int maxBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max() // max() ищет самое большое число
                .orElse(0); // orElse(0) вернет 0, если список посетителей пуст

        System.out.println("Максимальное число книг у одного посетителя: " + maxBooks);
    }

    private static void printSmsMessages(List<Visitor> visitors) {
        System.out.println("\n----- Задание № 6. Сообщения подписчикам -----");

        // среднее арифметическое по всем посетителям
        double average = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average() // считает среднее
                .orElse(0.0); // среднее 0.0 если список посетителей пуст

        // фильтруем подписчиков и создаем для них SMS
        List<SmsMessage> messages = visitors.stream()
                .filter(v -> v.isSubscribed()) // оставляем только subscribed = true
                .map(v -> {
                    int count = v.getFavoriteBooks().size(); // узнаем сколько книг
                    String text;

                    if (count > average) {
                        text = "you are cool guy";
                    } else if (count < average) {
                        text = "you idiot";
                    } else {
                        text = "ok";
                    }

                    // создаем смс с номером телефона посетителя
                    return new SmsMessage(v.getPhone(), text);
                })
                .collect(Collectors.toList()); // собираем в список

        // выводим результат
        messages.forEach(m -> System.out.println("На номер " + m.getPhoneNumber() + ": " + m.getMessage()));
        System.out.println("Всего отправлено сообщений: " + messages.size());
    }
}
