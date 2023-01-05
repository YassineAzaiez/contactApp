package com.example.contactapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.contactapp.models.Contact;

public class MainActivity extends AppCompatActivity implements ContactFragment.OnEditContactListener,ViewContactsFragment.OnAddContactListener {

    public final static int REQUEST_CODE = 1;
    Bundle args = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        ViewContactsFragment fragment = new ViewContactsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Generalized method asking for permissions
     *
     * @param permissions
     */

    public void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

    }

    public boolean checkPermission(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(
                this,
                permission
        );

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("PERmssion", "onRequestPermissionsResult: " + grantResults[i]);
                    } else {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void oneEditContactSelected(Contact contact) {
        EditContactFragment editContactFragment = new EditContactFragment();
        args.putSerializable("ContactInfo",contact);
        Log.d("contactInfo", "oneEditContactSelected: "+contact);
        editContactFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,editContactFragment);
        transaction.addToBackStack("editFragment");
        transaction.commit();
    }

    @Override
    public void onAddContact() {
        AddContactFragment fragment = new AddContactFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.add_contact_fragment));
        transaction.commit();
    }
}
