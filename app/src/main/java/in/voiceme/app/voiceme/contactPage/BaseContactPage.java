package in.voiceme.app.voiceme.contactPage;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.infrastructure.BaseActivity;

/**
 * Created by harish on 1/20/2017.
 */

public abstract class BaseContactPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        if (!application.getAuth().getUser().isPhoneNumber() && !application.getAuth().getUser().isAllContacts()) {
            Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
            startActivity(intent);
            finish();
            return;

        } else {
            startActivity(new Intent(getApplicationContext(), ContactListActivity.class));
        }

            finish();
            return;
        }

}
