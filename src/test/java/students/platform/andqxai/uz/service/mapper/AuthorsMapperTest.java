package students.platform.andqxai.uz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorsMapperTest {

    private AuthorsMapper authorsMapper;

    @BeforeEach
    public void setUp() {
        authorsMapper = new AuthorsMapperImpl();
    }
}
