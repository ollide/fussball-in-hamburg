package org.ollide.fussifinder.http.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.Converter.Factory;

public final class TextConverterFactory extends Factory {

    public static TextConverterFactory create() {
        return new TextConverterFactory();
    }

    private TextConverterFactory() {
        // hidden
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new TextResponseBodyConverter();
    }

}
