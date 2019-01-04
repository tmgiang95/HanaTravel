package kr.changhan.mytravels.traveldetail;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.base.BaseActivity;

public class PlanDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
