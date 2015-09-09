package com.lib_common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.util.List;

public class GsonUtil {
    private static Gson gson = new GsonBuilder().setDateFormat(DateUtil.DEFAULT_PATTERN).create();

    public static <T> T fromJsonObj(JsonObject obj, Class<T> classOfT) {
        return gson.fromJson(obj.toString(), classOfT);
    }

    public static <T> T fromJsonString(String objStr, Class<T> classOfT) {
        return gson.fromJson(objStr, classOfT);
    }

    public static <T> List<T> fromJsonArr(JsonArray ary, TypeToken<List<T>> typeToken) {
        return gson.fromJson(ary.toString(), typeToken.getType());
    }

    public static <T> List<T> fromJsonArr(JsonArray ary, Class<T> t) {
        return gson.fromJson(ary.toString(), new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> String toJsonString(T t) {
        return gson.toJson(t);
    }
}
