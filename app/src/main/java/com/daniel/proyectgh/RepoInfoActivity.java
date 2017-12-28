package com.daniel.proyectgh;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.proyectgh.Utilities.IssuesAdapter;
import com.daniel.proyectgh.Utilities.IssuesObject;
import com.daniel.proyectgh.Utilities.RepositoryAdapter;
import com.daniel.proyectgh.Utilities.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RepoInfoActivity extends AppCompatActivity {
    Context mCtx;

    TextView mDescriptionRepo;
    TextView mContributorName1;
    TextView mContributorName2;
    TextView mContributorName3;
    TextView mContributions1;
    TextView mContributions2;
    TextView mContributions3;

    List<IssuesObject> mIssueList = new ArrayList<>();
    IssuesAdapter mIssueAdapter;

    String mRepoFullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCtx = this;

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String repoId = extras.getString("repo_id");
        mRepoFullname = extras.getString("repo_fullname");
        String repoStars = extras.getString("repo_stars");
        String repoForks = extras.getString("repo_forks");
        String repoWatchers = extras.getString("repo_watchers");

        TextView starsRepo = findViewById(R.id.tv_stars_count);
        TextView forksRepo = findViewById(R.id.tv_forks_count);
        TextView watchersRepo = findViewById(R.id.tv_watchers_count);

        mContributorName1= findViewById(R.id.tv_first_contrib_name);
        mContributorName2 = findViewById(R.id.tv_second_contrib_name);
        mContributorName3 = findViewById(R.id.tv_third_contrib_name);

        mContributions1 = findViewById(R.id.tv_first_contrib);
        mContributions2 = findViewById(R.id.tv_second_contrib);
        mContributions3 = findViewById(R.id.tv_third_contrib);

        mDescriptionRepo = findViewById(R.id.tv_repo_description);

        searchRepoInfo(repoId);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRepoFullname);
        }

        starsRepo.setText(repoStars);
        forksRepo.setText(repoForks);
        watchersRepo.setText(repoWatchers);

        mIssueAdapter = new IssuesAdapter(mIssueList);

        final RecyclerView recyclerView = findViewById(R.id.recycler_issue);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mIssueAdapter);
    }

    private void searchRepoInfo(final String repoId) {
        final ProgressDialog dialog = new ProgressDialog(mCtx);
        dialog.setMessage("Cargando");
        dialog.show();

        WebService.searchRepo(mCtx, repoId, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String repoDescription = jsonResponse.getString("description");

                    String jsonArray = jsonResponse.getString("owner");
                    JSONObject jsonArrayResponse = new JSONObject(jsonArray);
                    String ownerImageProfile = jsonArrayResponse.getString("avatar_url")
                            .replaceAll("\\\\", "");

                    getContributors(mRepoFullname, ownerImageProfile);

                    mIssueAdapter.notifyDataSetChanged();
                    getIssues(mRepoFullname);

                    mDescriptionRepo.append(" " + repoDescription);

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

    private void getContributors(String mRepoFullname, final String ownerImageProfile) {
        WebService.getContributors(mCtx, mRepoFullname, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    List<String> contributorImageProfileList = new ArrayList<>();
                    List<String> contributorNameList = new ArrayList<>();
                    List<String> contributionsList = new ArrayList<>();

                    for (int i = 0; i < 3; i ++) {
                        JSONObject object = jsonResponse.getJSONObject(i);

                        contributorImageProfileList.add(object.getString("avatar_url")
                                .replaceAll("\\\\", ""));
                        contributionsList.add(object.getString("contributions"));
                        contributorNameList.add(object.getString("login"));
                    }

                    DownloadImageTask downloadImageProfileTask = new DownloadImageTask(
                            (ImageView) findViewById(R.id.iv_image_profile));
                    DownloadImageTask downloadImageContrib1Task = new DownloadImageTask(
                            (ImageView) findViewById(R.id.iv_first_contrib));
                    DownloadImageTask downloadImageContrib2Task = new DownloadImageTask(
                            (ImageView) findViewById(R.id.iv_second_contrib));
                    DownloadImageTask downloadImageContrib3Task = new DownloadImageTask(
                            (ImageView) findViewById(R.id.iv_third_contrib));

                    downloadImageProfileTask.execute(ownerImageProfile);
                    downloadImageContrib1Task.execute(contributorImageProfileList.get(0));
                    downloadImageContrib2Task.execute(contributorImageProfileList.get(1));
                    downloadImageContrib3Task.execute(contributorImageProfileList.get(2));

                    mContributorName1.setText(contributorNameList.get(0));
                    mContributorName2.setText(contributorNameList.get(1));
                    mContributorName3.setText(contributorNameList.get(2));

                    mContributions1.setText(contributionsList.get(0));
                    mContributions2.setText(contributionsList.get(1));
                    mContributions3.setText(contributionsList.get(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void getIssues(String repoFullname) {
        WebService.getIssues(mCtx, repoFullname, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    mIssueList.clear();
                    mIssueAdapter.notifyDataSetChanged();

                    JSONArray jsonResponse = new JSONArray(response);

                    if (jsonResponse.length() > 0) {
                        for (int i = 0; i < 3; i++) {
                            JSONObject object = jsonResponse.getJSONObject(i);

                            IssuesObject issue = new IssuesObject();

                            String issueTitle = object.getString("title");

                            String user = object.getString("user");
                            JSONObject jsonArrayResponse = new JSONObject(user);

                            String createdBy = jsonArrayResponse.getString("login");

                            String milestone = object.getString("milestone");
                            if (!milestone.equals("null")) {
                                JSONObject jsonArrayDatesResponse = new JSONObject(milestone);
                                String createdAt = jsonArrayDatesResponse.getString("created_at");
                                issue.setIssueCreatedBAt(" " + createdAt);
                            }

                            issue.setIssueTitle(issueTitle)
                                    .setIssueCreatedBy(" " + createdBy);

                            mIssueList.add(issue);
                        }
                    } else {
                        Toast.makeText(mCtx, "No hay Issues", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImagecontrib1;
        protected void onPreExecute(){

        }

        DownloadImageTask(ImageView contrib1) {
            this.bmImagecontrib1 = contrib1;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImagecontrib1.setImageBitmap(getCircleBitmap(result));
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
