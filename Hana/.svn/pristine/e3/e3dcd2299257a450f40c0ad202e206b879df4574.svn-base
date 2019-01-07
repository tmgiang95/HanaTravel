package kr.changhan.mytravels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.changhan.mytravels.base.BaseActivity;
import kr.changhan.mytravels.base.MyApplication;
import kr.changhan.mytravels.base.MyConst;
import kr.changhan.mytravels.base.TravelSort;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.main.TravelListAdapter;
import kr.changhan.mytravels.main.TravelViewModel;

public class MainActivity extends BaseActivity implements TravelListAdapter.ListItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TravelViewModel mTravelViewModel;
    private TravelListAdapter mTravelListAdapter;
    private final Observer<List<Travel>> mTravelObserver = new Observer<List<Travel>>() {
        @Override
        public void onChanged(List<Travel> travels) {
            Log.d(TAG, "onChanged: travels.size=" + travels.size());
            mTravelListAdapter.setList(travels);
        }
    };

    @Override
    public void onListItemClick(View view, int position, Travel travel) {
        Log.d(TAG, "onListItemClick: position=" + position);
        Log.d(TAG, "onListItemClick: travel=" + travel);
        // call TravelDetailActivity
        Intent intent = new Intent(MainActivity.this, TravelDetailActivity.class);
        intent.putExtra(MyConst.REQKEY_TRAVEL_ID, travel.getId());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call EditTravelActivity
                Intent intent = new Intent(MainActivity.this
                        , EditTravelActivity.class);
                startActivityForResult(intent, MyConst.REQCD_TRAVEL_ADD);
            }
        });
        mTravelListAdapter = new TravelListAdapter(this);
        mTravelListAdapter.setListItemClickListener(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(mTravelListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTravelViewModel = ViewModelProviders.of(this).get(TravelViewModel.class);
        mTravelViewModel.getAllTravels().observe(this, mTravelObserver);
        mTravelViewModel.setTravelSort(((MyApplication) getApplication()).getTravelSort());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        TravelSort travelSort = ((MyApplication) getApplication()).getTravelSort();
        switch (travelSort) {
            case DEFAULT:
                menu.findItem(R.id.action_sort_default).setChecked(true);
                break;
            case TITLE_ASC:
                menu.findItem(R.id.action_sort_travel_asc).setChecked(true);
                break;
            case TITLE_DESC:
                menu.findItem(R.id.action_sort_travel_desc).setChecked(true);
                break;
            case START_ASC:
                menu.findItem(R.id.action_sort_start_asc).setChecked(true);
                break;
            case START_DESC:
                menu.findItem(R.id.action_sort_start_desc).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_default:
                mTravelViewModel.setTravelSort(TravelSort.DEFAULT);
                item.setChecked(true);
                return true;
            case R.id.action_sort_travel_asc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_ASC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_travel_desc:
                mTravelViewModel.setTravelSort(TravelSort.TITLE_DESC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_start_asc:
                mTravelViewModel.setTravelSort(TravelSort.START_ASC);
                item.setChecked(true);
                return true;
            case R.id.action_sort_start_desc:
                mTravelViewModel.setTravelSort(TravelSort.START_DESC);
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode);
        Log.d(TAG, "onActivityResult: resultCode=" + resultCode);
        Log.d(TAG, "onActivityResult: data=" + data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case MyConst.REQCD_TRAVEL_ADD: {
                Travel travel = (Travel) data.getExtras().getSerializable(MyConst.REQKEY_TRAVEL);
                Log.d(TAG, "onActivityResult: travel=" + travel);
                // insert a new travel
                mTravelViewModel.insert(travel);
            }
            break;
            case MyConst.REQCD_TRAVEL_EDIT: {
                Travel travel = (Travel) data.getExtras().getSerializable(MyConst.REQKEY_TRAVEL);
                Log.d(TAG, "onActivityResult: travel=" + travel);
                if (MyConst.REQACTION_DEL_TRAVEL.equals(data.getAction())) {
                    mTravelViewModel.delete(travel);
                } else {
                    mTravelViewModel.update(travel);
                }
            }
            break;
        }
    }
}
