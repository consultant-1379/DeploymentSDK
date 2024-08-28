package com.ericsson.component.aia.sdk.DeploymentSDK.kubeinterpreter.helmutil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CmdExecutor {

    private static String OperatingSystem = System.getProperty("os.name").toLowerCase();
    private static String cmdinterpreter;
    private int returnCode;

    private static boolean isWindows() {
        return (OperatingSystem.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (OperatingSystem.indexOf("mac") >= 0);
    }

    private static boolean isUnix() {
        return (OperatingSystem.indexOf("nux") >= 0);
    }
    /*These are for detecting OperatingSystem and executing proper shell as without shell java is executing process directly and output cannot be redirected.
    Due to some weird behave of helm on cmd.exe I changed this to execute on git bash*/
    public static void detectOS() {
        if (isWindows()) {
            cmdinterpreter = "C:\\Program Files\\Git\\bin\\bash";
        } else if (isMac()) {
            cmdinterpreter = "bash";

        } else if (isUnix()) {
            cmdinterpreter = "/bin/sh";
        };
    }
    public Map cmdExec(final String command) throws InterruptedException, IOException {
        final Map<PrintWriter, Integer> printMap = new HashMap<>();
        detectOS();
        final Process process = Runtime.getRuntime().exec(cmdinterpreter);
        new Thread(new BufferedPipeTool(process.getErrorStream(), System.err)).start();
        new Thread(new BufferedPipeTool(process.getInputStream(), System.out)).start();
        final PrintWriter stdin = new PrintWriter(process.getOutputStream());
        stdin.println(command);
        printMap.put(stdin, returnCode);
        stdin.close();
        returnCode = process.waitFor();
        return printMap;
    }
}
