package dev.ivanov.validator.rule;

import java.lang.annotation.Annotation;

public interface Rule<T extends Annotation> {
    void check(T annotation, String fieldName, Object target);

    Class<T> getAnnotationClass();
}