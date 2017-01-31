package in.voiceme.app.voiceme.userpost;

import android.os.Bundle;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class EditPost extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");
        setNavDrawer(new MainNavDrawer(this));
    }
}
