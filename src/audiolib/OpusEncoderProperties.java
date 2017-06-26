/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import tomp2p.opuswrapper.Opus;


/**
 *
 * @author bowen
 */
public interface OpusEncoderProperties {
    
    static interface OpusEncoderInt32 {
        public int getInt32();
    }
    
    public enum SampleRate implements OpusEncoderInt32 {
        N8000(8000), N12000(12000), N16000(16000), N24000(24000), N48000(48000);
        
        private final int samplingRate;
        
        SampleRate(int i) {
            samplingRate = i;
        }
        public int getInt32() {
            return samplingRate;
        }
    }
    
    public static SampleRate getSampleRate(int opus_int32) {
        switch (opus_int32) {
            case 8000:
                return SampleRate.N8000;
            case 12000:
                return SampleRate.N12000;
            case 16000:
                return SampleRate.N16000;
            case 24000:
                return SampleRate.N24000;
            default:
                return SampleRate.N48000;
        }
    }
    
    public enum Channels implements OpusEncoderInt32 {
        AUTO(Opus.OPUS_AUTO), MONO(1), STEREO(2);
        
        private final int channels;
        
        Channels(int i) {
            channels = i;
        }
        public int getInt32() {
            return channels;
        }
    }
    
    public static Channels getChannels(int opus_int32) {
        switch (opus_int32) {
            case 1:
                return Channels.MONO;
            case 2:
                return Channels.STEREO;
            default:
                return Channels.AUTO;
        }
    }
    
    public enum Application implements OpusEncoderInt32 {
        VOIP(Opus.OPUS_APPLICATION_VOIP), AUDIO(Opus.OPUS_APPLICATION_AUDIO), RESTRICTED_LOWDELAY(Opus.OPUS_APPLICATION_RESTRICTED_LOWDELAY);

        private final int opus_int32;
        
        Application(int i) {
            opus_int32 = i;
        }
        public int getInt32() {
            return opus_int32;
        }
    }
    
    public static Application getApplication(int opus_int32) {
        switch (opus_int32) {
            case Opus.OPUS_APPLICATION_VOIP:
                return Application.VOIP;
            case Opus.OPUS_APPLICATION_AUDIO:
                return Application.AUDIO;
            default:
                return Application.RESTRICTED_LOWDELAY;
        }
    }
    
    
    public enum Bandwidth implements OpusEncoderInt32 {
        AUTO(Opus.OPUS_AUTO), NARROWBAND(Opus.OPUS_BANDWIDTH_NARROWBAND), MEDIUMBAND(Opus.OPUS_BANDWIDTH_MEDIUMBAND), WIDEBAND(Opus.OPUS_BANDWIDTH_WIDEBAND), SUPERWIDEBAND(Opus.OPUS_BANDWIDTH_SUPERWIDEBAND), FULLBAND(Opus.OPUS_BANDWIDTH_FULLBAND);

        private final int opus_int32;
        
        Bandwidth(int i) {
            opus_int32 = i;
        }
        public int getInt32() {
            return opus_int32;
        }
    }
    
    public static Bandwidth getBandwidth(int opus_int32) {
        switch (opus_int32) {
            case Opus.OPUS_BANDWIDTH_NARROWBAND:
                return Bandwidth.NARROWBAND;
            case Opus.OPUS_BANDWIDTH_MEDIUMBAND:
                return Bandwidth.MEDIUMBAND;
            case Opus.OPUS_BANDWIDTH_WIDEBAND:
                return Bandwidth.WIDEBAND;
            case Opus.OPUS_BANDWIDTH_SUPERWIDEBAND:
                return Bandwidth.SUPERWIDEBAND;
            case Opus.OPUS_BANDWIDTH_FULLBAND:
                return Bandwidth.FULLBAND;
            default:
                return Bandwidth.AUTO;
        }
    }
    
    public enum SpecialBitrate implements OpusEncoderInt32 {
        AUTO(Opus.OPUS_AUTO), MAX(Opus.OPUS_BITRATE_MAX);

        private final int opus_int32;
        
        SpecialBitrate(int i) {
            opus_int32 = i;
        }
        public int getInt32() {
            return opus_int32;
        }
    }
    
    public enum SignalType implements OpusEncoderInt32 {
        AUTO(Opus.OPUS_AUTO), VOICE(Opus.OPUS_SIGNAL_VOICE), MUSIC(Opus.OPUS_SIGNAL_MUSIC);

        private final int opus_int32;
        
        SignalType(int i) {
            opus_int32 = i;
        }
        public int getInt32() {
            return opus_int32;
        }
    }
    public static SignalType getSignalType(int opus_int32) {
        switch (opus_int32) {
            case Opus.OPUS_SIGNAL_VOICE:
                return SignalType.VOICE;
            case Opus.OPUS_SIGNAL_MUSIC:
                return SignalType.MUSIC;
            default:
                return SignalType.AUTO;
        }
    }
    
}
