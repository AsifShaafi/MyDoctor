package com.example.shaafi.mydoctor.doctor.listClasses;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorHomePage;
import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;
import com.example.shaafi.mydoctor.mainUi.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity implements View.OnLongClickListener {

    PatientDetailsForDoctorList[] mPatientList;

    public static boolean contextMoodOn = false;
    PatientListAdapter adapter;
    List<PatientDetailsForDoctorList> selectedList = new ArrayList<>();
    int counter = 0;

    @BindView(R.id.patientListRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyList)
    TextView mEmptyListMsg;
    @BindView(R.id.patientListProgressBar)
    ProgressBar mDoctorProgressBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarText)
    TextView mToolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        addSupportActionBar();
        ButterKnife.bind(this);
        getPatientList(getIntent().getStringExtra(DoctorHomePage.PATIENT_LIST));
    }

    @Override
    protected void onStart() {
        super.onStart();
        contextMoodOn = false;
        setSupportActionBar(mToolbar);
        addSupportActionBar();
        mToolbarText.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                //adapter.updateAdapter(selectedList);
                if (contextMoodOn) {
                    adapter.notifyDataSetChanged();
                    cancelContextMode();
                }
                break;
            case android.R.id.home:
                if (contextMoodOn) {
                    cancelContextMode();
                }
                else {
                    finish();
                }
                break;
        }

        return true;
    }

    /*
        refresh the list each tym the activity or list is shown with the data got from the web database
     */
    private void refreshPatientList() {

        if (mPatientList != null) {
            adapter = new PatientListAdapter(this, this, mPatientList);
            mRecyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setHorizontalScrollBarEnabled(true);

            Drawable dividerDrawable = ContextCompat.getDrawable(this,
                    R.drawable.devider);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        } else {
            mEmptyListMsg.setVisibility(View.VISIBLE);
            mEmptyListMsg.setText("No patient found");
        }
    }

    public void addSupportActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                && getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getPatientList(String username) {

        mDoctorProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        //String url = "http://192.168.13.2/my_doctor/getPatientList.php";
        String url = NetworkConnection.mainUrl + "getPatientList.php";
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("submit", "submit")
                .add("username", username)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ListActivity.this, "Operation failed", Toast.LENGTH_SHORT).show();
                        mDoctorProgressBar.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        setPatientListForDoctor(jsonData);

                    } catch (JSONException e) {
                        Log.e("patientList", e.getMessage());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ListActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                            mDoctorProgressBar.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private void setPatientListForDoctor(String jsonData) throws JSONException {
        //Toast.makeText(DoctorHomePage.this, jsonData, Toast.LENGTH_LONG).show();
        //Log.i("Djson", jsonData);
        JSONObject object = new JSONObject(jsonData);
        JSONArray array = object.getJSONArray("patient_list");

        PatientDetailsForDoctorList[] mList = new PatientDetailsForDoctorList[array.length()];

        for (int i = 0; i < array.length(); i++) {

            PatientDetailsForDoctorList p = new PatientDetailsForDoctorList();

            JSONObject jsonObject = array.getJSONObject(i);
            p.setName(jsonObject.getString("patient_name"));
            p.setUserID(jsonObject.getString("patient_username"));
            p.setAge(jsonObject.getString("age"));
            //Log.i("listP", jsonObject.getString("patient_name"));

            mList[i] = p;
        }
        if (mList.length > 0) {
            mPatientList = mList;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mDoctorProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                refreshPatientList();
            }
        });
    }


    /*
        methods related to the context mode
     */
    @Override
    public boolean onLongClick(View view) {
        startContextMode();
        return true;
    }

    /*
        works that will be done when the context mode is on,,that means it updates the counter,
        shows the context action bar and changes the view
     */
    public void contextModeWork(View v, int position) {
        if (((CheckBox) v).isChecked()) {
            selectedList.add(mPatientList[position]);
            counter = counter + 1;
            updateCounter(counter);
        } else {
            selectedList.remove(mPatientList[position]);
            counter = counter - 1;
            updateCounter(counter);
        }
    }

    /*
        updates the counter of the selected items and shows them in the context action bar
     */
    public void updateCounter(int counter) {
        String msg;
        if (counter == 0) {
            msg = " item selected";
        } else {
            msg = " items selected";
        }

        mToolbarText.setText(counter + msg);
    }

    /*
        start the context mode and show context mode view when user long presses any item
     */
    public void startContextMode() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.context_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contextMoodOn = true;
        adapter.notifyDataSetChanged();
        mToolbarText.setVisibility(View.VISIBLE);
        mToolbarText.setText("0 item selected");
    }

    /*
        cancel the context mode and show default view when user press back button or delete any item
     */
    public void cancelContextMode() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.main_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        contextMoodOn = false;
        adapter.notifyDataSetChanged();
        mToolbarText.setVisibility(View.GONE);
        selectedList.clear();
        counter = 0;
    }


    /*
        methods and classes related to the style of the recyclerview
     */

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
