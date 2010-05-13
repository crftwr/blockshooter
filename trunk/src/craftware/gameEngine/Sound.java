package craftware.gameEngine;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Sound
{
	public Sound( Context context, int id )
	{
		media_player = MediaPlayer.create( context, id );
	}
	
	private void stopInternal()
	{
		media_player.stop();

		try
		{
			media_player.prepare();
		}
		catch(IOException e)
		{
			Log.e( "gameEngine", "sound prepare error :" + e );
		}
		catch(IllegalStateException e)
		{
			Log.e( "gameEngine", "sound prepare error :" + e );
		}
		
		media_player.seekTo(0);
	}
	
	public void play()
	{
		stopInternal();
		media_player.start();
	}
	
	public void stop()
	{
		stopInternal();
	}
	
	private MediaPlayer media_player;
}
