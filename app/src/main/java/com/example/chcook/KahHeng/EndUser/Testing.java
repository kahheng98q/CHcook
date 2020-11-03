package com.example.chcook.KahHeng.EndUser;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chcook.Domain.CookingSteps;
import com.example.chcook.KahHeng.EndUser.DA.CookingStepDA;
import com.example.chcook.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Testing extends Fragment implements RecognitionListener {
    private View view = null;
    private ImageView imageView = null;
    private TextView stepDesc = null;

    private TextView returnedError;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private ProgressBar progressBar;
    private Button btnActivate;
    //    private Boolean isActivate=false;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private Switch SwitchVoice = null;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private ArrayList<CookingSteps> tmpCookingSteps = new ArrayList<>();
    private String recipeKey = "";
    private int position = 0;

    public Testing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testing, container, false);
//        returnedText =view.findViewById(R.id.testT);

        progressBar = getActivity().findViewById(R.id.progressBar);
//        btnActivate=view.findViewById(R.id.btnActiveVoice);
        SwitchVoice = view.findViewById(R.id.switchVoice);
        imageView = view.findViewById(R.id.imageViewStepPremium);
        stepDesc = view.findViewById(R.id.txtCookingStepDescPremium);
//        returnedError = findViewById(R.id.errorView1);
        progressBar.setVisibility(View.INVISIBLE);
        CookingStepDA cookingStepDA = new CookingStepDA();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipeKey = bundle.getString("key");
            cookingStepDA.setRecipeKey(recipeKey);
            cookingStepDA.getUploadedCooking(new CookingStepDA.StepsCallback() {
                @Override
                public ArrayList<CookingSteps> onCallback(ArrayList<CookingSteps> cookingSteps) {
                    tmpCookingSteps = cookingSteps;
                    Glide.with(getContext())
                            .asBitmap()
                            .load(tmpCookingSteps.get(0).getImageUrl())
                            .into(imageView);
                    stepDesc.setText(tmpCookingSteps.get(0).getDescription());
                    progressBar.setVisibility(View.GONE);
                    return cookingSteps;

                }
            });
        }


        // start speech recogniser
        SwitchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    isActivate=true;
                    resetSpeechRecognizer();
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminate(true);

                    // check for permission
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }
                    setRecogniserIntent();
                    speech.startListening(recognizerIntent);
                } else {
//                    isActivate=false;
                    stopVoiceRecognition();
                }
            }
        });

        return view;
    }

    private void resetSpeechRecognizer() {

        if (speech != null)
            speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(getActivity()));
        if (SpeechRecognizer.isRecognitionAvailable(getActivity()))
            speech.setRecognitionListener(this);
        else
            getActivity().finish();
    }

    private void setRecogniserIntent() {

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech.startListening(recognizerIntent);
            } else {
                Toast.makeText(getActivity(), "Permission Denied!", Toast
                        .LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }


    @Override
    public void onResume() {
        Log.i(LOG_TAG, "resume");
        super.onResume();
        if (SwitchVoice.isChecked()) {
            resetSpeechRecognizer();
            speech.startListening(recognizerIntent);
        }

    }

    @Override
    public void onPause() {
        Log.i(LOG_TAG, "pause");
        super.onPause();
        if (speech != null) {
            speech.stopListening();
        }
//
    }

    @Override
    public void onStop() {
        Log.i(LOG_TAG, "stop");
        super.onStop();
        if (speech != null) {
            speech.destroy();
        }
    }


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
//        progressBar.setIndeterminate(false);
//        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
//        progressBar.setIndeterminate(true);
        speech.stopListening();
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        Boolean voiceCheck = false;
        for (String result : matches){
            if (result.toUpperCase().equals("NEXT") && position < tmpCookingSteps.size() - 1) {
                position = position + 1;
                Glide.with(getContext())
                        .asBitmap()
                        .load(tmpCookingSteps.get(position).getImageUrl())
                        .into(imageView);
                stepDesc.setText(tmpCookingSteps.get(position).getDescription());
                Toast.makeText(getActivity(), "Next Step", Toast.LENGTH_SHORT).show();
                voiceCheck = true;
            } else if (result.toUpperCase().equals("PREVIOUS") && position > 0) {
                position = position - 1;
                Glide.with(getContext())
                        .asBitmap()
                        .load(tmpCookingSteps.get(position).getImageUrl())
                        .into(imageView);
                stepDesc.setText(tmpCookingSteps.get(position).getDescription());
                Toast.makeText(getActivity(), "Previous Step", Toast.LENGTH_SHORT).show();
                voiceCheck = true;
            } else if (result.toUpperCase().equals("PREVIOUS") && position == 0) {
                Toast.makeText(getActivity(), "This is already first Step", Toast.LENGTH_SHORT).show();
                voiceCheck = true;
            } else if (result.toUpperCase().equals("NEXT") && position == tmpCookingSteps.size() - 1) {
                Toast.makeText(getActivity(), "This is already last Step", Toast.LENGTH_SHORT).show();
                voiceCheck = true;
            }
        }
           if (!voiceCheck){
                Toast.makeText(getActivity(), "Invalid Command", Toast
                        .LENGTH_SHORT).show();
            }
        speech.startListening(recognizerIntent);

    }

    @Override
    public void onError(int errorCode) {
        Log.i("Test", "FAILED ");
        String errorMessage = getErrorText(errorCode);
        Toast.makeText(getActivity(), errorMessage, Toast
                .LENGTH_SHORT).show();
//        Log.i(LOG_TAG, "FAILED " + errorMessage);
//        returnedError.setText(errorMessage);

        // rest voice recogniser
        resetSpeechRecognizer();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        //Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public void stopVoiceRecognition() {

        if (speech != null) {
            speech.destroy();

            speech = null;
        }
    }
}
