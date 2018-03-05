import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;

/**
 * Created by fatin on 2016-10-21.
 */
public class YoutubePreset implements IOutputPreset {
    @Override
    public FFmpegBuilder addPresetsToCommandBuilder(FFmpegOutputBuilder commandBuilder) {
        return commandBuilder
                .addExtraArgs("-rtbufsize", "1500M") // realtime buffer, mainly used for directshow devices
                .addExtraArgs("-bufsize", "14000k") // https://trac.ffmpeg.org/wiki/EncodingForStreamingSites#a-bufsize
                .addExtraArgs("-maxrate", "7000k") // https://trac.ffmpeg.org/wiki/EncodingForStreamingSites#a-maxrate
                .addExtraArgs("-crf", "18")
                .setAudioCodec("aac")
                //.setVideoBitRate(4_500_000)
                //.setAudioSampleRate(441000) currently removed cause throwing an error, ffmpeg says this format is not sypported
                //.setAudioBitRate(1_000_000)
                .setVideoCodec("libx264")
                //.setVideoResolution(1920, 1080)
                //.addExtraArgs("-pix_fmt", "uyvy422")
                .addExtraArgs("-preset", "veryfast") // https://trac.ffmpeg.org/wiki/EncodingForStreamingSites#a-preset
                //.addExtraArgs("-g", "30") // https://trac.ffmpeg.org/wiki/EncodingForStreamingSites#a-g
                .setFormat("flv")
                .done();
    }
}
