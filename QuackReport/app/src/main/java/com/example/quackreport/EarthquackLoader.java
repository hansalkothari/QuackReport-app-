package com.example.quackreport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquackLoader extends AsyncTaskLoader<List<Earthquack>> {

    private static final String LOG_TAG = EarthquackLoader.class.getName();

    private String mUrl;

    public EarthquackLoader(@NonNull Context context , String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquack> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<Earthquack> earthquacks = QueryUtils.fetchEarthquackData(mUrl);
        return earthquacks;
    }
}
