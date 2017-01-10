package com.speedment.runtime.typemapper.longs;

import com.speedment.runtime.config.Column;
import com.speedment.runtime.typemapper.TypeMapper;
import java.lang.reflect.Type;

/**
 * Maps between {@code Long} and {@code short} values by simply casting to
 * lower precision.
 * 
 * @author Emil Forslund
 * @since  3.0.2
 */
public final class PrimitiveLongToShortMapper implements TypeMapper<Long, Short> {

    @Override
    public String getLabel() {
        return "Long to short (primitive)";
    }

    @Override
    public Type getJavaType(Column column) {
        return short.class;
    }

    @Override
    public Short toJavaType(Column column, Class<?> entityType, Long value) {
        return value == null ? null : ((short) (long) value);
    }

    @Override
    public Long toDatabaseType(Short value) {
        return value == null ? null : ((long) (short) value);
    }

}