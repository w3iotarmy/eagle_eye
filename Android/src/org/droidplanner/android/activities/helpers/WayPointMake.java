package org.droidplanner.android.activities.helpers;

import org.droidplanner.android.SocketDataReceiver;
import org.droidplanner.android.utils.file.DirectoryPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Borhan Uddin on 7/3/2018.
 */
public class WayPointMake {
    public static String getString()
    {
        String dir = DirectoryPath.getWaypointsPath() + "/WayPoints.txt";
        File file = new File(dir);
        String readText = null;
        String codedText;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            readText = text.toString();

        }
        catch (IOException e) {

        }
        for (int i = 0; i < readText.length(); i++){
            if(readText.charAt(i) == '\t'){
                readText = readText.replace('\t', ',');
            }
            if(readText.charAt(i) == '\n'){
                readText = readText.replace('\n', '*');
            }
        }

        String code = "{\"u\":\"ground\",\"action\":\"wp\",\"data\":\"%s\"}";
        String result = String.format(code, readText);
        return result;
        /*try {
            SocketDataReceiver.attemptSend(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
