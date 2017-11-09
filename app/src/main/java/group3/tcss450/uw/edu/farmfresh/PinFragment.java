package group3.tcss450.uw.edu.farmfresh;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private String user;
    private String pass;
    private String name;

    public PinFragment() {
        // Required empty public constructor
        user = "";
        pass = "";
        name = "";
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            user = getArguments().getString(getString(R.string.email_key));
            pass = getArguments().getString(getString(R.string.password_key));
            name = getArguments().getString(getString(R.string.name_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_pin, container, false);

        Button pinSubmitButton = (Button) v.findViewById(R.id.pin_submit_button);
        pinSubmitButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch(view.getId()) {
                case R.id.pin_submit_button:
                    mListener.submitPin(user, pass, name);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void submitPin(String user, String pass, String name);

    }

}
