package com.example.quackreport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

//import androidx.loader.app.LoaderManager;

public class EarthquackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquack>>{
    public static final String LOG_TAG = EarthquackActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    private static final int EARTHQUACK_LOADER_ID = 1;

    private EarthquackAdapter mAdapter;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquack_activity);


        ListView earthquackListView = (ListView)findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        mAdapter = new EarthquackAdapter(this,new ArrayList<Earthquack>());
        earthquackListView.setAdapter(mAdapter);


        earthquackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquack currentEarthquack = mAdapter.getItem(position);
                Uri earthquackUri = Uri.parse(currentEarthquack.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquackUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            //android.app.LoaderManager loaderManager = getLoaderManager();
            getSupportLoaderManager().initLoader(EARTHQUACK_LOADER_ID,null,this);

        }
        else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }
    @NonNull
    @Override
    public Loader<List<Earthquack>> onCreateLoader(int id, @Nullable Bundle args) {
        return new EarthquackLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquack>> loader, List<Earthquack> earthquackes) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquackes);

        if(earthquackes != null && !earthquackes.isEmpty()){
            //updateUi(earthquackes);
            mAdapter.addAll(earthquackes);
            mEmptyStateTextView.setVisibility(View.GONE);
        }
    }



    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquack>> loader) {
        //mAdapter.clear();
    }


}
