package com.example.kentons.templateapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TextView fromIntentTextView;
    private TextView userNameTextView;
    private TextView userEmailTextView;


    private Bundle arguments;

    public HomeFragment() {
        // Required empty public constructor
    }

    /*
    public static final HomeFragment newInstance(String from, String user, String email)
    {
        HomeFragment f = new HomeFragment();

        fromIntentTextView = (TextView) getView().findViewById(R.id.from_intent);
        userNameTextView = (TextView) getView().findViewById(R.id.user_name);
        userEmailTextView = (TextView) getView().findViewById(R.id.user_email);

        Bundle mybundle = args;
        String from = (String) mybundle.get("From");

        if(from.equals("Facebook")) {
            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");
            userNameTextView.setText(name);
            userEmailTextView.setText(email);

        }


        if(from.equals("Google")) {

            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");
            userNameTextView.setText(name);
            userEmailTextView.setText(email);
        }



        // Bundle bdl = new Bundle(2);
       // bdl.putInt(EXTRA_TITLE, title);
       // bdl.putString(EXTRA_MESSAGE, message);
       // f.setArguments(bdl);
        return f;
    }
    */



    @Override
    public void setArguments(Bundle args) {
        this.arguments = args;
        //super.setArguments(args);

        //Toast.makeText(getActivity(), "In setArguments for HomeFragment", Toast.LENGTH_SHORT).show();
        /*
        fromIntentTextView = (TextView) getView().findViewById(R.id.from_intent);
        userNameTextView = (TextView) getView().findViewById(R.id.user_name);
        userEmailTextView = (TextView) getView().findViewById(R.id.user_email);

        Bundle mybundle = args;
        String from = (String) mybundle.get("From");

        if (from.equals("Facebook")) {
            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");
            userNameTextView.setText(name);
            userEmailTextView.setText(email);

        }


        if (from.equals("Google")) {

            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");
            userNameTextView.setText(name);
            userEmailTextView.setText(email);
        }
        */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        fromIntentTextView = (TextView) rootView.findViewById(R.id.from_intent);
        userNameTextView = (TextView) rootView.findViewById(R.id.user_name);
        userEmailTextView = (TextView) rootView.findViewById(R.id.user_email);

        Bundle mybundle = this.arguments;

        if(mybundle != null)
        {

            //Toast.makeText(getActivity(), "mybundle is not null", Toast.LENGTH_SHORT).show();

            String from = (String) mybundle.get("From");

            fromIntentTextView.setText("Intent from " + from + " button");

            if (from.equals("Facebook")) {
                String name = (String) mybundle.get("Name");
                String email = (String) mybundle.get("Email");
                userNameTextView.setText(name);
                userEmailTextView.setText(email);

            }


            if (from.equals("Google")) {

                String name = (String) mybundle.get("Name");
                String email = (String) mybundle.get("Email");
                userNameTextView.setText(name);
                userEmailTextView.setText(email);
            }

            mybundle = null;  //set it back to null to get google information

        }
        else
        {
            //Toast.makeText(getActivity(), "mybundle is null (bad)", Toast.LENGTH_SHORT).show();

        }


        return rootView;

    }





}

