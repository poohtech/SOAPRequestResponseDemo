package com.example.soaprequestresponsedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ListActivity extends Activity {

    private RecyclerView listRecyclerView;
    private ArrayList<ListBean> arrayList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listRecyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecyclerView.setLayoutManager(mLayoutManager);

        new GetRegionProcess().execute();
    }

    public class GetRegionProcess extends AsyncTask<Void, Void, Integer> {

        public ProgressDialog mProgressDialog;
        public ArrayList<ListBean> regionArray;

        protected void onPreExecute() {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(ListActivity.this);
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                if (mProgressDialog != null
                        && !((Activity) ListActivity.this).isFinishing())
                    mProgressDialog.show();
            }
        }

        protected Integer doInBackground(Void... arg0) {
            SoapPrimitive result1 = null;// response
            int result = -1; // response
            try {
                SoapObject _request = new SoapObject(Config.NAMESPACE,
                        Config.METHOD_NAME_GET_REGION);

                _request.addProperty("clientid", "lptest");
                _request.addProperty("username", "jla");
                _request.addProperty("password", "jla");

                result1 = openSoapPrimitiveRequest(_request, Config.NAMESPACE +
                        Config.METHOD_NAME_GET_REGION, Config.SERVER_URL);

                result = this.parse(result1);

            } catch (Exception e) {
                System.out.println(this.getClass() + "::doInBackground::" + e);
            }
            return result;
        }

        protected void onPostExecute(Integer result) {
            String title = "GetRegionList";

            Message msg = new Message();
            if (ListActivity.this instanceof Activity && mProgressDialog.isShowing()
                    && mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            msg.what = 101;
            msg.arg1 = result;

            if (result == Config.RESULT_OK) {// successful
                msg.obj = regionArray;
            } else {
                if (ListActivity.this instanceof Activity) {
                    if (result == Config.RESULT_ERROR) {
                        Toast.makeText(ListActivity.this, "Unable to get response from the server" + title, Toast.LENGTH_LONG).show();
                    } else if (result == Config.RESULT_NOT_VALID) {
                        Toast.makeText(ListActivity.this, "Unable to get response from the server" + title, Toast.LENGTH_LONG).show();
                    }
                }
            }

            if (handler != null)
                handler.sendMessage(msg);
        }

        public Integer parse(SoapPrimitive response) {
            int result = Config.RESULT_OK;// response
            try {
                regionArray = new ArrayList<ListBean>();
                ListBean listBean = null;
                if (response == null) {
                    result = Config.RESULT_ERROR;
                } else {
                    System.out.println("Response :: " + response.toString());
                    System.out.println("=========VALUES:::::" + response.toString());

                    JSONArray jArray = new JSONArray(response.toString());
                    JSONObject jObj = new JSONObject();
                    for (int i = 0; i < jArray.length(); i++) {
                        jObj = jArray.getJSONObject(i);
                        listBean = new ListBean();
                        listBean.lat = jObj.getDouble("latitude");
                        listBean.log = jObj.getDouble("longitude");
                        regionArray.add(listBean);
                    }
                }

            } catch (Exception e) {
                System.out.println(this.getClass() + "" + e);
                result = Config.RESULT_NOT_VALID;
                System.out.println(this.getClass() + "::parse::" + e);
            }

            return result;
        }
    }

    public SoapPrimitive openSoapPrimitiveRequest(SoapObject request, String SOAP_ACTION, String SERVER_URL) {
        SoapSerializationEnvelope soapEnvelope = null;
        soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        new MarshalBase64().register(soapEnvelope);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapPrimitive result = null;

        soapEnvelope.setOutputSoapObject(request);

        HttpTransportSE aht = new HttpTransportSE(SERVER_URL,
                Config.HTTP_TIMEOUT);
        aht.debug = Config.LOG;

        try {

            System.out.println("SOAP_ACTION::" + SOAP_ACTION);
            System.out.println("SERVER_URL::" + SERVER_URL);

            aht.call(SOAP_ACTION, soapEnvelope);

            // Utils.writeFile(aht.responseDump,new File(Storage.PHOTO_LIST_DIR
            // + "/" + "v"+System.currentTimeMillis() +".txt"));

            System.out.println("Response" + "" + aht.responseDump);
            System.out.println("Request" + "" + aht.requestDump);

            // Object response= soapEnvelope.getResponse();
            result = (SoapPrimitive) soapEnvelope.getResponse();

            System.out.println("=====================FINAL RESPONSE================");
            System.out.println("-----------Request------------- :: " + "" + aht.requestDump);
            System.out.println("::: RESPONSE ::::" + result);
            System.out.println("::: RESPONSE STRING ::::" + result.toString());


        } catch (UnknownHostException e) {
            if (Config.LOG)
                System.out.println("UnknownHostException" + "" + e);
            // result = "";
            // result .addProperty(Config.TEXT_FAULT,
            // Config.ERROR_UNABLE_TO_CONNECT_SERVER);
            if (Config.LOG)
                System.out.println("Exception in util" + e.getMessage());
            soapEnvelope = null;

        } catch (IOException e) {
            if (Config.LOG)
                System.out.println("IOException" + "" + e);
            // result = "";
            // result .addProperty(Config.TEXT_FAULT, Config.ERROR_IO);
            e.printStackTrace();
            soapEnvelope = null;

        } catch (XmlPullParserException e) {
            if (Config.LOG)
                System.out.println("XmlPullParserException" + "" + e);
            // result = "";
            // result .addProperty(Config.TEXT_FAULT,
            // Config.ERROR_PARSER_EXCEPTION);
            e.printStackTrace();
            soapEnvelope = null;

        } catch (OutOfMemoryError e) {
            if (Config.LOG)
                System.out.println("OutOfMemoryError" + "" + e);
            // result = "";
            // result .addProperty(Config.TEXT_FAULT,
            // Config.ERROR_PARSER_EXCEPTION);
            e.printStackTrace();
            soapEnvelope = null;
            result = null;
        } catch (Exception e) {
            if (Config.LOG)
                System.out.println("Exception" + "" + e);
            // result = "";
            // result .addProperty(Config.TEXT_FAULT, Config.ERROR_EXCEPTION);
            System.out.println("Utils" + e);
            e.printStackTrace();
            soapEnvelope = null;
        } finally {
            soapEnvelope = null;
            request = null;
            SOAP_ACTION = null;
            SERVER_URL = null;
            aht = null;
        }
        return result;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 101) {
                try {
                    System.out.println("............msg.what.........." + msg.what);
                    System.out.println("............msg.obj.........." + msg.obj);
                    if (msg.arg1 == Config.RESULT_OK) {

                        arrayList = new ArrayList<ListBean>();

                        arrayList = (ArrayList<ListBean>) msg.obj;

                        System.out.println("............arrayList.size()........" + arrayList.size());

                        listAdapter = new ListAdapter(arrayList);
                        listRecyclerView.setAdapter(listAdapter);
                    }
                } catch (Exception e) {

                }
            }
        }
    };

    /*Response:
    <?xml version="1.0" encoding="utf-8"?>
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
    <soap:Body>
    <GetRegionResponse xmlns="http://service.demo.com/">
    <GetRegionResult>
      [{"latitude":"23.065837","longitude":"72.493231"},{"latitude":"23.071996","longitude":"72.508541"},{"latitude":"23.055481","longitude":"72.540395"},{"latitude":"22.989126","longitude":"72.528379"},{"latitude":"22.864701","longitude":"72.477224"},{"latitude":"22.736204","longitude":"72.365815"}]
     </GetRegionResult>
     </GetRegionResponse>
     </soap:Body>
    </soap:Envelope>
    */

}