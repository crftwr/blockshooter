package craftware.gameEngine;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Input
{
	public class Key
	{
		public void clearPressedReleased()
		{
			up_pressed = false; 
			down_pressed = false; 
			left_pressed = false; 
			right_pressed = false; 

			up_released = false; 
			down_released = false; 
			left_released = false; 
			right_released = false; 
		}
		
		public void post( KeyEvent event )
		{
			Log.d( "gameEngine", "key:" + event + event.getAction() );
			
			if(event.getAction()==KeyEvent.ACTION_DOWN)
			{
				switch(event.getKeyCode())
				{
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if(!left){ left_pressed = true; }
					left = true;
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if(!right){ right_pressed = true; }
					right = true;
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					if(!up){ up_pressed = true; }
					up = true;
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					if(!down){ down_pressed = true; }
					down = true;
					break;
				}
			}
			else if(event.getAction()==KeyEvent.ACTION_UP)
			{
				switch(event.getKeyCode())
				{
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if(left){ left_released = true; }
					left = false;
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if(right){ right_released = true; }
					right = false;
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					if(up){ up_released = true; }
					up = false;
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					if(down){ down_released = true; }
					down = false;
					break;
				}
			}
			else
			{
				assert(false);
			}
		}
		
		public boolean up = false; 
		public boolean down = false; 
		public boolean left = false; 
		public boolean right = false; 

		public boolean up_pressed = false; 
		public boolean down_pressed = false; 
		public boolean left_pressed = false; 
		public boolean right_pressed = false; 

		public boolean up_released = false; 
		public boolean down_released = false; 
		public boolean left_released = false; 
		public boolean right_released = false; 
	}
	
	public class Touch
	{
		public void post( MotionEvent event )
		{
			Log.d( "gameEngine", "touch:" + event );
		}
	}
	
	public Key key = new Key();
	public Touch touch = new Touch();
}
