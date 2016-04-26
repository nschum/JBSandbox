package de.nschum.jbsandbox.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class TinyMapTest {

    private Map<String, String> map;

    @Before
    public void setUp() throws Exception {
        map = new TinyMap<>();
    }

    @Test
    public void shouldBeEmpty() {
        assertThat(map.size(), equalTo(0));
    }

    @Test
    public void shouldNotReturnNotExistingValue() {
        assertThat(map.get("foo"), nullValue());
    }

    @Test
    public void shouldNotIterateOverNotExistingValue() {
        // TODO
    }

    @Test
    public void addingValueShouldIncreaseSize() {
        map.put("foo", "bar");

        assertThat(map.size(), equalTo(1));
    }

    @Test
    public void shouldReturnValueAfterAddingIt() {
        map.put("foo", "bar");

        assertThat(map.get("foo"), equalTo("bar"));
    }

    @Test
    public void shouldIterateOverValueAfterAddingIt() {
        map.put("foo", "bar");

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        assertThat(map.entrySet().size(), equalTo(1));
        assertThat(map, hasEntry("foo", "bar"));
    }

    @Test
    public void shouldReturnUpdatedValueAfterUpdatingIt() {
        map.put("foo", "bar");
        map.put("foo", "baz");

        assertThat(map.get("foo"), equalTo("baz"));
    }

    @Test
    public void addingSecondValueShouldIncreaseSize() {
        map.put("foo", "bar");
        map.put("x", "y");

        assertThat(map.size(), equalTo(2));
    }

    @Test
    public void shouldReturnBothValuesAfterAddingSecondValue() {
        map.put("foo", "bar");
        map.put("x", "y");

        assertThat(map.get("foo"), equalTo("bar"));
        assertThat(map.get("x"), equalTo("y"));
    }

    @Test
    public void shouldIterateOverBothValuesValueAfterAddingSecondValue() {
        map.put("foo", "bar");
        map.put("x", "y");

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        assertThat(map.entrySet().size(), equalTo(2));
        assertThat(map, hasEntry("foo", "bar"));
        assertThat(map, hasEntry("x", "y"));
    }

    @Test
    public void shouldReturnUpdatedSecondValueAfterUpdatingIt() {
        map.put("foo", "bar");
        map.put("x", "y");
        map.put("x", "z");

        assertThat(map.get("x"), equalTo("z"));
    }

    @Test(expected = TinyMap.TinyMapLimitExceededException.class)
    public void addingThirdValueShouldThrowException() {
        map.put("foo", "bar");
        map.put("x", "y");
        map.put("a", "b");
    }
}
