package students.platform.andqxai.uz.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import students.platform.andqxai.uz.domain.Book;

public interface BookRepositoryWithBagRelationships {
    Optional<Book> fetchBagRelationships(Optional<Book> book);

    List<Book> fetchBagRelationships(List<Book> books);

    Page<Book> fetchBagRelationships(Page<Book> books);
}
