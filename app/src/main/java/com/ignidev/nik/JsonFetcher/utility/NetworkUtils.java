package com.ignidev.nik.JsonFetcher.utility;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ignidev.nik.JsonFetcher.core.Number;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static List<Number> parseJsonToNumbers(String rawJson) {
        List<Number> numbers = new ArrayList<>();
        if (TextUtils.isEmpty(rawJson))
            return numbers;
        try {
            JSONObject obj = new JSONObject(rawJson);
            JSONArray arr = obj.getJSONArray("numbers");
            for (int i = 0; i < arr.length(); i++)
                numbers.add(new Number(arr.getInt(i)));

        } catch (JSONException e) {
            Log.i(TAG, "parseJsonToNumbers: " + e.getMessage());
        }
        return numbers;
    }

    public static void fetchJsonData(String url, @Nullable OnDownloadFinishedCallback callback) {
        try {
            new JsonLoader(callback).execute(url).get(8, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.i(TAG, "fetchJsonData: " + e.getClass());
        }
    }

    private static class JsonLoader extends AsyncTask<String, Void, String> {

        private final OnDownloadFinishedCallback callback;

        JsonLoader(OnDownloadFinishedCallback callback) {
            super();
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callback.onDownloadStarted();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                Log.i(TAG, "doInBackground: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.i(TAG, "doInBackground: " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callback.onDownloadFinished(s);
        }
    }

    public interface OnDownloadFinishedCallback {
        void onDownloadFinished(String rawData);
        void onDownloadStarted();
    }

}
