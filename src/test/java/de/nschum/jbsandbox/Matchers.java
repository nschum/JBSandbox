package de.nschum.jbsandbox;

import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.List;

public class Matchers {

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <E> Matcher<Iterable<? extends E>> contains(Matcher... itemMatchers) {
        // workaround for Java 7 issue in released Hamcrest (missing @SafeVarargs)
        final List<Matcher<? super E>> matcherList = Arrays.asList(itemMatchers);
        return org.hamcrest.Matchers.contains(matcherList);
    }

}
