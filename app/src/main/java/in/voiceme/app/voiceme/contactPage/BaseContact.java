package in.voiceme.app.voiceme.contactPage;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;

public class BaseContact extends BaseActivity {
    protected String usedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_contact);

        if (!application.getAuth().getUser().isLoggedIn()) {
            if (MySharedPreferences.getContactSent(preferences) == null) {
                Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(getApplicationContext(), ContactListActivity.class));
            }
            finish();
        }
    }
}
