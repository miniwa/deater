package se.miniwa.deater.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;

public class AppArgs {
    @Parameter(names ={"--players-file", "-f"}, required = true, converter = FileConverter.class,
            description = "Path to json file containing player information")
    private File playerFile;

    public File getPlayerFile() {
        return playerFile;
    }
}
