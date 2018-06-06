package org.ollide.fussifinder.http.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

public final class TextResponseBodyConverter implements Converter<ResponseBody, String> {

    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
