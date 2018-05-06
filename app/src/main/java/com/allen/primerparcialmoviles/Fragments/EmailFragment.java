package com.allen.primerparcialmoviles.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.primerparcialmoviles.Adapter.MailAdapter;
import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTACT_PARAM = "contacts";
    android.support.v7.widget.Toolbar cl;
    View v;


    private Contact c;

    private OnFragmentInteractionListener mListener;

    public EmailFragment() {
        // Required empty public constructor
    }


    public static EmailFragment newInstance(Contact param1) {
        EmailFragment fragment = new EmailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_PARAM, param1);
      
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_phones, container, false);
        if (getArguments() != null) {
            c = (Contact) getArguments().getSerializable(CONTACT_PARAM);
        }
        RecyclerView rv = v.findViewById(R.id.phones_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(lm);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        //Log.d("NUMERO_EN_INFO", "onCreate: "+c.getNumber().size());
        MailAdapter pa = new MailAdapter(v.getContext(),c.getEmails());
        //Log.d("LISTA_EN_INFO", "onCreate: "+pa.getItemCount());
        rv.setAdapter(pa);
        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
