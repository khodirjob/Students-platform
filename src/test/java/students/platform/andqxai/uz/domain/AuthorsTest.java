package students.platform.andqxai.uz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class AuthorsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authors.class);
        Authors authors1 = new Authors();
        authors1.setId(1L);
        Authors authors2 = new Authors();
        authors2.setId(authors1.getId());
        assertThat(authors1).isEqualTo(authors2);
        authors2.setId(2L);
        assertThat(authors1).isNotEqualTo(authors2);
        authors1.setId(null);
        assertThat(authors1).isNotEqualTo(authors2);
    }
}
