package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // генерирует геттеры, сеттеры, тустринг, иквалс и хэшкод
@NoArgsConstructor // пустой конструктор
@AllArgsConstructor
public class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
}