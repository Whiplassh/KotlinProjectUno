package com.example.projectuno.module_4.task_7

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: Int
)

enum class PlaybackState {
    IDLE, PLAYING, PAUSED, STOPPED
}

class MusicService : Service() {
    private val binder = MusicBinder()
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private var playbackJob: Job? = null

    private val playlist = listOf(
        Track(1, "Summer Vibes", "Artist A", 180),
        Track(2, "Night Drive", "Artist B", 240),
        Track(3, "Morning Coffee", "Artist C", 200),
        Track(4, "Sunset Dreams", "Artist D", 210),
        Track(5, "City Lights", "Artist E", 195)
    )

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        println("MusicService: onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("MusicService: onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MusicService: onDestroy")
        stop()
        serviceScope.cancel()
    }

    fun getPlaylist(): List<Track> = playlist

    fun play(track: Track) {
        if (_currentTrack.value?.id == track.id && _playbackState.value == PlaybackState.PAUSED) {
            resume()
            return
        }

        stop()
        _currentTrack.value = track
        _currentPosition.value = 0
        _playbackState.value = PlaybackState.PLAYING

        playbackJob = serviceScope.launch {
            while (_currentPosition.value < track.duration && _playbackState.value == PlaybackState.PLAYING) {
                delay(1000)
                _currentPosition.value += 1
            }

            if (_currentPosition.value >= track.duration) {
                playNext()
            }
        }
    }

    fun pause() {
        if (_playbackState.value == PlaybackState.PLAYING) {
            _playbackState.value = PlaybackState.PAUSED
            playbackJob?.cancel()
        }
    }

    fun resume() {
        if (_playbackState.value == PlaybackState.PAUSED) {
            _playbackState.value = PlaybackState.PLAYING
            val track = _currentTrack.value ?: return

            playbackJob = serviceScope.launch {
                while (_currentPosition.value < track.duration && _playbackState.value == PlaybackState.PLAYING) {
                    delay(1000)
                    _currentPosition.value += 1
                }

                if (_currentPosition.value >= track.duration) {
                    playNext()
                }
            }
        }
    }

    fun stop() {
        _playbackState.value = PlaybackState.STOPPED
        playbackJob?.cancel()
        _currentPosition.value = 0
    }

    fun playNext() {
        val currentTrack = _currentTrack.value ?: return
        val currentIndex = playlist.indexOfFirst { it.id == currentTrack.id }
        val nextIndex = (currentIndex + 1) % playlist.size
        play(playlist[nextIndex])
    }

    fun playPrevious() {
        val currentTrack = _currentTrack.value ?: return
        val currentIndex = playlist.indexOfFirst { it.id == currentTrack.id }
        val previousIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        play(playlist[previousIndex])
    }

    fun seekTo(position: Int) {
        val track = _currentTrack.value ?: return
        _currentPosition.value = position.coerceIn(0, track.duration)
    }
}
