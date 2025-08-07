# Console Log to Workspace

This is a Jenkins plug-in that allows you to save the console log to a file in the
build's workspace for further processing.

One of its uses is to allow post-processing tools such as log analyzers that
only reads files to do its work.

It is available as a build-step and as a post-build action.

## Usage

The plugin is not yet in the Jenkins plugin store. Download the .jar file
from the release archive and add it to your Jenkins server manually.

To use it in declarative pipeline:

    steps {
        step([$class: 'ConsoleLogToWorkspaceBuildStep', fileName: './consoleLog.txt', writeConsoleLog: true, blockOnAllOutput: true])
    }

## Development

### Building with Docker (Recommended)

The project includes a Makefile that uses Docker for consistent builds across different environments.
This approach ensures you have the correct Java 21 and Maven 3.9.9 versions required for modern Jenkins plugin development.

Prerequisites:
- Docker installed on your system

Build the plugin:

    make package

The built plugin binary becomes available as `target/console-log-to-workspace.jar`.

Other useful targets:

    make help       # Show all available targets

### Building Locally (Traditional)

If you prefer to build locally without Docker, you'll need:
- Java 21 (OpenJDK recommended)
- Maven 3.9.6 or later

Then run:

    mvn package

or

    mvn install

## Tribute

This plugin is based on the work of (forked from) Clark Boylan's "Jenkins Console Log To Workspace Plugin".
His version is no longer maintained and this version strives at being the "official" version from now on (August 2017).
