package students.platform.andqxai.uz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CafeMapperTest {

    private CafeMapper cafeMapper;

    @BeforeEach
    public void setUp() {
        cafeMapper = new CafeMapperImpl();
    }
}
