package com.zebra.kiosk_application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.barcode.Scanner;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, EMDKListener, StatusListener, DataListener{



    private EMDKManager emdkManager = null;


    private static Scanner scanner = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        Button submitRegistrationButton = findViewById(R.id.submitRegistrationButton);

        submitRegistrationButton.setOnClickListener(this);


        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);

        if(results.statusCode != EMDKResults.STATUS_CODE.SUCCESS){
            Log.e("EMDK_ERR", "EMDK Failure");
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(emdkManager != null) {

            emdkManager.release();
            emdkManager = null;

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try{

            if(scanner != null){

                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
                scanner.disable();
                scanner = null;

            }

        }

        catch(ScannerException e){

            e.printStackTrace();

        }


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){


            case R.id.submitRegistrationButton:


                break;


            default:
                break;

        }


    }




    @Override
    public void onOpened(EMDKManager emdkManager) {


        this.emdkManager = emdkManager;


        try{
            initializeScanner();
        }
        catch (ScannerException e) {
            e.printStackTrace();
        }


        if(scanner != null){
            Toast.makeText(RegistrationActivity.this, "Please scan now", Toast.LENGTH_LONG).show();
        }

        else{

            Toast.makeText(RegistrationActivity.this, "Device does not have scanning hardware, please enter information manually", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onClosed() {


        if(this.emdkManager != null){

            this.emdkManager.release();
            this.emdkManager = null;

        }


    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {


        new AsyncDataUpdate().execute(scanDataCollection);


    }

    @Override
    public void onStatus(StatusData statusData) {


        new AsyncStatusUpdate().execute(statusData);

    }


    private void initializeScanner() throws ScannerException {


        if(scanner == null) {

            BarcodeManager barcodeManager = (BarcodeManager) this.emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);

            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);


            if(scanner != null){

                scanner.addDataListener(this);
                scanner.addStatusListener(this);


                scanner.triggerType = Scanner.TriggerType.HARD;


                scanner.enable();


                scanner.read();

            }


        }


    }



    private static class AsyncStatusUpdate extends AsyncTask<StatusData, Void, String> {

        @Override
        protected String doInBackground(StatusData... params) {

            String statusStr = "";

            StatusData statusData = params[0];

            StatusData.ScannerStates state = statusData.getState();


            switch (state) {

                case IDLE:

                    statusStr = "The scanner is enabled and it's idle";
                    break;

                case SCANNING:

                    statusStr = "Scanning...";
                    break;

                case WAITING:

                    statusStr = "Waiting for trigger press...";
                    break;

                case DISABLED:

                    statusStr = "Scanner is not enabled";
                    break;

                default:
                    break;

            }

            return statusStr;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }



    private static class AsyncDataUpdate extends AsyncTask<ScanDataCollection, Void, String> {

        @Override
        protected String doInBackground(ScanDataCollection... params) {

            String statusStr = "";


            try {

                scanner.read();


                ScanDataCollection scanDataCollection = params[0];


                if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {

                    ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection.getScanData();


                    for (ScanDataCollection.ScanData data : scanData) {

                        String barcodeData = data.getData();

                        ScanDataCollection.LabelType labelType = data.getLabelType();

                        statusStr = barcodeData + " " + labelType;

                    }


                }


            } catch (ScannerException e) {
                e.printStackTrace();
            }


            return statusStr;


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }



}
