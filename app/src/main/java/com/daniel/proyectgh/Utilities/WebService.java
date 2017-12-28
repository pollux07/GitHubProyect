package com.daniel.proyectgh.Utilities;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 27/12/17.
 */

public class WebService {
    private static String URL_DEF = "https://api.github.com";
    private static int DEFAULT_TIME = 120000;

    public interface RequestListener {
        void onSucces(String response);
        void onError();
    }

    public static void searchRepoLanguages(final Context context,
                                       final Map<String, String> params,
                                       final RequestListener requestListener) {
        String url = String.format("%s/search/repositories?%s", URL_DEF, getUrlParams(params));
        StringRequest searchLanguagesAction = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isExpectedJson(response)) {
                    requestListener.onError();
                }

                requestListener.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        searchLanguagesAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(searchLanguagesAction);
    }

    public static void searchRepo(final Context context,
                                  final String repoId,
                                  final RequestListener requestListener) {
        String url = String.format("%s/repositories/%s", URL_DEF, repoId);
        StringRequest searchRepoAction = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isExpectedJson(response)) {
                    requestListener.onError();
                }

                requestListener.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        });

        searchRepoAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(searchRepoAction);
    }

    public static void getContributors (final Context context,
                                        final String fullNameRepo,
                                        final RequestListener requestListener) {
        String url = String.format("%s/repos/%s/contributors", URL_DEF, fullNameRepo);
        StringRequest getContributorsAction = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isExpectedJson(response)) {
                    requestListener.onError();
                }

                requestListener.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        });

        getContributorsAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(getContributorsAction);
    }

    public static void getIssues (final Context context,
                                        final String fullNameRepo,
                                        final RequestListener requestListener) {
        String url = String.format("%s/repos/%s/issues", URL_DEF, fullNameRepo);
        StringRequest getIssuesAction = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!isExpectedJson(response)) {
                    requestListener.onError();
                }

                requestListener.onSucces(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestListener.onError();
            }
        });

        getIssuesAction.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingelton.getInstance(context).addToRequestQueue(getIssuesAction);
    }

    private static String getUrlParams(Map<String, String> params)  {
        List<String> tokens = new ArrayList<>();
        for (String key : params.keySet()) {
            try {
                tokens.add(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return TextUtils.join("&", tokens);
    }

    private static boolean isExpectedJson(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String code = jsonResponse.getString("code");
            if (null == code) {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }

        return true;
    }
}
