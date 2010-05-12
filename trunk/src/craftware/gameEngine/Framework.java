package craftware.gameEngine;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class Framework
{
	public Framework()
	{
		actor_list = new Vector<Actor>();

		current_time_msec = System.currentTimeMillis();
		prev_time_msec = current_time_msec;
		delta_time_msec = 0;
	}
	
	public void appendActor( Actor actor )
	{
		actor_list.addElement(actor);
	}
	
	public void removeActor( Actor actor )
	{
		actor_list.removeElement(actor);
	}
	
	public void setClearMode( boolean _enabled, int _color )
	{
		clear_enabled = _enabled;
		clear_color = _color;
	}

	public void setSurfaceSize(int width, int height)
	{
		surface_width = width;
		surface_height = height;
	}
	
	public void setVirtualSurfaceSize(int width, int height)
	{
		virtual_surface_width = width;
		virtual_surface_height = height;
	}
	
	public long deltaTimeMsec()
	{
		return delta_time_msec;
	}
	
	public void update()
	{
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).update();
		}
	}

	public void draw(Canvas canvas)
	{
		if(clear_enabled)
		{
			canvas.drawColor(clear_color);
		}
		
		canvas.scale( ((float)surface_width) / virtual_surface_width, ((float)surface_height) / virtual_surface_height );
		
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).draw(canvas);
		}

		prev_time_msec = current_time_msec; 
		current_time_msec = System.currentTimeMillis();
		delta_time_msec = current_time_msec - prev_time_msec;
		delta_time_msec = Math.max( delta_time_msec, 1);
		delta_time_msec = Math.min( delta_time_msec, 100);
		
        //Log.d( "gameEngine", "delta:" + delta_time_msec );
	}
	
	private Vector<Actor> actor_list; 
	
	private int surface_width = 1;
	private int surface_height = 1;

	protected int virtual_surface_width  = 480;
	protected int virtual_surface_height = 800;
	
	private boolean clear_enabled = true;
	private int clear_color = Color.BLACK;
	
	private long current_time_msec;
	private long prev_time_msec;
	protected long delta_time_msec;
}
