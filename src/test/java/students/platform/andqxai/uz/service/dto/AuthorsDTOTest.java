package students.platform.andqxai.uz.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class AuthorsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorsDTO.class);
        AuthorsDTO authorsDTO1 = new AuthorsDTO();
        authorsDTO1.setId(1L);
        AuthorsDTO authorsDTO2 = new AuthorsDTO();
        assertThat(authorsDTO1).isNotEqualTo(authorsDTO2);
        authorsDTO2.setId(authorsDTO1.getId());
        assertThat(authorsDTO1).isEqualTo(authorsDTO2);
        authorsDTO2.setId(2L);
        assertThat(authorsDTO1).isNotEqualTo(authorsDTO2);
        authorsDTO1.setId(null);
        assertThat(authorsDTO1).isNotEqualTo(authorsDTO2);
    }
}
