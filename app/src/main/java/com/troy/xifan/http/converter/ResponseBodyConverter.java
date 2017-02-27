package com.troy.xifan.http.converter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by chenlongfei on 2016/11/20.
 */

public class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final static String JSON_KEY_DATA = "data";
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Type type;

    ResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
        this.gson = gson;
        this.adapter = adapter;
        this.type = type;
    }

    //将返回的数据统一封装成HttpResponseData<T>
    @Override
    public T convert(ResponseBody value) throws IOException {
        String result = value.string();
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        if (result.startsWith("{") || result.startsWith("[")) {
            JsonElement jsonElement = jsonParser.parse(result);
            jsonObject.add(JSON_KEY_DATA, jsonElement);
        } else {
            jsonObject.addProperty(JSON_KEY_DATA, result);
        }
        //Logger.json(jsonObject.toString());
        return gson.fromJson(jsonObject.toString(), type);
    }
}
