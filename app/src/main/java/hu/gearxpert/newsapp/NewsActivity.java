package hu.gearxpert.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    public static final String LOG_TAG = NewsActivity.class.getName();

    private String GUARDIAN_REQUEST_URL;
    private List<NewsData> news = new ArrayList<>();
    List<NewsData> savedNewsList = new ArrayList<>();
    private RecyclerView newsListView;
    /** Adapter for the list of news */
    private NewsDataAdapter mAdapter;
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    /**Constant value for the news loader ID. */
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";

        //find the recyclerview with ID list
        newsListView = (RecyclerView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsDataAdapter(this, news);

        // Set layout manager to position the items
        newsListView.setLayoutManager(new LinearLayoutManager(this));

        //add dividers between list items in the recyclerview
        newsListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // Attach the adapter to the recyclerview to populate items
        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);


        ItemClickSupport.addTo(newsListView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // Find the current news that was clicked on

                NewsData currentNews = savedNewsList.get(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getWebUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        //builds the URI according to the query parameters below
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("page-size", "50");
        uriBuilder.appendQueryParameter("q", "google");
        uriBuilder.appendQueryParameter("api-key", "cec15f85-1160-49c5-b214-e41b64e9972e");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> news) {

        //hides the progress bar circle
        View circle = findViewById(R.id.progress_bar);
        circle.setVisibility(View.GONE);

        // Clear the adapter of previous news data
        mAdapter.setNewsInfoList(null);

        // If there is a valid list of {@link NewsData}s, then add them to the adapter's
        // data set.
        if (news != null && !news.isEmpty()) {
            mAdapter.setNewsInfoList(news);
            mAdapter.notifyDataSetChanged();
            savedNewsList = new ArrayList<>(news);

        } else {
            // Set empty state text to display "No news found."
            mEmptyStateTextView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.setNewsInfoList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


