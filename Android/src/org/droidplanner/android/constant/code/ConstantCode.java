package org.droidplanner.android.constant.code;

/**
 * Created by Borhan Uddin on 3/29/2018.
 */

public class ConstantCode {

    public static String USER="ground";
    public static String USER_EAGLE_EYE="eagle";
    public static String USERNAME_KEY="u";
    public static String ENABLE_DISABLE_KEY ="enable_disable";
    public static String CONTROL_TYPE ="control_type";
    public static String LAT ="lat";
    public static String LNG ="lng";
    public static String ALT ="alt";
    public static String BATTERY_VOLTAGE ="voltage";
    public static String BATTERY_CURRENT ="current";
    public static String BATTERY_LEVEL ="level";

    //GPS Info
    public static String GPS_FIXED ="fix";
    public static String SAT_NUM ="sat_num";

    //Ultrasonic type
    public static int CONTROL_TYPE_RADAR =1;
    public static int SENSOR_ENABLE=1;
    public static int SENSOR_DISABLE=2;
    // Default PWM Type
    public static int CONTROL_TYPE_ENABLE_PWM =2;
    public static int CONTROL_TYPE_ENABLE_ALT =3;
    public static int CONTROL_TYPE_ENABLE_LOITER =4;

    // Default MENUAL CONTROL
    public static int CONTROL_TYPE_MUNUAL =3;


    public static String VARIABLE_ARRAY_LEFT_OR_RIGHT ="variable_array_left_or_right";
    public static String VARIABLE_ARRAY_PWM ="variable_array_pwm";
    public static String VARIABLE_ARRAY_TIME ="variable_array";
    public static String VARIABLE_ARRAY_KEY ="variable_array";
    public static String VARIABLE_LAT ="lat";
    public static String VARIABLE_LNG ="lng";
    public static String SOCKET_URL ="http://184.72.95.87:3000/";



    // pwm signal enabled
    public static int DRONE_LEFT_RIGHT_CONTROL=2001;
    public static int PWM_ENABLED=1001;
    // pwm signal enabled
    public static int DRONE_LAT_LNG_RESPONSE=4002;
    public static int DRONE_LAT_LNG_REQUEST=4001;
    public static int CONTROL_TYPE_OBSTACLE_DETECT =5001;

    public static String WAYPOINT_DIRECTORY ="waypoint";
    public static String WAYPOINT_DIRECTORY_NAMEE ="Eagle";



    ///######### Ground code start
    public static String ACTION_TYPE ="action";
    public static String ACTION_BATTERY ="battery";
    ////##### Location
    public static String ACTION_LOCATION ="location";
    ////##### GPS_Info
    public static String ACTION_GPSINFO ="gps_info";
    ////###$Guided Mode
    public static String ACTION_MODE_GUIDED = "GUIDED";
    public static String ACTION_ARMED ="arm";
    public static String ACTION_DISARMED ="disarm";
    public static String ACTION_TAKEOFF ="takeoff";
    public static String DATA_STRING ="data";
    public static String MODE_CHANGE ="mode";
    public static String WAY_RES ="wp_res";
    public static String HOME_LOCATION ="home_location";
    public static String IS_ARMED="arm";
    public static String NO_WAYPOINT="no_waypoint";

}
