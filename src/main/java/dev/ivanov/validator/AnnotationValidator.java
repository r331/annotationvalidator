package dev.ivanov.validator;

import dev.ivanov.validator.api.EntityValidator;
import dev.ivanov.validator.rule.Rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList;

public class AnnotationValidator implements EntityValidator {
    private final Map<Class<? extends Annotation>, Rule<?>> rules;

    public AnnotationValidator(List<Rule<?>> rules) {
        this.rules = rules.stream()
                .collect(toMap(Rule::getAnnotationClass, identity()));
    }

    @Override
    public void validate(Object entity) {
        getAllFieldsList(entity.getClass())
                .stream()
                .peek(f -> f.setAccessible(true))
                .forEach(field -> {
                    Annotation[] annotations = field.getAnnotations();
                    if (annotations.length == 0) return;

                    Object fieldValue = getValue(field, entity);
                    for (Annotation annotation : annotations) {
                        doCheck(annotation, field.getName(), fieldValue);
                    }
                });
    }

    private void doCheck(Annotation annotation, String field, Object fieldValue) {
        Rule r = rules.get(annotation.annotationType());
        if (r == null) return;

        r.check(annotation, field, fieldValue);
    }

    private Object getValue(Field field, Object entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}