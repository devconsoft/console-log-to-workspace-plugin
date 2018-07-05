package hudson.plugins.ConsoleLogToWorkspace;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;

import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import javax.servlet.ServletException;


public class ConsoleLogToWorkspacePublisher extends Recorder {

    private final String fileName;
    private final boolean writeConsoleLog;
    private final boolean blockOnAllOutput;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public ConsoleLogToWorkspacePublisher(String fileName, boolean writeConsoleLog, boolean blockOnAllOutput) {
        this.fileName = fileName;
        this.writeConsoleLog = writeConsoleLog;
        this.blockOnAllOutput = blockOnAllOutput;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }


    public String getFileName() {
        return fileName;
    }

    public boolean getWriteConsoleLog() {
        return writeConsoleLog;
    }

    public boolean getBlockOnAllOutput() {
        return blockOnAllOutput;
    }

    /**
     * Actually publish the Console Logs to the workspace.
     */
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        return ConsoleLogToWorkspace.perform(build, build.getWorkspace(), listener,
            writeConsoleLog, fileName, blockOnAllOutput);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }


    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Write Console Log to Workspace";
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         */
        public FormValidation doCheckFileName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set an output file name");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

    }
}
