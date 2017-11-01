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
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = (Button) v.findViewById(R.id.login_button);
        Button register = (Button) v.findViewById(R.id.register_button);
        Button forgot = (Button) v.findViewById(R.id.forgot_button);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgot.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.register_button:
                    Log.d("Pressed register button", "pressed Register button");
                    mListener.goRegister();
                    break;
                case R.id.login_button:
                    /*
                    User has logged in or typed incorrect email/password.
                     */
                    break;
                case R.id.forgot_button:
                    /*
                    Go to password reset page.
                     */
                    break;
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
                    + " must implement OnFragmentInterationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface  OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void goRegister();

        void goPin();
    }

}
