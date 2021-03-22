package com.example.quackreport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();
    private QueryUtils(){

    }
    public static List<Earthquack> fetchEarthquackData(String requestUrl){
        URL url = createUrl(requestUrl);
        String JsonResponse = null;
        try{
            JsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"problem making the HTTP request",e);
        }
        List<Earthquack> earthquacks = extractFeatures(JsonResponse);
        return earthquacks;
    }
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Problem building the URL",e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException{
        String JsonResponse = "";
        if(url == null){
            return JsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
               inputStream = urlConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG,"Error response code"+urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG,"Problem retrieving the earthquack Json response" , e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return JsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }

        }
        return output.toString();
    }
    private static List<Earthquack> extractFeatures(String earthquackJSON){
        if(TextUtils.isEmpty(earthquackJSON))
        {
            return null;
        }
        List<Earthquack> earthquacks = new ArrayList<>();
        try{
            JSONObject baseJsonResponse = new JSONObject(earthquackJSON);
            JSONArray earthquackArray = baseJsonResponse.getJSONArray("features");
            for(int i = 0 ; i<earthquackArray.length();i++){
                JSONObject currentEarthquack = earthquackArray.getJSONObject(i);
                JSONObject properties = currentEarthquack.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                Earthquack earthquack = new Earthquack(magnitude,location,time,url);
                earthquacks.add(earthquack);
            }
        }catch (JSONException e){
            Log.e("QueryUtils ","problem parsing the earthquack json" , e);

        }
        return earthquacks;
    }


}
