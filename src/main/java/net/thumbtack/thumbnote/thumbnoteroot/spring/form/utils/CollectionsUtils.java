package net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionsUtils {
    @SafeVarargs
    public static <T> Set<T> convertArrayToSet(T... items) {
        return new HashSet<>(Arrays.asList(items));
    }
}
