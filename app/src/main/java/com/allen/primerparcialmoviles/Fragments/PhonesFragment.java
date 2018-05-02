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

import com.allen.primerparcialmoviles.Adapter.PhoneAdapter;
import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhonesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhonesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTACT_PARAM = "contacts";
    android.support.v7.widget.Toolbar cl;
    View v;

    // TODO: Rename and change types of parameters
    private Contact c;

    private OnFragmentInteractionListener mListener;

    public PhonesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PhonesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhonesFragment newInstance(Contact param1) {
        PhonesFragment fragment = new PhonesFragment();
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
        //Log.d("NUMERO_EN_INFO", "onCreate: "+c.getNumber().size());
        PhoneAdapter pa = new PhoneAdapter(v.getContext(),c.getNumber());
        //Log.d("LISTA_EN_INFO", "onCreate: "+pa.getItemCount());
        rv.setAdapter(pa);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
