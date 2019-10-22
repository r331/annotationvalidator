package dev.ivanov.validator;

import dev.ivanov.validator.annotation.NotEmpty;
import dev.ivanov.validator.rule.NotEmptyRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnotationValidatorTest {
    @Mock
    NotEmptyRule notEmptyRule;

    @BeforeEach
    public void init() {
        when(notEmptyRule.getAnnotationClass()).thenReturn(NotEmpty.class);
    }

    @Test
    public void testRulesCall() {
        Entity entity = new Entity("1", "2");
        AnnotationValidator annotationValidator
                = new AnnotationValidator(singletonList(notEmptyRule));
        annotationValidator.validate(entity);

        verify(notEmptyRule).getAnnotationClass();
        verify(notEmptyRule).check(any(NotEmpty.class), eq("firstString"), eq(entity.firstString));
        verify(notEmptyRule).check(any(NotEmpty.class), eq("second"), eq(entity.second));
    }

    private class Entity {
        @NotEmpty
        private String firstString;

        @NotEmpty
        private String second;

        public Entity(String firstString, String second) {
            this.firstString = firstString;
            this.second = second;
        }
    }
}
