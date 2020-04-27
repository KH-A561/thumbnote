package net.thumbtack.thumbnote.thumbnoteroot.spring.form.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TagFormUtils {
    public static Set<String> getTagNamesAsSet(String tags) {
        Set<String> tagNames = CollectionsUtils.convertArrayToSet(tags.split("#"));
        tagNames = tagNames.stream().map(t -> t = t.trim()).collect(Collectors.toSet());
        tagNames.removeIf(String::isEmpty);
        return tagNames;
    }
}
