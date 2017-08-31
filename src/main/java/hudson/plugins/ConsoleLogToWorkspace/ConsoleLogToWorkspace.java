package hudson.plugins.ConsoleLogToWorkspace;

import hudson.FilePath;
import hudson.console.AnnotatedLargeText;
import hudson.model.TaskListener;
import hudson.model.Result;
import hudson.model.Run;
import java.io.IOException;
import java.io.OutputStream;
import hudson.EnvVars;

public class ConsoleLogToWorkspace {

    public static boolean perform(Run<?, ?> build, FilePath workspace, TaskListener listener,
            boolean writeConsoleLog, String fileName, boolean blockOnAllOutput) {
        final OutputStream os;
        final EnvVars env = build.getEnvironment(listener);
        fileName=env.expand(fileName);

        try {
            if (writeConsoleLog) {
                log("Writing console log to workspace file " + fileName + " started", listener);
                os = workspace.child(fileName).write();
                writeLogFile(build.getLogText(), os, blockOnAllOutput);
                os.close();
                log("Wrote console log to workspace file " + fileName + " successfully", listener);
            }
        } catch (IOException|InterruptedException e) {
            build.setResult(Result.UNSTABLE);
            log("Writing console log to workspace file " + fileName + " failed", listener);
        }
        return true;
    }

    private static void log(String message, TaskListener listener) {
        listener.getLogger().println("[ConsoleLogToWorkspace] " + message);
    }

    private static void writeLogFile(AnnotatedLargeText logText, OutputStream out,
            boolean block) throws IOException, InterruptedException {
        long pos = 0;
        long prevPos = pos;
        do {
            prevPos = pos;
            pos = logText.writeLogTo(pos, out);
            // Nothing new has been written or not blocking
            if (prevPos >= pos || !block) {
                break;
            }
            Thread.sleep(1000);
        } while(true);
    }

}
