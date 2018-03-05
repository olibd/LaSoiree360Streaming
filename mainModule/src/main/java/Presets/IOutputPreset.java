package Presets;

import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;

/**
 * Created by olivier on 16-10-09.
 *
 * This class represent a predefined set of commands that can be added to an FFmpegOutputBuilder
 */
public interface IOutputPreset {
    /**
     * This method adds the preset's commands to the FFmpegOutputBuilder
     * @param commandBuilder
     * @return an FFmpegBuilder
     */
    FFmpegBuilder addPresetsToCommandBuilder(FFmpegOutputBuilder commandBuilder);
}
