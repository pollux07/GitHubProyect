package com.daniel.proyectgh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.daniel.proyectgh.Utilities.Common;
import com.daniel.proyectgh.Utilities.RepositoryAdapter;
import com.daniel.proyectgh.Utilities.RepositoryObject;
import com.daniel.proyectgh.Utilities.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Context mCtx;

    List<RepositoryObject> mRepoObjectList = new ArrayList<>();
    RepositoryAdapter mRepoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtx = this;

        List<String> languages = new ArrayList<>();
        languages.add("Seleccione...");
        languages.add("java");
        languages.add("javascript");
        languages.add("php");
        languages.add("ruby");
        languages.add("python");
        languages.add("swift");

        Spinner languagesSp = findViewById(R.id.sp_languages);

        ArrayAdapter<String> Adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);

        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languagesSp.setAdapter(Adapter);

        languagesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = parent.getItemAtPosition(position).toString();
                if (!language.equals("Seleccione...")) {
                    mRepoAdapter.notifyDataSetChanged();
                    searchLanguages(language);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mRepoAdapter = new RepositoryAdapter(mRepoObjectList);

        final RecyclerView recyclerView = findViewById(R.id.recycler_repo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRepoAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mCtx, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RepositoryObject repObject = mRepoObjectList.get(position);
                Intent intentRepo = new Intent(MainActivity.this, RepoInfoActivity.class);
                intentRepo.putExtra("repo_id", repObject.getRepoId());
                intentRepo.putExtra("repo_fullname", repObject.getRepoFullName());
                intentRepo.putExtra("repo_stars", repObject.getRepoStars());
                intentRepo.putExtra("repo_forks", repObject.getRepoForks());
                intentRepo.putExtra("repo_watchers", repObject.getRepoWatchers());
                startActivity(intentRepo);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void searchLanguages(String language) {
        final ProgressDialog dialog = new ProgressDialog(mCtx);
        dialog.setMessage("Cargando");
        dialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("q", language);
        params.put("sort", "stars");
        WebService.searchRepoLanguages(mCtx, params, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("items");
                    mRepoObjectList.clear();
                    mRepoAdapter.notifyDataSetChanged();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        RepositoryObject repo = new RepositoryObject();
                        repo.setRepoId(object.getString("id"))
                                .setRepoName(object.getString("name"))
                                .setRepoFullName(object.getString("full_name"))
                                .setRepoStars(object.getString("stargazers_count"))
                                .setRepoForks(object.getString("forks_count"))
                                .setRepoWatchers(object.getString("watchers"));
                        mRepoObjectList.add(repo);
                    }
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mCtx, "Error de comunicaciones", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            @Override
            public void onError() {
            }
        });
    }

    // Put click listener to recycler view
    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final
        MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
