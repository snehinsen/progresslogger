package ca.tlcp.progresslog;

import java.io.File;

public class ServerUtils {

    public static final String PFP_SAVE_DIRECTORY = "data/usr/PFPs/";

    public static String absolutePFPPath(String pfpLocation) {
        File file = new File(PFP_SAVE_DIRECTORY + pfpLocation);
        return file.getAbsolutePath()
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");
    }

}
