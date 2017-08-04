package me.linyujie.mydribbbo.dribbble.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by linyujie on 8/1/17.
 */

public class Auth {

    public static final int REQ_CODE = 100;

    // referece id Dribbble assigned for MyDribbbo
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String CLIENT_ID = "c436072577d744c9bce0d6259d62fc59cc2f75138a2c219e130e8cbd251ea4bf";

    private static final String KEY_CLIENT_SECRET = "client_secret";
    private static final String CLIENT_SECRET = "5ed31893098c64269ec8a58e9f7f4daaac29c9814d94d7e2be5cb8f87e5dd242";


    // Url string request for step 1 authorization
    private static final String URI_AUTHORIZE = "https://dribbble.com/oauth/authorize";

    private static final String KEY_REDIRECT_URI = "redirect_uri";
    public static final String REDIRECT_URI = "http://www.mydribbbo.com";

    private static final String KEY_SCOPE = "scope";
    private static final String SCOPE = "public+write";

    // Url string request for step 2 authorization
    private static final String URI_TOKEN = "https://dribbble.com/oauth/token";

    private static final String KEY_CODE = "code";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    // create URL for step 1 authorization
    private static String getAuthorizeUrl() {
        String url = Uri.parse(URI_AUTHORIZE)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .build()
                .toString();

        url += "&" + KEY_REDIRECT_URI + "=" + REDIRECT_URI;
        url += "&" + KEY_SCOPE + "=" + SCOPE;

        return url;
    }

    // create URL for step 2 authorization
    private static String getTokenUrl(String authCode) {
        return Uri.parse(URI_TOKEN)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .appendQueryParameter(KEY_CLIENT_SECRET, CLIENT_SECRET)
                .appendQueryParameter(KEY_CODE, authCode)
                .appendQueryParameter(KEY_REDIRECT_URI, REDIRECT_URI)
                .build()
                .toString();
    }

    // open Authorization acitivity for step 1 authorization
    public static void openAuthActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra(AuthActivity.KEY_URL, getAuthorizeUrl());

        activity.startActivityForResult(intent, REQ_CODE);
    }

    // fetch the access_token from step 2 authorization

    public static String fetchAccessToken(String authCode) throws IOException{

        OkHttpClient client = new OkHttpClient();
        RequestBody postBody = new FormBody.Builder()
                .add(KEY_CLIENT_ID, CLIENT_ID)
                .add(KEY_CLIENT_SECRET, CLIENT_SECRET)
                .add(KEY_CODE, authCode)
                .add(KEY_REDIRECT_URI, REDIRECT_URI)
                .build();
        Request request = new Request.Builder()
                .url(URI_TOKEN)
                .post(postBody)
                .build();
        Response response = client.newCall(request).execute();

        String responseString = response.body().string();

        try {
            JSONObject obj = new JSONObject(responseString);
            return obj.getString(KEY_ACCESS_TOKEN);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }


}
