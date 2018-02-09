package com.example.pc.budderflypilot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    public static String MQTT_BROKER_URL = "tcp://172.17.0.247:1883";
    public static String CLIENT_ID = "12345";

    private MqttAndroidClient mqttAndroidClient;
    private Button btnSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMQTT();
    }

    public void initMQTT() {
        try {
            mqttAndroidClient = getMqttClient(
                    getApplicationContext(), MQTT_BROKER_URL, CLIENT_ID);

            if (mqttAndroidClient != null) {
                mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean b, String s) {
                        try {
//                            subscribe(mqttAndroidClient, "#", 1);
                        } catch (Exception e) {
                            Log.e(TAG, "connectComplete: ", e);
                        }
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
                                } else if (mqttMessage.getPayload()[0] == 16) {
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


            findViewById(R.id.btnSubscribe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (mqttAndroidClient.isConnected()) {
                            String macAddress = ((EditText) findViewById(R.id.etMacAddress)).getText() != null ? ((EditText) findViewById(R.id.etMacAddress)).getText().toString() : "";
                            if (macAddress.equals("#")) {
                                subscribe(mqttAndroidClient, macAddress, 1);
                            } else if (!macAddress.equals("")) {
                                subscribe(mqttAndroidClient, macAddress + "/RX", 1);
                                subscribe(mqttAndroidClient, macAddress + "/TX", 1);
                            }
                            if (!macAddress.equals(""))
                                subscribe(mqttAndroidClient, macAddress, 1);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "onClick: ", e);
                    }
                }
            });

            findViewById(R.id.btnUnSubscribe).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (mqttAndroidClient.isConnected()) {
                            String macAddress = ((EditText) findViewById(R.id.etMacAddress)).getText() != null ? ((EditText) findViewById(R.id.etMacAddress)).getText().toString() : "";
                            if (macAddress.equals("#")) {
                                unSubscribe(mqttAndroidClient, macAddress);
                            } else if (!macAddress.equals("")) {
                                unSubscribe(mqttAndroidClient, macAddress + "/RX");
                                unSubscribe(mqttAndroidClient, macAddress + "/TX");
                            }
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "onClick: ", e);
                    }
                }
            });

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
