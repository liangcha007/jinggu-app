package com.jingu.app.main.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jingu.app.R;
import com.jingu.app.zxing.camera.CameraManager;
import com.jingu.app.zxing.decoding.CaptureActivityHandler;
import com.jingu.app.zxing.decoding.InactivityTimer;
import com.jingu.app.zxing.view.ViewfinderView;

/**
 * Initial the camera
 * 
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback
{

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String mark;
    private boolean isLight;
    public Integer type;
    private String bottleCode;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_capture);
	CameraManager.init(getApplication());
	viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

	mark = "";
	type = 0;
	bottleCode = "";
	isLight = false;
	if (getIntent() != null)
	{
	    if (getIntent().getStringExtra("Scan") != null && !"".equals(getIntent().getStringExtra("Scan")))
	    {
		mark = getIntent().getStringExtra("Scan").toString();
		type = getIntent().getIntExtra("type", 0);
	    }
	}
	// ���ذ�ť�ĵ�����Ӧ
	Button mButtonBack = (Button) findViewById(R.id.button_back);
	mButtonBack.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		if ("Scan".equals(mark))
		{
		    if (type == 2)
		    {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", bottleCode);
			resultIntent.putExtras(bundle);
			MipcaActivityCapture.this.setResult(RESULT_FIRST_USER, resultIntent);
			MipcaActivityCapture.this.finish();
		    }
		    else
		    {
			MipcaActivityCapture.this.finish();
		    }
		}
		else
		{
		    MipcaActivityCapture.this.finish();
		}
	    }
	});
	// �ж��Ƿ�Ҫ��ʾ"ɨ�����"
	if ("Scan".equals(mark))
	{
	    Button button = (Button) findViewById(R.id.btn_end_scan);
	    // ����type���Ͳ�ͬ����ʾ��ͬ�İ�ť
	    switch (type)
	    {
	    case 1:
		// ɨ���ύ����ťĬ�ϴ�������ʾ
		break;
	    case 2:
		// ɨ�����ύ����ʾ��ť������ύ����
		button.setVisibility(Button.VISIBLE);
		button.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", bottleCode);
			resultIntent.putExtras(bundle);
			MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
			MipcaActivityCapture.this.finish();
		    }
		});
		break;
	    case 3:
		// ɨ�뷵�أ������ť����ǰһҳ��
		button.setVisibility(Button.VISIBLE);
		button.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", bottleCode);
			resultIntent.putExtras(bundle);
			MipcaActivityCapture.this.setResult(RESULT_FIRST_USER, resultIntent);
			MipcaActivityCapture.this.finish();
		    }
		});
		break;
	    default:
		break;
	    }
	}

	hasSurface = false;
	inactivityTimer = new InactivityTimer(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume()
    {
	super.onResume();
	SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
	SurfaceHolder surfaceHolder = surfaceView.getHolder();
	if (hasSurface)
	{
	    initCamera(surfaceHolder);
	}
	else
	{
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	decodeFormats = null;
	characterSet = null;

	playBeep = true;
	AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
	if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
	{
	    playBeep = false;
	}
	initBeepSound();
	vibrate = true;

    }

    @Override
    protected void onPause()
    {
	super.onPause();
	if (handler != null)
	{
	    handler.quitSynchronously();
	    handler = null;
	}
	isLight = false;
	CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy()
    {
	inactivityTimer.shutdown();
	super.onDestroy();
    }

    /**
     * ����ɨ����
     * 
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result)
    {
	inactivityTimer.onActivity();
	playBeepSoundAndVibrate();
	String resultString = result.getText();
	if (resultString.equals(""))
	{
	    Toast.makeText(MipcaActivityCapture.this, "ɨ��ʧ�ܣ��볢�������Ƕ�����ɨ��!", Toast.LENGTH_SHORT).show();
	    MipcaActivityCapture.this.finish();
	}
	else
	{
	    if ("Scan".equals(mark))
	    {
		if (type == 1)
		{
		    // ֱ��ɨ���ύ
		    Intent resultIntent = new Intent();
		    Bundle bundle = new Bundle();
		    bundle.putString("result", resultString);
		    resultIntent.putExtras(bundle);
		    MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
		    MipcaActivityCapture.this.finish();
		}
		else
		{
		    // ���ɨ�裬����¼ɨ���¼
		    if ("".equals(bottleCode))
		    {
			bottleCode = resultString + ",";
		    }
		    else
		    {
			if (!bottleCode.contains(resultString))
			{
			    bottleCode += resultString + ",";
			}
		    }
		    Toast.makeText(MipcaActivityCapture.this, "������,��ƿ��ţ�" + resultString, Toast.LENGTH_SHORT).show();
		}
	    }
	    else
	    {
		// ɨ��鿴��ƿ��Ϣ
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("result", resultString);
		resultIntent.putExtras(bundle);
		this.setResult(RESULT_OK, resultIntent);
		MipcaActivityCapture.this.finish();
	    }
	}

    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
	try
	{
	    CameraManager.get().openDriver(surfaceHolder);
	}
	catch (IOException ioe)
	{
	    return;
	}
	catch (RuntimeException e)
	{
	    return;
	}
	if (handler == null)
	{
	    handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
	}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
	if (!hasSurface)
	{
	    hasSurface = true;
	    initCamera(holder);
	}

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
	hasSurface = false;

    }

    public ViewfinderView getViewfinderView()
    {
	return viewfinderView;
    }

    public Handler getHandler()
    {
	return handler;
    }

    public void drawViewfinder()
    {
	viewfinderView.drawViewfinder();

    }

    private void initBeepSound()
    {
	if (playBeep && mediaPlayer == null)
	{
	    // The volume on STREAM_SYSTEM is not adjustable, and users found it
	    // too loud,
	    // so we now play on the music stream.
	    setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    mediaPlayer = new MediaPlayer();
	    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    mediaPlayer.setOnCompletionListener(beepListener);

	    AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
	    try
	    {
		mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
		file.close();
		mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
		mediaPlayer.prepare();
	    }
	    catch (IOException e)
	    {
		mediaPlayer = null;
	    }
	}
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate()
    {
	if (playBeep && mediaPlayer != null)
	{
	    mediaPlayer.start();
	}
	if (vibrate)
	{
	    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	    vibrator.vibrate(VIBRATE_DURATION);
	}
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener()
    {
	public void onCompletion(MediaPlayer mediaPlayer)
	{
	    mediaPlayer.seekTo(0);
	}
    };

    /**
     * ���ֵ�Ͳ
     * 
     * @param v
     */
    public void FlishLight(View v)
    {
	Button lightButton = (Button) this.findViewById(R.id.light_button);
	if (!isLight)
	{
	    isLight = true;
	    CameraManager.get().setFlashlight(isLight);
	    lightButton.setBackground(getResources().getDrawable(R.drawable.dark));
	}
	else
	{
	    isLight = false;
	    CameraManager.get().setFlashlight(isLight);
	    lightButton.setBackground(getResources().getDrawable(R.drawable.light));
	}
    }
}