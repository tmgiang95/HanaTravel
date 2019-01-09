package kr.changhan.mytravels.traveldetail;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import android.view.inputmethod.EditorInfo;
import android.widget.TimePicker;

import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.base.BaseActivity;
import kr.changhan.mytravels.base.MyConst;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.entity.TravelBaseEntity;
import kr.changhan.mytravels.entity.TravelPlan;
import kr.changhan.mytravels.main.TravelListAdapter;
import kr.changhan.mytravels.main.TravelListItemClickListener;
import kr.changhan.mytravels.traveldetail.plan.TravelPlanListAdapter;
import kr.changhan.mytravels.traveldetail.plan.TravelPlanViewModel;
import kr.changhan.mytravels.utils.MyDate;
import kr.changhan.mytravels.utils.MyItemTouchHelper;
import kr.changhan.mytravels.utils.MyString;

public class PlanDetailActivity extends BaseActivity implements View.OnClickListener ,
        MyItemTouchHelper.MyItemTouchHelperListener, TravelListItemClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PlanDetailActivity";

    private final Calendar mCalendar = MyDate.getCurrentCalendar();
    private TextView mDateTxt;
    private TextView mTimeTxt;
    private TextView mPlaceTxt;
    private View mDescLayout;
    private EditText mTitleTxt;
    private EditText mDescTxt;
    private FloatingActionButton mFab;

    private boolean mEditMode;
    private TravelPlanListAdapter mListAdapter;
    private TravelPlanViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDateTxt = findViewById(R.id.date_txt);
        mTimeTxt = findViewById(R.id.time_txt);
        mPlaceTxt = findViewById(R.id.place_txt);
        mDescLayout = findViewById(R.id.desc_layout);
        mDescTxt = findViewById(R.id.desc_txt);
        mTitleTxt = findViewById(R.id.title_txt);
        mFab = findViewById(R.id.fab);

        mDateTxt.setText(MyDate.getDateString(mCalendar.getTime()));
        mTimeTxt.setText(MyDate.getTimeMinString(mCalendar.getTime()));
        mTitleTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                        mDescTxt.requestFocus();
                        return true;
                }
                return false;
            }
        });
        mFab.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(this).get(TravelPlanViewModel.class);
        mViewModel.currentItem.observe(this, new Observer<TravelPlan>() {
            @Override
            public void onChanged(TravelPlan travelPlan) {
                mCalendar.setTimeInMillis(travelPlan.getDateTimeLong());
                mDateTxt.setText(travelPlan.getDateTimeText());
                mTimeTxt.setText(travelPlan.getDateTimeHourMinText());
                mPlaceTxt.setText(travelPlan.getPlaceName());
                mTitleTxt.setText(travelPlan.getTitle());
                mDescTxt.setText(travelPlan.getDesc());
            }
        });

        mListAdapter = new TravelPlanListAdapter(this);
        mListAdapter.setListItemClickListener(this);
        mViewModel.travelPlanList.observe(this, new Observer<PagedList<TravelPlan>>() {
            @Override
            public void onChanged(PagedList<TravelPlan> travelPlans) {
                mListAdapter.submitList(travelPlans);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mListAdapter);
        //attach MyItemTouchHelper
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        TravelPlan requestItem = (TravelPlan) getIntent().getExtras().getSerializable(MyConst.REQKEY_TRAVEL);
        mViewModel.currentItem.setValue(requestItem);
    }

    @Override
    public void onBackPressed() {
        if (mEditMode) {
            setEditMode(false);
            return;
        }
        super.onBackPressed();
    }

    private void setEditMode(boolean editMode) {
        mEditMode = editMode;
        if (mEditMode) {
            mDescLayout.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_done_white_24dp);
        } else {
            mDescLayout.setVisibility(View.GONE);
            mTitleTxt.setText(null);
            mDescTxt.setText(null);
            mPlaceTxt.setText(null);
            mFab.setImageResource(R.drawable.ic_add_black_24dp);
            TravelPlan item = new TravelPlan();
            item.setId(0);
            item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
            item.setDateTime(mViewModel.currentItem.getValue().getDateTime());
            mViewModel.currentItem.setValue(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_txt: {
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
            break;
            case R.id.time_txt: {
                TimePickerDialog tpd = new TimePickerDialog(this, this,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        false);
                tpd.show();
            }
            break;
            case R.id.place_txt:
                showPlacePicker();
                break;
            case R.id.fab: {
                hideKeyBoard();
                if (!mEditMode) {
                    setEditMode(true);
                    return;
                }
                TravelPlan item = mViewModel.currentItem.getValue();
                setValuesFromEditText(item);
                if (MyString.isEmpty(item.getTitle()) || MyString.isEmpty(item.getPlaceName())) {
                    Snackbar.make(v, R.string.plan_edit_warn, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                item.setDateTime(mCalendar.getTimeInMillis());
                mViewModel.insertItem(item);
                setEditMode(false);
            }
            break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mCalendar.set(i,i1,i2);
        TravelPlan item = mViewModel.currentItem.getValue();
        setValuesFromEditText(item);
        item.setDateTime(mCalendar.getTimeInMillis());
        mViewModel.currentItem.setValue(item);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        mCalendar.set(Calendar.HOUR_OF_DAY, i);
        mCalendar.set(Calendar.MINUTE, i1);
        mCalendar.set(Calendar.SECOND, 0);
        TravelPlan item = mViewModel.currentItem.getValue();
        setValuesFromEditText(item);
        item.setDateTime(mCalendar.getTimeInMillis());
        mViewModel.currentItem.setValue(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case MyConst.REQCD_PLACE_PICKER: {
                Place place = PlacePicker.getPlace(this, data);
                Log.d(TAG, "onActivityResult: place=" + place);
                TravelPlan item = mViewModel.currentItem.getValue();
                setValuesFromEditText(item);
                item.setPlaceId(place.getId());
                item.setPlaceName(place.getName().toString());
                item.setPlaceAddr(place.getAddress().toString());
                item.setPlaceLat(place.getLatLng().latitude);
                item.setPlaceLng(place.getLatLng().longitude);
                mViewModel.currentItem.setValue(item);
            }
            break;
        }
    }

    private void setValuesFromEditText(TravelPlan item) {
        item.setTitle(mTitleTxt.getText().toString().trim());
        item.setDesc(mDescTxt.getText().toString().trim());
    }

    @Override
    public void onListItemClick(View view, int position, TravelBaseEntity entity, boolean longClick) {
        TravelPlan item = (TravelPlan) entity;
        Log.d(TAG, "onListItemClick: item=" + item);
        if (!longClick) return;
        mViewModel.currentItem.setValue(item);
        setEditMode(true);
    }

    @Override
    public boolean onRequestItemViewSwipeEnabled() {
        return mEditMode;
    }

    @Override
    public void onItemDimiss(int position) {
        TravelPlan item = mListAdapter.getItem(position);
        item.setDeleteYn(true);
        mViewModel.updateItem(item);
        Snackbar.make(mFab, R.string.delete_warn_msg, Snackbar.LENGTH_INDEFINITE).setAction(R.string.undo,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undeleteAllMarkedYes();
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                switch (event) {
                    case BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_SWIPE:
                    case BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT:
                        deleteAllMarkedYes();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void finish() {
        deleteAllMarkedYes();
        super.finish();
    }

    private void deleteAllMarkedYes() {
        TravelPlan item = new TravelPlan();
        item.setId(-99);
        item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
        mViewModel.deleteItem(item);
    }

    private void undeleteAllMarkedYes() {
        TravelPlan item = new TravelPlan();
        item.setId(-99);
        item.setTravelId(mViewModel.currentItem.getValue().getTravelId());
        mViewModel.updateItem(item);
    }
}
