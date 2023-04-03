package students.platform.andqxai.uz.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import students.platform.andqxai.uz.domain.Authors;
import students.platform.andqxai.uz.domain.Book;
import students.platform.andqxai.uz.domain.Category;
import students.platform.andqxai.uz.service.dto.AuthorsDTO;
import students.platform.andqxai.uz.service.dto.BookDTO;
import students.platform.andqxai.uz.service.dto.CategoryDTO;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorsFullNameSet")
    BookDTO toDto(Book s);

    @Mapping(target = "removeAuthors", ignore = true)
    Book toEntity(BookDTO bookDTO);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("authorsFullName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    AuthorsDTO toDtoAuthorsFullName(Authors authors);

    @Named("authorsFullNameSet")
    default Set<AuthorsDTO> toDtoAuthorsFullNameSet(Set<Authors> authors) {
        return authors.stream().map(this::toDtoAuthorsFullName).collect(Collectors.toSet());
    }
}
