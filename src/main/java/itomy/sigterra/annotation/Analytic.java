package itomy.sigterra.annotation;

import itomy.sigterra.domain.enumeration.EventType;

public @interface Analytic {
    EventType[] type() default {};
}
