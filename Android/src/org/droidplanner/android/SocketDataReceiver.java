package org.droidplanner.android;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import org.droidplanner.android.activities.interfaces.SocketCallBackListener;
import org.droidplanner.android.constant.code.ConstantCode;
import org.droidplanner.android.constant.code.SocketServerCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by Borhan Uddin on 3/29/2018.
 */

public class SocketDataReceiver {
    public static Socket mSocket;

    private Context context;
    private Activity activity;
    private SocketCallBackListener socketCallBackListener;

    private Boolean isConnected = true;
    private static String mUsername="Borhan";
    private boolean isMe=false;
    private String TAG ="borhan SocketDataReceiver";

    TextToSpeech tts;
    private String speech_txt="";
    public SocketDataReceiver(Activity activity, Context context, SocketCallBackListener socketCallBackListener)
    {
        this.context=context;
        this.socketCallBackListener = socketCallBackListener;
        this.activity=activity;
        //ChatApplication app = (ChatApplication) activity.getApplication();
        mSocket = this.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        mSocket.on("chat message", onNewMessage);

        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
    }



    public Socket getSocket() {
        Socket sSocket=null;
        try {
            sSocket = IO.socket(ConstantCode.SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return sSocket;
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null!=mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(activity.getApplicationContext(), R.string.connect, Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                    Toast.makeText(activity.getApplicationContext(),R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                    Toast.makeText(activity.getApplicationContext(),R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data;
                    int control_type;
                    String username;
                    int message;
                    try {
                        data = new JSONObject((String) args[0]);
                        username = data.getString(ConstantCode.USERNAME_KEY);

                        if(username.equals(ConstantCode.USER))
                        {
                            String action_type = data.getString(ConstantCode.ACTION_TYPE);
                        }
                        else if(username.equals(ConstantCode.USER_EAGLE_EYE))
                        {
                            String action_type = data.getString(ConstantCode.ACTION_TYPE);
                            //Log.d(TAG, "its active 1"+data.toString());
                            if(action_type.equals(ConstantCode.ACTION_BATTERY))
                            {
                                socketCallBackListener.backJsonString(ConstantCode.ACTION_BATTERY,data.toString());
                            }
                            if(action_type.equals(ConstantCode.ACTION_LOCATION))
                            {
                                Log.d(TAG, "its active 2"+data.toString());
                                socketCallBackListener.backJsonString(ConstantCode.ACTION_LOCATION,data.toString());
                            }
                            if(action_type.equals(ConstantCode.ACTION_GPSINFO))
                            {
                                Log.d(TAG, "its active 3"+data.toString());
                                socketCallBackListener.backJsonString(ConstantCode.ACTION_GPSINFO,data.toString());
                            }
                            if(action_type.equals(ConstantCode.MODE_CHANGE))
                            {
                                JSONObject mode = new JSONObject((String) data.toString());
                                String modeName = mode.getString(ConstantCode.DATA_STRING);
                                Toast.makeText(context,"Res Change Mode to "+modeName,Toast.LENGTH_LONG).show();
                                speech_txt = " Change Mode to "+modeName;
                                speech_content();
                            }
                            if(action_type.equals(ConstantCode.WAY_RES))
                            {
                                Toast.makeText(context," Waypoint uploaded ",Toast.LENGTH_LONG).show();
                                speech_txt = " Waypoint uploaded to drone";
                                speech_content();
                            }
                            if(action_type.equals(ConstantCode.IS_ARMED))
                            {
                                //Toast.makeText(context," Waypoint uploaded ",Toast.LENGTH_LONG).show();
                                speech_txt = "armed";
                                speech_content();
                            }
                            if(action_type.equals(ConstantCode.ACTION_TAKEOFF))
                            {
                                //Toast.makeText(context," Waypoint uploaded ",Toast.LENGTH_LONG).show();
                                speech_txt = "takeoff ";
                                speech_content();
                            }
                            if(action_type.equals(ConstantCode.NO_WAYPOINT))
                            {
                                //Toast.makeText(context," Waypoint uploaded ",Toast.LENGTH_LONG).show();
                                speech_txt = "No Waypoint upload ";
                                speech_content();
                            }
                            /*control_type = data.getInt(ConstantCode.CONTROL_TYPE);
                            message = data.getInt(ConstantCode.ENABLE_DISABLE_KEY);

                            switch (control_type)
                            {
                                case 1:
                                    Log.d(TAG," RADAR Enable or Disable");
                                    break;
                                case 2:
                                    Log.d(TAG," Other 2");
                                    break;
                                case 3:
                                    Log.d(TAG," Other 3");
                                    break;
                                case SocketServerCode.PWM_ENABLED:
                                    socketCallBackListener.backJsonString(SocketServerCode.PWM_ENABLED+"","");
                                    break;
                                case SocketServerCode.OBSTACLE_ENABLE:
                                    socketCallBackListener.backJsonString(SocketServerCode.OBSTACLE_ENABLE+"","");
                                    break;
                                case SocketServerCode.OBSTACLE_DISABLE:
                                    socketCallBackListener.backJsonString(SocketServerCode.OBSTACLE_DISABLE+"","");
                                    break;
                                case SocketServerCode.DRONE_LEFT_RIGHT_CONTROL_RESPONSE:
                                    socketCallBackListener.backJsonString(SocketServerCode.DRONE_LEFT_RIGHT_CONTROL_RESPONSE+"","");
                                    break;
                                case SocketServerCode.DRONE_GET_LAT_LNG_REQUEST:
                                    Log.d(TAG, data.toString()+" "+ SocketServerCode.DRONE_GET_LAT_LNG_REQUEST);
                                    socketCallBackListener.backJsonString(SocketServerCode.DRONE_GET_LAT_LNG_REQUEST+"","");
                                    break;
                                case SocketServerCode.DRONE_GET_OBJECT_DETECT:
                                    Log.d(TAG, data.toString()+" "+ SocketServerCode.DRONE_GET_OBJECT_DETECT);
                                    socketCallBackListener.backJsonString(SocketServerCode.DRONE_GET_OBJECT_DETECT+"",data.toString());
                                    break;
                                default:
                                    break;
                            }*/
                        }


                    } catch (JSONException e) {
                        Log.d(TAG, "error:  "+e.toString());
                    }
                }
            });
        }
    };
    private void speech_content()
    {
        tts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{

                        ConvertTextToSpeech(speech_txt);
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }
    private void ConvertTextToSpeech(String text) {
        if(text==null||"".equals(text))
        {

            //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }else
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    //addLog(getResources().getString(R.string.message_user_joined, username));
                   // addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    /*addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);*/
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    //addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    ///removeTyping(username);
                }
            });
        }
    };


    public static void attemptSend(String message) {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;
        mSocket.emit("chat message", message);
    }
}
