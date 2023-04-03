package students.platform.andqxai.uz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VacancyMapperTest {

    private VacancyMapper vacancyMapper;

    @BeforeEach
    public void setUp() {
        vacancyMapper = new VacancyMapperImpl();
    }
}
