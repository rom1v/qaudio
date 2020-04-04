This is a quick & dirty attempt to use the [Playback capture API] from Android Q
to record device audio.

[Playback capture API]: https://developer.android.com/guide/topics/media/playback-capture

For now, it just does not work:

```
E AndroidRuntime: java.lang.RuntimeException: Unable to start service com.rom1v.qaudio.RecordService@4111b9c with Intent { act=com.rom1v.qaudio.RECORD cmp=com.rom1v.qaudio/.RecordService (has extras) }: java.lang.SecurityException: Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
E AndroidRuntime:    at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:4222)
E AndroidRuntime:    at android.app.ActivityThread.access$2100(ActivityThread.java:231)
E AndroidRuntime:    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1984)
E AndroidRuntime:    at android.os.Handler.dispatchMessage(Handler.java:107)
E AndroidRuntime:    at android.os.Looper.loop(Looper.java:214)
E AndroidRuntime:    at android.app.ActivityThread.main(ActivityThread.java:7682)
E AndroidRuntime:    at java.lang.reflect.Method.invoke(Native Method)
E AndroidRuntime:    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:516)
E AndroidRuntime:    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:950)
E AndroidRuntime: Caused by: java.lang.SecurityException: Media projections require a foreground service of type ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
E AndroidRuntime:    at android.os.Parcel.createException(Parcel.java:2071)
E AndroidRuntime:    at android.os.Parcel.readException(Parcel.java:2039)
E AndroidRuntime:    at android.os.Parcel.readException(Parcel.java:1987)
E AndroidRuntime:    at android.media.projection.IMediaProjection$Stub$Proxy.start(IMediaProjection.java:231)
E AndroidRuntime:    at android.media.projection.MediaProjection.<init>(MediaProjection.java:58)
E AndroidRuntime:    at android.media.projection.MediaProjectionManager.getMediaProjection(MediaProjectionManager.java:104)
E AndroidRuntime:    at com.rom1v.qaudio.RecordService.onStartCommand(RecordService.java:60)
E AndroidRuntime:    at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:4204)
E AndroidRuntime:    ... 8 more
E AndroidRuntime: Caused by: android.os.RemoteException: Remote stack trace:
E AndroidRuntime:    at com.android.server.media.projection.MediaProjectionManagerService$MediaProjection.start(MediaProjectionManagerService.java:485)
E AndroidRuntime:    at android.media.projection.IMediaProjection$Stub.onTransact(IMediaProjection.java:135)
E AndroidRuntime:    at android.os.Binder.execTransactInternal(Binder.java:1032)
E AndroidRuntime:    at android.os.Binder.execTransact(Binder.java:1005)
```
