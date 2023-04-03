package students.platform.andqxai.uz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class CafeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CafeDTO.class);
        CafeDTO cafeDTO1 = new CafeDTO();
        cafeDTO1.setId(1L);
        CafeDTO cafeDTO2 = new CafeDTO();
        assertThat(cafeDTO1).isNotEqualTo(cafeDTO2);
        cafeDTO2.setId(cafeDTO1.getId());
        assertThat(cafeDTO1).isEqualTo(cafeDTO2);
        cafeDTO2.setId(2L);
        assertThat(cafeDTO1).isNotEqualTo(cafeDTO2);
        cafeDTO1.setId(null);
        assertThat(cafeDTO1).isNotEqualTo(cafeDTO2);
    }
}
