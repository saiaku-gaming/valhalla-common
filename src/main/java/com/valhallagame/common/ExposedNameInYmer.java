package com.valhallagame.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExposedNameInYmer {
	String value();
}
