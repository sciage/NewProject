package in.voiceme.app.voiceme.userpost;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class ReportAbuseActivity extends BaseActivity {
    private EditText enteredProblems;
    private Button submitProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abuse);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");
        setNavDrawer(new MainNavDrawer(this));

        enteredProblems = (EditText) findViewById(R.id.reportabuse_edittext);
        submitProblem = (Button) findViewById(R.id.submit_report);


    }
}
