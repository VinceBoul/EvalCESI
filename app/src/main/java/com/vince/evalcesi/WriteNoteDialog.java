package com.vince.evalcesi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.model.HttpResult;
import com.vince.evalcesi.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vince on 11/11/2016.
 */

public class WriteNoteDialog extends DialogFragment {

    private EditText message;

    public static WriteNoteDialog getInstance(final String token) {
        WriteNoteDialog f = new WriteNoteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("token", token);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_note, null);
        message = (EditText) view.findViewById(R.id.note_text);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        closeKeyboard();
                        if (!message.getText().toString().isEmpty()) {
                            //post message
                            new WriteNoteDialog.SendMessageAsyncTask(view.getContext())
                                    .execute(message.getText().toString());
                        } else {
                            message.setError(WriteNoteDialog.this.getActivity()
                                    .getString(R.string.error_missing_msg));
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        closeKeyboard();
                        WriteNoteDialog.this.dismiss();
                    }
                }

        );

        return builder.create();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
    }

    /**
     * AsyncTask for sign-in
     */
    protected class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {

        Context context;

        public SendMessageAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                Map<String, String> p = new HashMap<>();
                p.put("note", params[0]);
                HttpResult result = NetworkHelper.doPost(context.getString(R.string.url_notes), p, getArguments().getString("token"));

                return result.code;
            } catch (Exception e) {
                Log.d(Constants.TAG, "Error occured in your AsyncTask : ", e);
                return 500;
            }
        }

        @Override
        public void onPostExecute(Integer status) {
            if (status != 200) {
                Toast.makeText(context, context.getString(R.string.error_send_msg), Toast.LENGTH_SHORT).show();
            }else {
                WriteNoteDialog.this.dismiss();
            }
        }
    }
}
