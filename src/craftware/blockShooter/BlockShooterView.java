package craftware.blockShooter;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlockShooterView extends SurfaceView implements SurfaceHolder.Callback
{
	/**
	 * Base class for any external event passed to the BlockShooterThread. This
	 * can include user input, system events, network input, etc.
	 */
	class GameEvent
	{
		public GameEvent()
		{
			eventTime = System.currentTimeMillis();
		}

		long eventTime;
	}

	/**
	 * A GameEvent subclass for key based user input. Values are those used by
	 * the standard onKey
	 */
	class KeyGameEvent extends GameEvent
	{
		/**
		 * Simple constructor to make populating this event easier.
		 */
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

	// JET info: the BlockShooterThread receives all the events from the JET
	// player
	// JET info: through the OnJetEventListener interface.
	class BlockShooterThread extends Thread
	{
		/** Queue for GameEvents */
		protected ConcurrentLinkedQueue<GameEvent> mEventQueue = new ConcurrentLinkedQueue<GameEvent>();

		/** Context for processKey to maintain state accross frames * */
		protected Object mKeyContext = null;

		// the timer display in seconds
		public int mTimerLimit;

		// used for internal timing logic.
		public final int TIMER_LIMIT = 72;

		// start, play, running, lose are the states we use
		public int mState;

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

		/**
		 * the heart of the worker bee
		 */
		public void run()
		{
			// while running do stuff in this loop...bzzz!
			while (mRun)
			{
				Canvas c = null;
				
				framework.update();

				try
				{
					c = mSurfaceHolder.lockCanvas(null);
					// synchronized (mSurfaceHolder) {
					doDraw(c);
					// }
				} 
				finally 
				{
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) 
					{
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}// end finally block
			}// end while mrun block
		}

		/**
		 * This method handles updating the model of the game state. No
		 * rendering is done here only processing of inputs and update of state.
		 * This includes positons of all game objects (asteroids, player,
		 * explosions), their state (animation frame, hit), creation of new
		 * objects, etc.
		 */
		protected void updateGameState()
		{
			// Process any game events and apply them
			while (true)
			{
				GameEvent event = mEventQueue.poll();
				if (event == null)
					break;

				// Process keys tracking the input context to pass in to later
				// calls
				if (event instanceof KeyGameEvent)
				{
					// Process the key for affects other then asteroid hits
					mKeyContext = processKeyEvent((KeyGameEvent) event, mKeyContext);
				}
			}
		}

		/**
		 * This method handles the state updates that can be caused by key press
		 * events. Key events may mean different things depending on what has
		 * come before, to support this concept this method takes an opaque
		 * context object as a parameter and returns an updated version. This
		 * context should be set to null for the first event then should be set
		 * to the last value returned for subsequent events.
		 */
		protected Object processKeyEvent(KeyGameEvent event, Object context)
		{
			// Log.d(TAG, "key code is " + event.keyCode + " " + (event.up ?
			// "up":"down"));

			// If it is a key up on the fire key make sure we mute the
			// associated sound
			if (event.up) {
				if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
					return null;
				}
			}
			// If it is a key down on the fire key start playing the sound and
			// update the context
			// to indicate that a key has been pressed and to ignore further
			// presses
			else {
				if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER
						&& (context == null)) {
					return event;
				}
			}

			// Return the context unchanged
			return context;
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
		 * returns the current int value of game state as defined by state
		 * tracking constants
		 * 
		 * @return
		 */
		public int getGameState() {
			synchronized (mSurfaceHolder) {
				return mState;
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
		public void setGameState(int mode) {
			synchronized (mSurfaceHolder) {
				//setGameState(mode, null);
			}
		}

		/**
		 * Add key press input to the GameEvent queue
		 */
		public boolean doKeyDown(int keyCode, KeyEvent msg)
		{
			mEventQueue.add(new KeyGameEvent(keyCode, false, msg));

			return true;
		}

		/**
		 * Add key press input to the GameEvent queue
		 */
		public boolean doKeyUp(int keyCode, KeyEvent msg)
		{
			mEventQueue.add(new KeyGameEvent(keyCode, true, msg));

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

	public static final String TAG = "BlockShooter";

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
