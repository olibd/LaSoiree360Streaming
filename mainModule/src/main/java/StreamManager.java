import Exceptions.OSNotSupportedError;
import Exceptions.CantStartStreamException;
import Exceptions.StreamIONotSetException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

import java.io.*;

/**
 * Created by olivier on 16-10-09.
 */
public class StreamManager extends Thread {

    private static FFmpeg ffmpeg;
    private static FFprobe ffprobe;
    private FFmpegOutputBuilder outputCommandBuilder;
    private FFmpegBuilder ffmpegBuilder;
    private String output;
    private FFmpegJob job;
    private IFFMPEGCompatibleDriver inputAudioDriver;
    private IFFMPEGCompatibleDriver inputVideoDriver;
    private double audioOffset;

    public StreamManager(IFFMPEGCompatibleDriver aDriver, IFFMPEGCompatibleDriver vDriver, String output) throws IOException, OSNotSupportedError {
        this();
        inputAudioDriver = aDriver;
        inputVideoDriver = vDriver;
        this.output = output;
    }

    public StreamManager() throws IOException, OSNotSupportedError {
        instantiateFFMPEG();
    }

    /**
     * Prepares an FFMPEG command for streaming using the instance variable's value for
     * audioInput, videoInput and output.
     *
     * @throws IOException
     */
    public void prepareStream() throws IOException {
        prepareStream(inputAudioDriver, inputVideoDriver, output);
    }


    /**
     * Prepares an FFMPEG command for streaming.
     *
     * @param aDriver
     * @param vDriver
     * @param output
     * @throws IOException
     */
    public void prepareStream(IFFMPEGCompatibleDriver aDriver, IFFMPEGCompatibleDriver vDriver, String output) throws IOException {

        inputAudioDriver = aDriver;
        inputVideoDriver = vDriver;
        this.output = output;


        //set the video input group (driver and device)
        FFmpegBuilder inputBuilder = new FFmpegBuilder()
                .addInput(vDriver.getInputFormat(), vDriver.getFFMPEGVideoInputCommand()).done();


        //set the audio input group (driver and device)
        inputBuilder.addInput(aDriver.getInputFormat(), aDriver.getFFMPEGAudioInputCommand())
                .done();


        outputCommandBuilder = inputBuilder.overrideOutputFiles(true) // Override the output if it exists
                .addOutput(this.output); // Filename for the destination
    }

    /**
     * Adds a predefined set of parameters to the existing FFMPEG command. You must set the stream's IO by calling
     * prepareStream before calling this method.
     *
     * @param preset
     * @throws StreamIONotSetException
     */
    public void setOutputPreset(IOutputPreset preset) throws StreamIONotSetException {

        if (outputCommandBuilder == null) {
            throw new StreamIONotSetException("The inputs and output for the stream have not been set yet. " +
                    "Call prepareStream ()");
        }

        ffmpegBuilder = preset.addPresetsToCommandBuilder(outputCommandBuilder);
    }



    /**
     * This is the method you have to use in order to properly start a stream. This method will create a thread when
     * called for the first time then start the stream. If the thread already exist and has not been terminated, it will
     * simply start a stream without creating a new thread.
     *
     * @throws CantStartStreamException
     */
    public void startStreaming() throws CantStartStreamException {

        if (getState() == State.NEW)
            start();
        else if (getState() != State.TERMINATED)
            //if the thread is active, aka has not terminated, try to start ffmpeg without starting the thread again
            startStreamJob();
        else
            throw new CantStartStreamException("The current StreamManager thread has state:" + State.TERMINATED);
    }

    /**
     * Create and execute the FFMPEG job handling the stream.
     */
    private void startStreamJob() {
        if (!isStreamJobActive()) {
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            job = executor.createJob(ffmpegBuilder);
            job.run();
        }
    }

    @Override
    public void run() {
        startStreamJob();
    }

    /**
     * Will kill any FFMPEG process running on the machine by sending it a SIGINT signal. This is currently
     * the only way to kill stopStreaming.
     *
     * @throws IOException
     */
    public void stopStreaming() throws IOException {

        if (isStreamJobActive()) {
            ffmpeg.terminateProcess();
        }
    }

    /**
     * Will get the FFMPEG executable and instantiate the FFMPEG wrapper. Computer agnostic.
     *
     * @throws IOException
     * @throws OSNotSupportedError
     */
    private void instantiateFFMPEG() throws IOException {

        Class objClass = this.getClass();
        PathFactory pathFactory = PathFactory.getInstance();

        ffmpeg = new FFmpeg(pathFactory.getFFMPEGPath(objClass));
        ffprobe = new FFprobe(pathFactory.getFFPROBEPath(objClass));

    }

    /**
     * @return the set input audio driver
     */
    public IFFMPEGCompatibleDriver getInputAudioDriver() {
        return inputAudioDriver;
    }

    /**
     * @param inputAudioDriver
     */
    public void setInputAudioDriver(IFFMPEGCompatibleDriver inputAudioDriver) {
        this.inputAudioDriver = inputAudioDriver;
    }

    /**
     * @return the set input video driver
     */
    public IFFMPEGCompatibleDriver getInputVideoDriver() {
        return inputVideoDriver;
    }

    /**
     * @param inputVideoDriver
     */
    public void setInputVideoDriver(IFFMPEGCompatibleDriver inputVideoDriver) {
        this.inputVideoDriver = inputVideoDriver;
    }


    /**
     * @param output
     */
    public void setOutputPath(String output) {
        this.output = output;
    }

    /**
     * Set the value by which the audio should be delayed (in seconds).
     * @param audioOffset
     */
    public void setAudioOffset(double audioOffset) {this.audioOffset = audioOffset;}

    /**
     * @return the path where the stream is sent
     */
    public String getOutputPath() {
        return output;
    }

    /**
     * Checks if StreamManager currently has an active (running or idle) FFMPEG stream job.
     *
     * @return
     */
    public boolean isStreamJobActive() {
        return job != null && (job.getState() == FFmpegJob.State.RUNNING || job.getState() == FFmpegJob.State.WAITING);
    }

}
