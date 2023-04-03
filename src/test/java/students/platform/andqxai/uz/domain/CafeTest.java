package students.platform.andqxai.uz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import students.platform.andqxai.uz.web.rest.TestUtil;

class CafeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cafe.class);
        Cafe cafe1 = new Cafe();
        cafe1.setId(1L);
        Cafe cafe2 = new Cafe();
        cafe2.setId(cafe1.getId());
        assertThat(cafe1).isEqualTo(cafe2);
        cafe2.setId(2L);
        assertThat(cafe1).isNotEqualTo(cafe2);
        cafe1.setId(null);
        assertThat(cafe1).isNotEqualTo(cafe2);
    }
}
