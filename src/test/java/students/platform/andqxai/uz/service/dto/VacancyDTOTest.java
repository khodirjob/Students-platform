package students.platform.andqxai.uz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class VacancyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacancyDTO.class);
        VacancyDTO vacancyDTO1 = new VacancyDTO();
        vacancyDTO1.setId(1L);
        VacancyDTO vacancyDTO2 = new VacancyDTO();
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
        vacancyDTO2.setId(vacancyDTO1.getId());
        assertThat(vacancyDTO1).isEqualTo(vacancyDTO2);
        vacancyDTO2.setId(2L);
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
        vacancyDTO1.setId(null);
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
    }
}
