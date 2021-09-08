package com.taidii.diibot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public final class JsonUtils {
    private static final Gson gson = new Gson();

    private JsonUtils() {
    }

    public final static <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public final static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public final static <T> T fromJson(JsonElement json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public final static <T> T fromJson(JsonElement json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public final static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
