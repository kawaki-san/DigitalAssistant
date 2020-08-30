package com.rtkay.audio;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.InputStream;

public class AudioPlayer {

    public static void output(InputStream audioStream){
        AdvancedPlayer player = null;
        try {
            player = new AdvancedPlayer(audioStream,
                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }
        });
        try {
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }

    }
}
