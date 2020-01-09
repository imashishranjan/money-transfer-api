package com.transfer.app.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class Utility {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return Utility::toJson;
    }

}
