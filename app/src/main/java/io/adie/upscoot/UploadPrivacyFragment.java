package io.adie.upscoot;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class UploadPrivacyFragment extends DialogFragment {
    private static final String TAG = UploadPrivacyFragment.class.getSimpleName();
    private UploadPrivacyDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UploadPrivacyDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.privacy_option)
                .setItems(R.array.upload_type_prompt_desc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Resources r = getResources();
                        int i = r.getIntArray(R.array.upload_type_prompt_values)[which];
                        mListener.onPrivacySelected(i == 1);
                    }
                })
                .setNegativeButton(R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface UploadPrivacyDialogListener {
        void onPrivacySelected(boolean hidden);
    }

}

