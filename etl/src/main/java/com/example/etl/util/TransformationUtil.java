package com.example.etl.util;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TransformationUtil {

    public static <T, R> List<R> transformList(final List<T> list, final Function<T, R> transform) {
        if (list == null) {
            return Collections.emptyList();
        }

        return list.stream()
                .map(transform)
                .toList();
    }

}
