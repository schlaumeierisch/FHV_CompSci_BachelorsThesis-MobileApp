package at.fhv.mme.bt_dementia_app.utils

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerUtils {
    private var mediaPlayer: MediaPlayer? = null

    fun startMediaPlayer(context: Context, audioResource: Int) {
        stopMediaPlayer()

        mediaPlayer = MediaPlayer.create(context, audioResource)
        mediaPlayer?.start()
    }

    fun stopMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            listener()
        }
    }
}