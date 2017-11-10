package group3.tcss450.uw.edu.farmfresh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChangePassFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private String email;

    public ChangePassFragment() {
        email = "EMPTY_EMAIL";
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            email = getArguments().getString(getString(R.string.email_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_change_pass, container, false);

        Button pinSubmitButton = (Button) v.findViewById(R.id.change_password_button);
        pinSubmitButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch(v.getId()) {
                case R.id.change_password_button:
                    mListener.onChangePass(this.email);
            }
        }
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
        void onChangePass(String email);
    }
}
