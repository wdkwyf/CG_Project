package edu.cg.wuyufei.addForm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.cg.wuyufei.activity.MainActivity;
import edu.cg.wuyufei.gallery.R;
import edu.cg.wuyufei.util.MyApplication;

/**
 * Created by wuyufei on 15/11/17.
 */
public class PageFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "pageFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static Bitmap bitmap;
    private int mPage;
    public static final int GET_FROM_GALLERY = 3;
    private Button button, uploadBtn;
    private View viewFront, viewEnd, viewPre;
    private TextInputLayout inputLayoutName, inputLayoutPhone, inputLayoutEmail, inputLayoutAddress;
    private EditText inputName, inputPhone, inputEmail, inputAddress;
    private ImageView imageView;
    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mPage known:start with 1
        switch (mPage) {
            case 1:         //front face
                viewFront = inflater.inflate(R.layout.fragment_page_front, container, false);
                button = (Button) viewFront.findViewById(R.id.btn_signup);
                button.setOnClickListener(this);
                inputLayoutName = (TextInputLayout) viewFront.findViewById(R.id.input_layout_name);
                inputLayoutPhone = (TextInputLayout) viewFront.findViewById(R.id.input_layout_phone);
                inputLayoutEmail = (TextInputLayout) viewFront.findViewById(R.id.input_layout_email);
                inputLayoutAddress = (TextInputLayout) viewFront.findViewById(R.id.input_layout_address);
                inputName = (EditText) viewFront.findViewById(R.id.input_name);
                inputPhone = (EditText) viewFront.findViewById(R.id.input_phone);
                inputEmail = (EditText) viewFront.findViewById(R.id.input_email);
                inputAddress = (EditText) viewFront.findViewById(R.id.input_address);
                inputName.addTextChangedListener(new MyTextWatcher(inputName));
                inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
                inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
                inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));
                return viewFront;
            case 2:
                viewEnd = inflater.inflate(R.layout.fragment_page_end, container, false);
                uploadBtn = (Button) viewEnd.findViewById(R.id.btn_upload);
                imageView = (ImageView) viewEnd.findViewById(R.id.imageView);
                uploadBtn.setOnClickListener(this);
                return viewEnd;
            case 3:
                viewPre = inflater.inflate(R.layout.fragment_page_preview, container, false);

                return viewPre;


        }
        return null;
    }

    private void submitForm() {
        Context context = MyApplication.getContext();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("name", inputName.getText().toString());
        edit.putString("email", inputEmail.getText().toString());
        edit.putString("phone", inputPhone.getText().toString());
        edit.putString("address", inputAddress.getText().toString());
        edit.commit();

        if (validateName() && validatePhone() && validateAddress() && validateEmail()) {
            Log.d(TAG, inputName.getText().toString());
            return;
        }
        return;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                submitForm();
                break;
            case R.id.btn_upload:
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                MainActivity.init = 0;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;

            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (inputAddress.getText().toString().trim().isEmpty()) {
            inputLayoutAddress.setError(getString(R.string.err_msg_address));
            requestFocus(inputAddress);
            return false;
        } else {
            inputLayoutAddress.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        String phone = inputPhone.getText().toString().trim();

        if (phone.isEmpty() || !isValidPhone(phone)) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }


}
