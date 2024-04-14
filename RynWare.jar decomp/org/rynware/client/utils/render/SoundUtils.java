// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import java.io.InputStream;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioSystem;
import org.rynware.client.utils.Helper;

public class SoundUtils implements Helper
{
    public static synchronized void playSound(final String url, final float volume, final boolean stop) {
        new Thread(() -> {
            try {
                final Clip clip = AudioSystem.getClip();
                final InputStream audioSrc = SoundUtils.class.getResourceAsStream("/assets/minecraft/rynware/sounds/" + url);
                final BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                final AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
                final FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
                if (stop) {
                    clip.stop();
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
