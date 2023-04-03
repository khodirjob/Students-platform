package students.platform.andqxai.uz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class ApartmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApartmentDTO.class);
        ApartmentDTO apartmentDTO1 = new ApartmentDTO();
        apartmentDTO1.setId(1L);
        ApartmentDTO apartmentDTO2 = new ApartmentDTO();
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
        apartmentDTO2.setId(apartmentDTO1.getId());
        assertThat(apartmentDTO1).isEqualTo(apartmentDTO2);
        apartmentDTO2.setId(2L);
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
        apartmentDTO1.setId(null);
        assertThat(apartmentDTO1).isNotEqualTo(apartmentDTO2);
    }
}
