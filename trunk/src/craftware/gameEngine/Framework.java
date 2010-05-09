package craftware.gameEngine;

import java.util.Vector;

import android.graphics.Canvas;

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
	
	public void Update()
	{
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).Update();
		}
	}

	public void Draw(Canvas canvas)
	{
		for( int i=0 ; i<actor_list.size() ; ++i )
		{
			actor_list.elementAt(i).Draw(canvas);
		}
	}
	
	private Vector<Actor> actor_list; 
}
