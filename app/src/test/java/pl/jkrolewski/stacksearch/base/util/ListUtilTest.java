package pl.jkrolewski.stacksearch.base.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListUtilTest {

    @Test
    public void shouldReturnPassedListWhenInputNotNull()  {
        // given
        List<String> input = new ArrayList<>();

        // when
        List<String> output = ListUtil.emptyForNull(input);

        // then
        assertThat(output).isSameAs(input);
    }

    @Test
    public void shouldReturnEmptyListWhenInputNull()  {
        // when
        List<String> output = ListUtil.emptyForNull(null);

        // then
        assertThat(output).hasSize(0);
    }
}