package org.droidplanner.android.fragments.actionbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.o3dr.android.client.Drone;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.property.Altitude;
import com.o3dr.services.android.lib.drone.property.Battery;
import com.o3dr.services.android.lib.drone.property.Gps;
import com.o3dr.services.android.lib.drone.property.Home;
import com.o3dr.services.android.lib.drone.property.Signal;
import com.o3dr.services.android.lib.drone.property.State;
import com.o3dr.services.android.lib.gcs.returnToMe.ReturnToMeState;
import com.o3dr.services.android.lib.util.MathUtils;

import org.beyene.sius.unit.length.LengthUnit;
import org.droidplanner.android.R;
import org.droidplanner.android.SocketDataReceiver;
import org.droidplanner.android.activities.interfaces.SocketCallBackListener;
import org.droidplanner.android.constant.code.ConstantCode;
import org.droidplanner.android.dialogs.SelectionListDialog;
import org.droidplanner.android.fragments.SettingsFragment;
import org.droidplanner.android.fragments.helpers.ApiListenerFragment;
import org.droidplanner.android.utils.Utils;
import org.droidplanner.android.utils.prefs.DroidPlannerPrefs;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fredia Huya-Kouadio on 1/14/15.
 */
public class ActionBarTelemFragment extends ApiListenerFragment implements AdapterView.OnItemSelectedListener {

    private final static IntentFilter eventFilter = new IntentFilter();
    private String TAG = "ActionBarTelemFragment borhan";
    private Double voltage;
    private Double current;
    private int level;

    int DroneResponse = 0;

    private int no_sat;
    private int no_fixed;

    private Double alt;

    static {
        eventFilter.addAction(AttributeEvent.BATTERY_UPDATED);
        eventFilter.addAction(AttributeEvent.STATE_CONNECTED);
        eventFilter.addAction(AttributeEvent.STATE_DISCONNECTED);
        eventFilter.addAction(AttributeEvent.GPS_POSITION);
        eventFilter.addAction(AttributeEvent.GPS_COUNT);
        eventFilter.addAction(AttributeEvent.GPS_FIX);
        eventFilter.addAction(AttributeEvent.SIGNAL_UPDATED);
        eventFilter.addAction(AttributeEvent.STATE_VEHICLE_MODE);
        eventFilter.addAction(AttributeEvent.TYPE_UPDATED);
        eventFilter.addAction(AttributeEvent.ALTITUDE_UPDATED);

        eventFilter.addAction(SettingsFragment.ACTION_PREF_HDOP_UPDATE);
        eventFilter.addAction(SettingsFragment.ACTION_PREF_UNIT_SYSTEM_UPDATE);

        eventFilter.addAction(DroidPlannerPrefs.ACTION_PREF_RETURN_TO_ME_UPDATED);
        eventFilter.addAction(AttributeEvent.RETURN_TO_ME_STATE_UPDATE);
        eventFilter.addAction(AttributeEvent.HOME_UPDATED);
    }

    private final BroadcastReceiver eventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity() == null)
                return;

            /*switch (intent.getAction()) {
                case AttributeEvent.BATTERY_UPDATED:
                    updateBatteryTelem();
                    break;

                case AttributeEvent.STATE_CONNECTED:
                    showTelemBar();
                    updateAllTelem();
                    break;

                case AttributeEvent.STATE_DISCONNECTED:
                    hideTelemBar();
                    updateAllTelem();
                    break;

                case DroidPlannerPrefs.ACTION_PREF_RETURN_TO_ME_UPDATED:
                case AttributeEvent.RETURN_TO_ME_STATE_UPDATE:
                case AttributeEvent.GPS_POSITION:
                case AttributeEvent.HOME_UPDATED:
                    updateHomeTelem();
                    break;

                case AttributeEvent.GPS_COUNT:
                case AttributeEvent.GPS_FIX:
                    updateGpsTelem();
                    break;

                case AttributeEvent.SIGNAL_UPDATED:
                    updateSignalTelem();
                    break;

                case AttributeEvent.STATE_VEHICLE_MODE:
                case AttributeEvent.TYPE_UPDATED:
                    updateFlightModeTelem();
                    break;

                case SettingsFragment.ACTION_PREF_HDOP_UPDATE:
                    updateGpsTelem();
                    break;

                case SettingsFragment.ACTION_PREF_UNIT_SYSTEM_UPDATE:
                    updateHomeTelem();
                    break;

                case AttributeEvent.ALTITUDE_UPDATED:
                    updateAltitudeTelem();
                    break;

                default:
                    break;
            }*/
        }
    };
    SocketCallBackListener socketCallBackListener = new SocketCallBackListener() {
        @Override
        public void backJsonString(String code, String data) {
            if ((ConstantCode.ACTION_BATTERY).equals(code)) {
                try {
                    final JSONObject targetData = new JSONObject((String) data);
                    voltage = targetData.getDouble(ConstantCode.BATTERY_VOLTAGE);
                    current = targetData.getDouble(ConstantCode.BATTERY_CURRENT);
                    level = targetData.getInt(ConstantCode.BATTERY_LEVEL);
                    final View batteryPopupView = batteryPopup.getContentView();
                    final TextView dischargeView = (TextView) batteryPopupView.findViewById(R.id.bar_power_discharge);
                    final TextView currentView = (TextView) batteryPopupView.findViewById(R.id.bar_power_current);
                    final TextView remainView = (TextView) batteryPopupView.findViewById(R.id.bar_power_remain);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dischargeView.setText(voltage + "");
                            currentView.setText(current + "");
                            remainView.setText(level + "");
                            Log.d(TAG, "voltage " + voltage + " Current: " + current + " Level:" + level);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //location condition << ferdib - al - islam

            else if ((ConstantCode.ACTION_LOCATION).equals(code)) {
                Log.d(TAG, " LOCATION " + data);
                try {
                    final JSONObject loc_alt = new JSONObject((String) data);
                    alt = loc_alt.getDouble(ConstantCode.ALT);


                    altitudeTelem = (TextView) getView().findViewById(R.id.bar_altitude);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            altitudeTelem.setText(alt + "");
                            Log.d(TAG, "Altitude: " + alt);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //GpsInfo condition << ferdib - al - islam
            else if ((ConstantCode.ACTION_GPSINFO).equals(code)) {
                try {
                    final JSONObject targetData3 = new JSONObject((String) data);
                    no_fixed = targetData3.getInt(ConstantCode.GPS_FIXED);
                    no_sat = targetData3.getInt(ConstantCode.SAT_NUM);

                    final View gpsPopupView = gpsPopup.getContentView();
                    final TextView numFixedSat = (TextView) gpsPopupView.findViewById(R.id.bar_gps_hdop_status);
                    final TextView numSatView = (TextView) gpsPopupView.findViewById(R.id.bar_gps_satno);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            numFixedSat.setText(no_fixed + "");
                            numSatView.setText(no_sat + "");

                            Log.d(TAG, "Fixed: " + no_fixed + " No. of Sat.: " + no_sat);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    SocketDataReceiver socketDataReceiver;
    private DroidPlannerPrefs appPrefs;

    private TextView homeTelem;
    private TextView altitudeTelem;
    private PopupWindow altPopup;

    private TextView gpsTelem;
    private PopupWindow gpsPopup;

    private TextView batteryTelem;
    private PopupWindow batteryPopup;
    private PopupWindow flightPopup;

    private TextView signalTelem;
    private PopupWindow signalPopup;

    private TextView flightModeTelem;

    private String emptyString;

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action_bar_telem, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyString = getString(R.string.empty_content);

        final Context context = getActivity().getApplicationContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final int popupWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        final int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        final Drawable popupBg = getResources().getDrawable(android.R.color.transparent);

        homeTelem = (TextView) view.findViewById(R.id.bar_home);
        homeTelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch dialog to allow the user to select between rtl and rtm
                /*
                final SelectionListDialog selectionDialog = SelectionListDialog.newInstance(new ReturnToHomeAdapter(context, getDrone(), appPrefs));
                Utils.showDialog(selectionDialog, getChildFragmentManager(), "Return to home type", true);
                 */


                // ferdib - al - islam << home button listener


            }
        });

        altitudeTelem = (TextView) view.findViewById(R.id.bar_altitude);

        gpsTelem = (TextView) view.findViewById(R.id.bar_gps);
        final View gpsPopupView = inflater.inflate(R.layout.popup_info_gps, (ViewGroup) view, false);
        gpsPopup = new PopupWindow(gpsPopupView, popupWidth, popupHeight, true);
        gpsPopup.setBackgroundDrawable(popupBg);
        gpsTelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsPopup.showAsDropDown(gpsTelem);
            }
        });

        batteryTelem = (TextView) view.findViewById(R.id.bar_battery);
        final View batteryPopupView = inflater.inflate(R.layout.popup_info_power, (ViewGroup) view, false);
        batteryPopup = new PopupWindow(batteryPopupView, popupWidth, popupHeight, true);
        batteryPopup.setBackgroundDrawable(popupBg);
        batteryTelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                batteryPopup.showAsDropDown(batteryTelem);
            }
        });

        signalTelem = (TextView) view.findViewById(R.id.bar_signal);
        final View signalPopupView = inflater.inflate(R.layout.popup_info_signal, (ViewGroup) view, false);
        signalPopup = new PopupWindow(signalPopupView, popupWidth, popupHeight, true);
        signalPopup.setBackgroundDrawable(popupBg);
        signalTelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signalPopup.showAsDropDown(signalTelem);
            }
        });

        flightModeTelem = (TextView) view.findViewById(R.id.bar_flight_mode);
        /*final View flightPopupView = inflater.inflate(R.layout.popup_mode_info, (ViewGroup) view, false);
        flightPopup = new PopupWindow(flightPopupView, popupWidth, popupHeight, true);
        flightPopup.setBackgroundDrawable(popupBg);*/

        flightModeTelem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch dialog to allow the user to select vehicle modes
                /*
                final Drone drone = getDrone();

                final SelectionListDialog selectionDialog = SelectionListDialog.newInstance(new FlightModeAdapter(context, drone));
                Utils.showDialog(selectionDialog, getChildFragmentManager(), "Flight modes selection", true);
                 */



                flightPopup.showAsDropDown(flightModeTelem);


            }
        });


        // ferdib - al - islam >>  flight mode spinner code here

        spinner = (Spinner) getView().findViewById(R.id.spin1);
        List<String> modes = new ArrayList<String>();

        modes.add("");
        modes.add("STABILIZE");
        modes.add("LAND");
        modes.add("OF_LOITER");
        modes.add("RTL");
        modes.add("DRIFT");
        modes.add("FLIP");
        modes.add("AUTOTUNE");
        modes.add("BRAKE");
        modes.add("GUIDED_NOGPS");
        modes.add("AVOID_ADSB");
        modes.add("POSITION");
        modes.add("SPORT");
        modes.add("FLOWHOLD");
        modes.add("POSHOLD");
        modes.add("AUTO");
        modes.add("GUIDED");
        modes.add("ACRO");
        modes.add("SMART_RTL");
        modes.add("ALT_HOLD");
        modes.add("LOITER");
        modes.add("CIRCLE");
        modes.add("THROW");



        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, modes);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        appPrefs = DroidPlannerPrefs.getInstance(context);
        socketDataReceiver = new SocketDataReceiver(getActivity(), getContext(), socketCallBackListener);
    }

    private void showTelemBar() {
        final View view = getView();
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

    private void hideTelemBar() {
        final View view = getView();
        if (view != null)
            view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        hideTelemBar();
        //showTelemBar();
        super.onStart();
    }

    @Override
    public void onApiConnected() {
        final Drone drone = getDrone();
        if (drone.isConnected())
            showTelemBar();
        else
            hideTelemBar();

        updateAllTelem();
        getBroadcastManager().registerReceiver(eventReceiver, eventFilter);
    }

    @Override
    public void onApiDisconnected() {
        getBroadcastManager().unregisterReceiver(eventReceiver);
    }

    private void updateAllTelem() {
        /*updateFlightModeTelem();
        updateSignalTelem();
        updateGpsTelem();
        updateHomeTelem();
        updateBatteryTelem();
        updateAltitudeTelem();*/
    }

    private void updateFlightModeTelem() {
        final Drone drone = getDrone();

        final boolean isDroneConnected = drone.isConnected();
        final State droneState = drone.getAttribute(AttributeType.STATE);
        if (isDroneConnected) {
            flightModeTelem.setText(droneState.getVehicleMode().getLabel());
            flightModeTelem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_navigation_light_blue_a400_18dp, 0, 0, 0);
        } else {
            flightModeTelem.setText(emptyString);
            flightModeTelem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_navigation_grey_700_18dp, 0, 0, 0);
        }
    }

    private void updateSignalTelem() {
        final Drone drone = getDrone();

        final View popupView = signalPopup.getContentView();
        TextView rssiView = (TextView) popupView.findViewById(R.id.bar_signal_rssi);
        TextView remRssiView = (TextView) popupView.findViewById(R.id.bar_signal_remrssi);
        TextView noiseView = (TextView) popupView.findViewById(R.id.bar_signal_noise);
        TextView remNoiseView = (TextView) popupView.findViewById(R.id.bar_signal_remnoise);
        TextView fadeView = (TextView) popupView.findViewById(R.id.bar_signal_fade);
        TextView remFadeView = (TextView) popupView.findViewById(R.id.bar_signal_remfade);

        final Signal droneSignal = drone.getAttribute(AttributeType.SIGNAL);
        if (!drone.isConnected() || !droneSignal.isValid()) {
            signalTelem.setText(emptyString);
            signalTelem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_signal_cellular_null_grey_700_18dp,
                    0, 0, 0);

            rssiView.setText("RSSI: " + emptyString);
            remRssiView.setText("RemRSSI: " + emptyString);
            noiseView.setText("Noise: " + emptyString);
            remNoiseView.setText("RemNoise: " + emptyString);
            fadeView.setText("Fade: " + emptyString);
            remFadeView.setText("RemFade: " + emptyString);
        } else {
            final int signalStrength = (int) droneSignal.getSignalStrength();
            final int signalIcon;
            if (signalStrength >= 100)
                signalIcon = R.drawable.ic_signal_cellular_4_bar_grey_700_18dp;
            else if (signalStrength >= 75)
                signalIcon = R.drawable.ic_signal_cellular_3_bar_grey_700_18dp;
            else if (signalStrength >= 50)
                signalIcon = R.drawable.ic_signal_cellular_2_bar_grey_700_18dp;
            else if (signalStrength >= 25)
                signalIcon = R.drawable.ic_signal_cellular_1_bar_grey_700_18dp;
            else
                signalIcon = R.drawable.ic_signal_cellular_0_bar_grey_700_18dp;

            signalTelem.setText(String.format(Locale.ENGLISH, "%d%%", signalStrength));
            signalTelem.setCompoundDrawablesWithIntrinsicBounds(signalIcon, 0, 0, 0);

            rssiView.setText(String.format("RSSI %2.0f dB", droneSignal.getRssi()));
            remRssiView.setText(String.format("RemRSSI %2.0f dB", droneSignal.getRemrssi()));
            noiseView.setText(String.format("Noise %2.0f dB", droneSignal.getNoise()));
            remNoiseView.setText(String.format("RemNoise %2.0f dB", droneSignal.getRemnoise()));
            fadeView.setText(String.format("Fade %2.0f dB", droneSignal.getFadeMargin()));
            remFadeView.setText(String.format("RemFade %2.0f dB", droneSignal.getRemFadeMargin()));
        }

        signalPopup.update();
    }

    private void updateGpsTelem() {
        final Drone drone = getDrone();
        final boolean displayHdop = appPrefs.shouldGpsHdopBeDisplayed();

        final View popupView = gpsPopup.getContentView();
        TextView satNoView = (TextView) popupView.findViewById(R.id.bar_gps_satno);
        TextView hdopStatusView = (TextView) popupView.findViewById(R.id.bar_gps_hdop_status);
        hdopStatusView.setVisibility(displayHdop ? View.GONE : View.VISIBLE);

        final String update;
        final int gpsIcon;
        if (!drone.isConnected()) {
            update = (displayHdop ? "hdop: " : "") + emptyString;
            gpsIcon = R.drawable.ic_gps_off_grey_700_18dp;
            satNoView.setText("S: " + emptyString);
            hdopStatusView.setText("hdop: " + emptyString);
        } else {
            Gps droneGps = drone.getAttribute(AttributeType.GPS);
            final String fixStatus = droneGps.getFixStatus();

            if (displayHdop) {
                update = String.format(Locale.ENGLISH, "hdop: %.1f", droneGps.getGpsEph());
            } else {
                update = String.format(Locale.ENGLISH, "%s", fixStatus);
            }

            switch (fixStatus) {
                case Gps.LOCK_3D:
                case Gps.LOCK_3D_DGPS:
                case Gps.LOCK_3D_RTK:
                    gpsIcon = R.drawable.ic_gps_fixed_black_24dp;
                    break;

                case Gps.LOCK_2D:
                case Gps.NO_FIX:
                default:
                    gpsIcon = R.drawable.ic_gps_not_fixed_grey_700_18dp;
                    break;
            }

            satNoView.setText(String.format(Locale.ENGLISH, "S: %d", droneGps.getSatellitesCount()));
            if (appPrefs.shouldGpsHdopBeDisplayed()) {
                hdopStatusView.setText(String.format(Locale.ENGLISH, "%s", fixStatus));
            } else {
                hdopStatusView.setText(String.format(Locale.ENGLISH, "hdop: %.1f", droneGps.getGpsEph()));
            }
        }

        gpsTelem.setText(update);
        gpsTelem.setCompoundDrawablesWithIntrinsicBounds(gpsIcon, 0, 0, 0);
        gpsPopup.update();
    }

    private void updateHomeTelem() {
        final Drone drone = getDrone();

        String update = getString(R.string.empty_content);
        int drawableResId = appPrefs.isReturnToMeEnabled()
                ? R.drawable.ic_person_grey_700_18dp
                : R.drawable.ic_home_grey_700_18dp;

        if (drone.isConnected()) {
            final Gps droneGps = drone.getAttribute(AttributeType.GPS);
            final Home droneHome = drone.getAttribute(AttributeType.HOME);
            if (droneGps.isValid() && droneHome.isValid()) {
                LengthUnit distanceToHome = getLengthUnitProvider().boxBaseValueToTarget
                        (MathUtils.getDistance2D(droneHome.getCoordinate(), droneGps.getPosition()));
                update = String.format("%s", distanceToHome);

                final ReturnToMeState returnToMe = drone.getAttribute(AttributeType.RETURN_TO_ME_STATE);
                switch (returnToMe.getState()) {

                    case ReturnToMeState.STATE_UPDATING_HOME:
                        //Change the home telemetry icon
                        drawableResId = R.drawable.ic_person_blue_a400_18dp;
                        break;

                    case ReturnToMeState.STATE_USER_LOCATION_INACCURATE:
                    case ReturnToMeState.STATE_USER_LOCATION_UNAVAILABLE:
                    case ReturnToMeState.STATE_WAITING_FOR_VEHICLE_GPS:
                    case ReturnToMeState.STATE_ERROR_UPDATING_HOME:
                        drawableResId = R.drawable.ic_person_red_500_18dp;
                        update = getString(R.string.empty_content);
                        break;
                }
            }
        }

        homeTelem.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
        homeTelem.setText(update);
    }

    private void updateBatteryTelem() {
        final Drone drone = getDrone();

        final View batteryPopupView = batteryPopup.getContentView();
        final TextView dischargeView = (TextView) batteryPopupView.findViewById(R.id.bar_power_discharge);
        final TextView currentView = (TextView) batteryPopupView.findViewById(R.id.bar_power_current);
        final TextView remainView = (TextView) batteryPopupView.findViewById(R.id.bar_power_remain);

        String update;
        Battery droneBattery;
        final int batteryIcon;
        if (!drone.isConnected() || ((droneBattery = drone.getAttribute(AttributeType.BATTERY)) == null)) {
            update = emptyString;
            dischargeView.setText("D: " + emptyString);
            currentView.setText("C: " + emptyString);
            remainView.setText("R: " + emptyString);
            batteryIcon = R.drawable.ic_battery_circle_0_24dp;
        } else {
            Double discharge = droneBattery.getBatteryDischarge();
            String dischargeText;
            if (discharge == null) {
                dischargeText = "D: " + emptyString;
            } else {
                dischargeText = "D: " + electricChargeToString(discharge);
            }

            dischargeView.setText(dischargeText);

            final double battRemain = droneBattery.getBatteryRemain();
            remainView.setText(String.format(Locale.ENGLISH, "R: %2.0f %%", battRemain));
            currentView.setText(String.format("C: %2.1f A", droneBattery.getBatteryCurrent()));


            update = String.format(Locale.ENGLISH, "%2.1fV", droneBattery.getBatteryVoltage());

            if (battRemain >= 100) {
                batteryIcon = R.drawable.ic_battery_circle_8_24dp;
            } else if (battRemain >= 87.5) {
                batteryIcon = R.drawable.ic_battery_circle_7_24dp;
            } else if (battRemain >= 75) {
                batteryIcon = R.drawable.ic_battery_circle_6_24dp;
            } else if (battRemain >= 62.5) {
                batteryIcon = R.drawable.ic_battery_circle_5_24dp;
            } else if (battRemain >= 50) {
                batteryIcon = R.drawable.ic_battery_circle_4_24dp;
            } else if (battRemain >= 37.5) {
                batteryIcon = R.drawable.ic_battery_circle_3_24dp;
            } else if (battRemain >= 25) {
                batteryIcon = R.drawable.ic_battery_circle_2_24dp;
            } else if (battRemain >= 12.5) {
                batteryIcon = R.drawable.ic_battery_circle_1_24dp;
            } else {
                batteryIcon = R.drawable.ic_battery_circle_0_24dp;
            }
        }

        batteryPopup.update();
        batteryTelem.setText(update);
        batteryTelem.setCompoundDrawablesWithIntrinsicBounds(batteryIcon, 0, 0, 0);
    }

    private String electricChargeToString(double chargeInmAh) {
        double absCharge = Math.abs(chargeInmAh);
        if (absCharge >= 1000) {
            return String.format(Locale.US, "%2.1f Ah", chargeInmAh / 1000);
        } else {
            return String.format(Locale.ENGLISH, "%2.0f mAh", chargeInmAh);
        }
    }

    private void updateAltitudeTelem() {
        final Drone drone = getDrone();
        final Altitude altitude = drone.getAttribute(AttributeType.ALTITUDE);
        if (altitude != null) {
            double alt = altitude.getAltitude();
            LengthUnit altUnit = getLengthUnitProvider().boxBaseValueToTarget(alt);

            //this.altitudeTelem.setText(altUnit.toString());
            this.altitudeTelem.setText("120");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // spinner item select listener << ferdib - al - islam
        int index = adapterView.getSelectedItemPosition();
        String flightModeJson = "";

        if (index == 1) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"STABILIZE\"}";

        }
        if (index == 2) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"LAND\"}";

        }
        if (index == 3) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"OF_LOITER\"}";

        }
        if (index == 4) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"RTL\"}";

        }
        if (index == 5) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"DRIFT\"}";

        }
        if (index == 6) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"FLIP\"}";

        }
        if (index == 7) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"AUTOTUNE\"}";

        }
        if (index == 8) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"BRAKE\"}";

        }
        if (index == 9) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"GUIDED_NOGPS\"}";

        }
        if (index == 10) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"AVOID_ADSB\"}";

        }
        if (index == 11) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"POSITION\"}";

        }
        if (index == 12) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"SPORT\"}";

        }
        if (index == 13) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"FLOWHOLD\"}";

        }
        if (index == 14) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"POSHOLD\"}";

        }
        if (index == 15) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"AUTO\"}";

        }
        if (index == 16) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"GUIDED\"}";

        }
        if (index == 17) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"ACRO\"}";

        }
        if (index == 18) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"SMART_RTL\"}";

        }
        if (index == 19) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"ALT_HOLD\"}";

        }
        if (index == 20) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"LOITER\"}";

        }
        if (index == 21) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"CIRCLE\"}";

        }
        if (index == 22) {
            flightModeJson = "{\"u\":\"ground\",\"action\":\"mode\",\"data\":\"THROW\"}";

        }

        SocketDataReceiver.attemptSend(flightModeJson);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        // spinner item nothing-select listener
    }
}
