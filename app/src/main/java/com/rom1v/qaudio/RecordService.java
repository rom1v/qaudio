package com.rom1v.qaudio;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.Log;

public class RecordService extends Service {

    private static final String TAG = "qaudio";
    private static final String CHANNEL_ID = "qaudio";

    private static final String ACTION_RECORD = "com.rom1v.qaudio.RECORD";
    private static final String EXTRA_MEDIA_PROJECTION_DATA = "mediaProjectionData";

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    public static void start(Context context, Intent data) {
        Intent intent = new Intent(context, RecordService.class);
        intent.setAction(ACTION_RECORD);
        intent.putExtra(EXTRA_MEDIA_PROJECTION_DATA, data);
        context.startForegroundService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder notificationBuilder = new Notification.Builder(this, CHANNEL_ID);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText("qaudio");
        Notification notification = notificationBuilder.build();

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
        getNotificationManager().createNotificationChannel(channel);

        startForeground(0, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "START COMMAND");

        Intent data = intent.getParcelableExtra(EXTRA_MEDIA_PROJECTION_DATA);
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, data);
        if (mediaProjection != null) {
            startRecording();
            Log.d(TAG, "===== OK");
        } else {
            Log.w(TAG, "====== oops");
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startRecording() {
        AudioPlaybackCaptureConfiguration.Builder builder = new AudioPlaybackCaptureConfiguration.Builder(mediaProjection);
        builder.addMatchingUsage(AudioAttributes.USAGE_MEDIA);
        AudioPlaybackCaptureConfiguration config = builder.build();
        final AudioRecord recorder = new AudioRecord.Builder().setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION).setAudioFormat(
                new AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(48000).setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                        .build()).setBufferSizeInBytes(2 * 1024 * 1024).setAudioPlaybackCaptureConfig(config).build();
        recorder.startRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buf = new byte[1024];
                int r = recorder.read(buf, 0, buf.length);
                Log.d(TAG, "read = " + r);
            }
        }).start();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}