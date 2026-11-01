package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data // генерирует геттеры, сеттеры, тустринг, иквалс и хэшкод
@NoArgsConstructor // пустой конструктор
public class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;
}
