package craftware.gameEngine;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;

public class Framework
{
	public Framework()
	{
		actor_list = new Vector<Actor>();
	}
	
	public void AppendActor( Actor actor )
	{
		actor_list.addElement(actor);
	}
	
	public void RemoveActor( Actor actor )
	{
		actor_list.removeElement(actor);
	}
	
	public void SetClearMode( boolean _enabled, int _color )
	{
		clear_enabled = _enabled;
		clear_color = _color;
	}
	
	public void Update()
	{
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).Update();
		}
	}

	public void Draw(Canvas canvas)
	{
		if(clear_enabled)
		{
			canvas.drawColor(clear_color);
		}
		
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).Draw(canvas);
		}
	}
	
	private Vector<Actor> actor_list; 

	private boolean clear_enabled = true;
	private int clear_color = Color.BLACK;
}
