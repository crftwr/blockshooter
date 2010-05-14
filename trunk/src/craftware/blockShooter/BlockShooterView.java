package craftware.blockShooter;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlockShooterView extends SurfaceView implements SurfaceHolder.Callback
{
	class GameEvent
	{
		public GameEvent()
		{
			eventTime = System.currentTimeMillis();
		}

		long eventTime;
	}

	class KeyGameEvent extends GameEvent
	{
		public KeyGameEvent(int keyCode, boolean up, KeyEvent msg)
		{
			this.keyCode = keyCode;
			this.msg = msg;
			this.up = up;
		}

		public int keyCode;
		public KeyEvent msg;
		public boolean up;
	}

	class TouchGameEvent extends GameEvent
	{
		public TouchGameEvent( MotionEvent _event )
		{
			event = _event;
		}

		public MotionEvent event;
	}

	// JET info: the BlockShooterThread receives all the events from the JET
	// player
	// JET info: through the OnJetEventListener interface.
	class BlockShooterThread extends Thread
	{
		/** Queue for GameEvents */
		protected ConcurrentLinkedQueue<GameEvent> mEventQueue = new ConcurrentLinkedQueue<GameEvent>();

		/** Context for processKey to maintain state accross frames * */
		protected Object mKeyContext = null;

		/** Handle to the surface manager object we interact with */
		private SurfaceHolder mSurfaceHolder;

		/** Indicate whether the surface has been created & is ready to draw */
		private boolean mRun = false;

		private BlockShooterFramework framework;

		/**
		 * This is the constructor for the main worker bee
		 * 
		 * @param surfaceHolder
		 * @param context
		 */
		public BlockShooterThread( SurfaceHolder surfaceHolder, Context context )
		{
			mSurfaceHolder = surfaceHolder;

			framework = new BlockShooterFramework(context);
		}

		private void doDraw(Canvas canvas)
		{
			framework.draw(canvas);
		}

		public void run()
		{
			while (mRun)
			{
				pollGameEvent();
				
				framework.update();
				
				draw();
			}
		}

		protected void pollGameEvent()
		{
			// Process any game events and apply them
			while (true)
			{
				GameEvent event = mEventQueue.poll();
				if (event == null)
				{
					break;
				}

				// Process keys tracking the input context to pass in to later calls
				if( event instanceof KeyGameEvent )
				{
					framework.input.key.post( ((KeyGameEvent) event).msg );
				}
				else if( event instanceof TouchGameEvent )
				{
					framework.input.touch.post( ((TouchGameEvent) event).event );
				}
			}
		}
		
		public void draw()
		{
			Canvas c = null;
			try
			{
				c = mSurfaceHolder.lockCanvas(null);
				doDraw(c);
			} 
			finally 
			{
				if (c != null) 
				{
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}

		/**
		 * Used to signal the thread whether it should be running or not.
		 * Passing true allows the thread to run; passing false will shut it
		 * down if it's already running. Calling start() after this was most
		 * recently called with false will result in an immediate shutdown.
		 * 
		 * @param b
		 *            true to run, false to shut down
		 */
		public void setRunning(boolean b)
		{
			mRun = b;

			if(mRun == false)
			{
			}
		}

		/**
		 * Sets the game mode. That is, whether we are running, paused, in the
		 * failure state, in the victory state, etc.
		 * 
		 * @see #setState(int, CharSequence)
		 * @param mode
		 *            one of the STATE_* constants
		 */
		public void setGameState(int mode)
		{
			synchronized (mSurfaceHolder)
			{
				//setGameState(mode, null);
			}
		}

		public boolean doKeyDown(int keyCode, KeyEvent msg)
		{
			mEventQueue.add(new KeyGameEvent(keyCode, false, msg));

			return true;
		}

		public boolean doKeyUp(int keyCode, KeyEvent msg)
		{
			mEventQueue.add(new KeyGameEvent(keyCode, true, msg));

			return true;
		}

		public boolean doTouchEvent(MotionEvent event)
		{
			mEventQueue.add(new TouchGameEvent(event));

			return true;
		}

		/* Callback invoked when the surface dimensions change. */
		public void setSurfaceSize(int width, int height)
		{
			// synchronized to make sure these all change atomically
			synchronized (mSurfaceHolder) 
			{
				framework.setSurfaceSize(width,height);
			}
		}

		/**
		 * Pauses the physics update & animation.
		 */
		public void pause()
		{
			synchronized (mSurfaceHolder)
			{
			}
		}

	}// end thread class

	/** The thread that actually draws the animation */
	private BlockShooterThread thread;

	/**
	 * The constructor called from the main BlockShooter activity
	 * 
	 * @param context
	 * @param attrs
	 */
	public BlockShooterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// create thread only; it's started in surfaceCreated()
		// except if used in the layout editor.
		if (isInEditMode() == false)
		{
			thread = new BlockShooterThread(holder, context);
		}
		
		setFocusable(true); // make sure we get key events
	}

	/**
	 * Standard window-focus override. Notice focus lost so we can pause on
	 * focus lost. e.g. user switches to take a call.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus)
	{
		if(!hasWindowFocus)
		{
			if (thread != null)
			{
				thread.pause();
			}
		}
	}

	/**
	 * Fetches the animation thread corresponding to this LunarView.
	 * 
	 * @return the animation thread
	 */
	public BlockShooterThread getThread()
	{
		return thread;
	}

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		thread.setSurfaceSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder arg0)
	{
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		boolean retry = true;
		thread.setRunning(false);
		while(retry)
		{
			try
			{
				thread.join();
				retry = false;
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}
