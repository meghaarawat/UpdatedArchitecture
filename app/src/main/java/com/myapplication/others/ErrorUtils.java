package com.myapplication.others;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ErrorUtils {

    private static final String ERRORS = "errors";
    private static final String ERROR = "error";

    public static void parseError(Object object) {
        if (object instanceof JSONObject) {

            parseJson((JsonObject) object);

        } else if (object instanceof JsonObject) {

            parseJSON((JSONObject) object);

        } else if (object instanceof String) {
            try {
                JSONObject json = new JSONObject(String.valueOf(object));
                getError(json);
            } catch (JSONException e) {
                //  Toaster.INSTANCE.somethingWentWrong();
            }
        } else {
            Log.e("TAG", "ON ERROR");
            // Toaster.INSTANCE.somethingWentWrong();
        }
    }

    private static void parseJSON(JSONObject object) {
        try {
            getError(object);
        } catch (JSONException e) {
            Toaster.INSTANCE.somethingWentWrong();
        }
    }

    private static void getError(JSONObject object) throws JSONException {
        if (object.has(ERRORS)) {
            if (object.optJSONObject(ERRORS) instanceof JSONObject) {
                getDynamicError(object.optJSONObject(ERRORS));
                return;
            }
        }
        if (object.has("message")) {
            getDynamicError(object.optJSONObject("message"));
            return;

        }
        Object obj = object.get(ERROR);


        if (obj instanceof JSONObject) {

            getDynamicError((JSONObject) obj);

        } else if (obj instanceof String) {
            Toaster.INSTANCE.shortToast(obj.toString());
        } else if (obj instanceof JSONArray) {

        }
    }

    private static void getDynamicError(JSONObject obj) {
        JSONObject errorJs = obj;
        for (Iterator<String> it = errorJs.keys(); it.hasNext(); ) {
            Object ob = errorJs.opt(it.next());
            if (ob instanceof String[]) {
                String[] err = (String[]) ob;
                Toaster.INSTANCE.shortToast(err[0]);
                break;
            } else if (ob instanceof JSONArray) {
                if (parseJSONArray((JSONArray) ob)) break;
            }

        }
    }

    private static boolean parseJSONArray(JSONArray ob) {
        try {
            String error = (String) ob.get(0);
            Toaster.INSTANCE.shortToast(error);
            return true;
        } catch (JSONException e) {
            Toaster.INSTANCE.somethingWentWrong();
        }
        return false;
    }

    private static void parseJson(JsonObject object) {
        if (!object.has(ERROR)) {
            Toaster.INSTANCE.somethingWentWrong();
            return;
        }
        if (object.get("error").isJsonObject()) {
            String jsonString = object.get(ERROR).getAsJsonObject().getAsString();
            try {
                JSONObject js = new JSONObject(jsonString);
                getError(js);
            } catch (JSONException e) {
                Toaster.INSTANCE.somethingWentWrong();
            }
        }
    }

    public static void onFailure(@NotNull Throwable t) {
        if (TextUtils.isEmpty(t.getMessage())) {
            Toaster.INSTANCE.somethingWentWrong();
        } else {
            Toaster.INSTANCE.shortToast(t.getMessage());
        }
    }
}
