package com.example.pc.budderflypilot.fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc.budderflypilot.MainActivity;
import com.example.pc.budderflypilot.R;
import com.example.pc.budderflypilot.database.models.Device;
import com.example.pc.budderflypilot.database.repositories.DeviceRepository;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class ControlDevicesFragment extends Fragment {

    public static String TAG = ManageDevicesFragment.class.getSimpleName();

    private MainActivity mainActivity;
    private View mView;
    public static String MQTT_BROKER_URL = "tcp://172.17.0.247:1883";
    public static String CLIENT_ID = "12345";
    private static final String DEVICE_ID = "DEVICE_ID";
    private MqttAndroidClient mqttAndroidClient;
    private Device device;

    public ControlDevicesFragment() {
        // Required empty public constructor
    }

    public static ControlDevicesFragment newInstance(int param1) {
        ControlDevicesFragment fragment = new ControlDevicesFragment();
        Bundle args = new Bundle();
        args.putInt(DEVICE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        MainActivity.pageIndex = 2;
        if (getArguments() != null) {
            device = (new DeviceRepository((mainActivity))).find(getArguments().getInt(DEVICE_ID));
            Log.d(TAG, "onCreate: **********************************" + device.getMacAddress());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null)
            mView = inflater.inflate(R.layout.fragment_control_devices, container, false);
        return mView;
    }

    public void initMQTT() {
        try {
            mqttAndroidClient = getMqttClient(
                    mainActivity, MQTT_BROKER_URL, CLIENT_ID);

            if (mqttAndroidClient != null) {
                mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean b, String s) {
//                        try {
//                            subscribe(mqttAndroidClient, "#", 1);
//                        } catch (Exception e) {
//                            Log.e(TAG, "connectComplete: ", e);
//                        }
                    }

                    @Override
                    public void connectionLost(Throwable throwable) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//                        String data = "";
//                        for(int i =0; i < mqttMessage.getPayload().length; i++){
//                            data += "--" + mqttMessage.getPayload()[i];
//                        }
//                        Log.d(TAG, "messageArrived: ");
//                        Log.d(TAG, "messageArrived: " + topic + "---" + data);
//                        if(s.equals("60:01:94:69:4d:d5")){
                        if (mqttMessage != null) {
                            if (mqttMessage.getPayload().length == 1) {
                                if (mqttMessage.getPayload()[0] == 33) {
                                    Log.d(TAG, "messageArrived: " + "turn on command");
                                } else if (mqttMessage.getPayload()[0] == 32) {
                                    Log.d(TAG, "messageArrived: " + "turn off command");
                                } else if (mqttMessage.getPayload()[0] == 16) {
                                    Log.d(TAG, "messageArrived: " + "get measure request");
                                }
                            } else if (mqttMessage.getPayload().length == 2) {
                                if (mqttMessage.getPayload()[0] == 33 && mqttMessage.getPayload()[1] == 1) {
                                    Log.d(TAG, "messageArrived: " + "turn on reply");
                                } else if (mqttMessage.getPayload()[0] == 32 && mqttMessage.getPayload()[1] == 1) {
                                    Log.d(TAG, "messageArrived: " + "turn off reply");
                                }
                            } else if (mqttMessage.getPayload().length > 2) {
                                if (mqttMessage.getPayload()[0] == 16) {
                                    Log.d(TAG, "messageArrived: " + "get measure reply");
                                }
                            }
                        }
                    }

//                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }

                });
            }

        } catch (Exception e) {
            Log.e(TAG, "initMQTT: ", e);
        }
    }

    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setWill("Last_will", "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("username");
        //mqttConnectOptions.setPassword("password".toCharArray());
        return mqttConnectOptions;
    }

    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }


    public MqttAndroidClient getMqttClient(Context context, String brokerUrl, String clientId) {
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try {
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "Success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failure " + exception.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return mqttAndroidClient;
    }

    public void publishMessage(@NonNull MqttAndroidClient client,
                               @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload = new byte[0];
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(5866);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void disconnect(@NonNull MqttAndroidClient client)
            throws MqttException {
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }

    public void subscribe(@NonNull MqttAndroidClient client,
                          @NonNull final String topic, int qos) throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);
        token.setActionCallback(new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Subscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "Subscribe Failed " + topic);
            }
        });
    }

    public void unSubscribe(@NonNull MqttAndroidClient client,
                            @NonNull final String topic) throws MqttException {

        IMqttToken token = client.unsubscribe(topic);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "UnSubscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e(TAG, "UnSubscribe Failed " + topic);
            }
        });
    }
}
