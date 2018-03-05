import Presets.YoutubePreset;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 16-11-06.
 */
public class YoutubePresetTest {
    @Ignore //ignored because feature will be deprecated in favor of preset files
    @Test
    public void addPresetsToCommandBuilder() throws Exception {

        FFmpegOutputBuilder outputCommandBuilder = new FFmpegBuilder()
                .addInput("AVFoundation", "").done()
                .addExtraArgs("-r", "5") //setting the input framerate, temp. fix for the SP360 over USB, needs to be disabled when streaming from an other camera.
                .overrideOutputFiles(true) // Override the output if it exists
                .addOutput("");   // Filename for the destination

        YoutubePreset ytp = new YoutubePreset();
        FFmpegBuilder builder = ytp.addPresetsToCommandBuilder(outputCommandBuilder);
        assertNotNull(builder);
    }

}