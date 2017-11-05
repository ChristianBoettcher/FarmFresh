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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private ProgressBar loginProgressBar;

    private Button login;

    private Button register;

    private Button forgot;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            String user = getArguments().getString(getString(R.string.email_key));
            EditText email = (EditText) getActivity().findViewById(R.id.login_email);
            email.setText(user);
            Toast.makeText(getActivity(),
                    "You have successfully registered. ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        login = (Button) v.findViewById(R.id.login_button);
        register = (Button) v.findViewById(R.id.register_button);
        forgot = (Button) v.findViewById(R.id.forgot_button);
        loginProgressBar = (ProgressBar) v.findViewById(R.id.login_progress);

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
                    mListener.goRegister();
                    break;
                case R.id.login_button:
                    /*
                    User has logged in or typed incorrect email/password.
                     */
                    Button[] LFR = {login, forgot, register};
                    mListener.loginManager(loginProgressBar, LFR);
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


    public interface  OnFragmentInteractionListener {
        void goRegister();

        void loginManager(ProgressBar theLoginProgressBar, Button[] theLFR);

    }

}
