package com.example.contactapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.models.Contact;
import com.example.contactapp.utils.ContactsListAdapter;
import com.example.contactapp.utils.DataBaseHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;


public class ViewContactsFragment extends Fragment implements ContactsListAdapter.ContactClickListener  {
    private static final String TAG = "ViewContactsFragment";
    private RecyclerView contacts;
    private ContactsListAdapter adpater;
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;
    private int mAppBarState;
    ArrayList<Contact>  contactslist ;
    Contact contact;
    private String tesrImaheUrl = "www.onlinekosten.de/bilder/android_0502w1100_976.png";

//https://

    private AppBarLayout viewContactsBar, searchBar;

  EditText mSearchContacts;
    public interface OnAddContactListener{
      void onAddContact();
    }
    OnAddContactListener mOnAddContact;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactslist = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_viewcontacts, container, false);

        viewContactsBar =  view.findViewById(R.id.viewContactsToolbar);
        searchBar = view.findViewById(R.id.searchToolbar);
        Log.d(TAG, "onCreateView: started.");
        contacts = view.findViewById(R.id.contactsList);
        setupContactList();
        Log.i(TAG, "onCreateView: " + contactslist.size());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Log.i(TAG, "onCreateView: " + getActivity());
        contacts.setLayoutManager(layoutManager);

        Log.i(TAG, "onCreateView: " + adpater);
        contacts.setHasFixedSize(true);
        contacts.setAdapter(adpater);


        setAppBarState(STANDARD_APPBAR);
        mSearchContacts = view.findViewById(R.id.serchContactet);
        mSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchContacts.getText().toString().toLowerCase(Locale.getDefault());
                adpater.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // navigate to add contacts fragment
        FloatingActionButton fab =  view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked fab.");
            mOnAddContact.onAddContact();
        });

        ImageView ivSearchContact = view.findViewById(R.id.ivSearchIcon);
        ivSearchContact.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked search icon.");
            toggleToolBarState();
        });

        ImageView ivBackArrow = view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked back arrow.");
            toggleToolBarState();
        });





        return view;
    }




    private void setupContactList() {

//        contactslist.add(new Contact("yassine", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("yassine", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("yassine", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("khali", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("mohamed", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("foulen", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        contactslist.add(new Contact("yassine", "25564", "mobile", "dsfqdg@fhfh", tesrImaheUrl));
//        adpater = new ContactsListAdapter(getActivity(), contactslist, "https://",this);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        Cursor cursor = dataBaseHelper.getAllContacts();

        while (cursor.moveToNext()){
            contactslist.add(new Contact(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)));

        }
        adpater = new ContactsListAdapter(getActivity(), contactslist,this);
    }


    /**
     * Initiates the appbar state toggle
     */
    private void toggleToolBarState() {
        Log.d(TAG, "toggleToolBarState: toggling AppBarState.");
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBarState(SEARCH_APPBAR);
        } else {
            setAppBarState(STANDARD_APPBAR);
        }
    }

    /**
     * Sets the appbar state for either the search 'mode' or 'standard' mode
     *
     * @param state
     */
    private void setAppBarState(int state) {
        Log.d(TAG, "setAppBarState: changing app bar state to: " + state);

        mAppBarState = state;

        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);

            //hide the keyboard
            View view = getView();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (NullPointerException e) {
                Log.d(TAG, "setAppBarState: NullPointerException: " + e.getMessage());
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //open the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }

    @Override
    public void contactClicked(int position) {
        Log.i(TAG, "contacClicked: the position is " + position+" "+contactslist.get(position).getName());

        ContactFragment contact = new ContactFragment();
        Bundle b = new Bundle();
        b.putSerializable("contact",contactslist.get(position));
        contact.setArguments(b);
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, contact);
        transaction.addToBackStack(getString(R.string.contact_fragment));
        transaction.commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mOnAddContact = (OnAddContactListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );


        }}}
