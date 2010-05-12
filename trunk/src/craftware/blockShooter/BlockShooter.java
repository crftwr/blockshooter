package craftware.blockShooter;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import craftware.blockShooter.BlockShooterView.BlockShooterThread;

public class BlockShooter extends Activity implements View.OnClickListener {

    /** A handle to the thread that's actually running the animation. */
	private BlockShooterThread mBlockShooterThread;

    /** A handle to the View in which the game is running. */
    private BlockShooterView mBlockShooterView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // get handles to the JetView from XML and the JET thread.
        mBlockShooterView = (BlockShooterView)findViewById(R.id.BlockShooterView);
        mBlockShooterThread = mBlockShooterView.getThread();
    }

    /**
     * Handles component interaction
     * 
     * @param v The object which has been clicked
     */
    public void onClick(View v)
    {
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            return super.onKeyDown(keyCode, msg);
        }
        else
        {
            return mBlockShooterThread.doKeyDown(keyCode, msg);
        }
    }

    /**
     * Standard override for key-up.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            return super.onKeyUp(keyCode, msg);
        }
        else
        {
            return mBlockShooterThread.doKeyUp(keyCode, msg);
        }
    }
}
