package students.platform.andqxai.uz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApartmentMapperTest {

    private ApartmentMapper apartmentMapper;

    @BeforeEach
    public void setUp() {
        apartmentMapper = new ApartmentMapperImpl();
    }
}
