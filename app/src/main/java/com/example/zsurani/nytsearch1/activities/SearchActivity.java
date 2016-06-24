package com.example.zsurani.nytsearch1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.zsurani.nytsearch1.Article;
import com.example.zsurani.nytsearch1.ArticleAdapter;
import com.example.zsurani.nytsearch1.EndlessScrollListener;
import com.example.zsurani.nytsearch1.FilterActivity;
import com.example.zsurani.nytsearch1.ItemClickSupport;
import com.example.zsurani.nytsearch1.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    // GridView gvResults;
    //Button btnSearch;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    String date;
    String order;
    String n_values;
    String p_query;

    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.gvResults) GridView lvItems;
    @BindView(R.id.gvResults) RecyclerView gvResults;
    @BindView(R.id.btnSearch) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
// Attach the layout manager to the recycler view
        gvResults.setLayoutManager(gridLayoutManager);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //gvResults.setLayoutManager(linearLayoutManager);
        // Add the scroll listener
        gvResults.addOnScrollListener(new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });

        setSupportActionBar(toolbar);
        setupViews();
    }


        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setupFiltersListener();


    public void customLoadMoreDataFromApi(int page) {

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "447a938cd4b5488fa13bc371599396ee");
        params.put("page", page);

        //Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        if (date == null)
        {
        } else if (date.length() > 4) {
            params.put("begin_date", date);
        }

        if (order == null)
        {
        } else if (order.equals("none")) {
        } else if (date.length() > 4) {
            params.put("sort", order);
        }

        if (n_values == null)
        {
        } else if (n_values.length() > 4) {
            params.put("news_desk", n_values);
        }

        // Toast.makeText(this, n_values, Toast.LENGTH_SHORT).show();

        params.put("q", p_query);

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public void setupViews() {
        // gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);
        gvResults.setAdapter(adapter);

        ItemClickSupport.addTo(gvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);
                        i.putExtra("article", Parcels.wrap(article));
                        startActivity(i);
                    }
                }
        );

//        //gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @OnClick(R.id.gvResults)
//            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        //});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "447a938cd4b5488fa13bc371599396ee");
        params.put("page", 0);

        //Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        if (date == null)
        {
        } else if (date.length() > 4) {
            params.put("begin_date", date);
        }

        if (order == null)
        {
        } else if (order.equals("none")) {
        } else if (date.length() > 4) {
            params.put("sort", order);
        }

        if (n_values == null)
        {
        } else if (n_values.length() > 4) {
            params.put("news_desk", n_values);
        }

        // Toast.makeText(this, n_values, Toast.LENGTH_SHORT).show();

        params.put("q", query);
        p_query = query;

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private final int REQUEST_CODE = 20;

//    private void setupFiltersListener() {
//        // Button button = (Button) findViewById(R.id.btnSearch);
//        assert button != null;
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//
//        });
//    }

    @OnClick(R.id.btnSearch)
    public void onButtonClick(View v) {

        Intent i = new Intent(SearchActivity.this, FilterActivity.class);
        startActivityForResult(i, REQUEST_CODE); // brings up the second activity

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            date = data.getExtras().getString("date");

            order = data.getExtras().getString("ordering");

            n_values = data.getExtras().getString("checkboxes");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // added because of search view

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                onArticleSearch(query);

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.menu_search, menu);
        //return true;
    }
}
