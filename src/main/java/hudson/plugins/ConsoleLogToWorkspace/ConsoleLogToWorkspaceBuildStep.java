package hudson.plugins.ConsoleLogToWorkspace;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Builder;

import hudson.util.FormValidation;

import jenkins.tasks.SimpleBuildStep;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.annotation.Nonnull;

public class ConsoleLogToWorkspaceBuildStep extends Builder implements SimpleBuildStep {

    private final String fileName;
    private final boolean writeConsoleLog;
    private final boolean blockOnAllOutput;

    @DataBoundConstructor
    public ConsoleLogToWorkspaceBuildStep(String fileName, boolean writeConsoleLog, boolean blockOnAllOutput) {
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

    @Override
    public void perform(@Nonnull Run<?, ?> build, @Nonnull FilePath workspace,
            @Nonnull Launcher launcher, @Nonnull TaskListener listener) throws InterruptedException, IOException {
        ConsoleLogToWorkspace.perform(build, workspace, listener,
            writeConsoleLog, fileName, blockOnAllOutput);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public String getDisplayName() {
            return "Write Console Log to Workspace";
        }

        /**
         * Performs on-the-fly validation of the form field 'fileName'.
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
