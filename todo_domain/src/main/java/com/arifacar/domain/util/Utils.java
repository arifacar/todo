package com.arifacar.domain.util;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class Utils {

    public static <E> void addAll(List<E> list, Collection<? extends E> c) {
        if (c != null) {
            list.addAll(c);
        }
    }
}
