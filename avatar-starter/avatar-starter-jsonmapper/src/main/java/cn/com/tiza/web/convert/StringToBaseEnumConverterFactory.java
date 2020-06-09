package cn.com.tiza.web.convert;

import cn.com.tiza.domain.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        if (!targetType.isEnum()) {
            throw new UnsupportedOperationException("只支持转换到枚举类型");
        }
        return new StringToBaseEnumConverter(targetType);
    }

    private class StringToBaseEnumConverter<T extends BaseEnum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToBaseEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String s) {
            for (T t : enumType.getEnumConstants()) {
                if (s.equals(t.getName())) {
                    return t;
                }
            }
            return null;
        }
    }
}