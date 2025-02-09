package io.github.sekelenao.skprofiler.annotation.marker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to enforce that a given field's value is a power of two.
 * Applying this annotation to a field is a way to indicate that the value assigned
 * must adhere to this specific constraint.
 * This annotation is retained in the source code but not included in the compiled
 * bytecode, as it uses the SOURCE retention policy.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface PowerOfTwo {
}
