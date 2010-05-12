package craftware.blockShooter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import craftware.blockShooter.BlockShooterView.BlockShooterThread;

public class BlockShooter extends Activity implements View.OnClickListener {

    /** A handle to the thread that's actually running the animation. */
	private BlockShooterThread mBlockShooterThread;

    /** A handle to the View in which the game is running. */
    private BlockShooterView mBlockShooterView;

    // the play start button
    private Button mButton;

    // used to hit retry
    private Button mButtonRetry;

    // the window for instructions and such
    private TextView mTextView;

    // game window timer
    private TextView mTimerView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // get handles to the JetView from XML and the JET thread.
        mBlockShooterView = (BlockShooterView)findViewById(R.id.BlockShooterView);
        mBlockShooterThread = mBlockShooterView.getThread();

        // look up the happy shiny button
        mButton = (Button)findViewById(R.id.Button01);
        mButton.setOnClickListener(this);

        mButtonRetry = (Button)findViewById(R.id.Button02);
        mButtonRetry.setOnClickListener(this);

        // set up handles for instruction text and game timer text
        mTextView = (TextView)findViewById(R.id.text);
        mTimerView = (TextView)findViewById(R.id.timer);

        mBlockShooterView.setTimerView(mTimerView);

        mBlockShooterView.SetButtonView(mButtonRetry);

        mBlockShooterView.SetTextView(mTextView);
    }


    /**
     * Handles component interaction
     * 
     * @param v The object which has been clicked
     */
    public void onClick(View v) {
        // this is the first screen
        if (mBlockShooterThread.getGameState() == BlockShooterThread.STATE_START) {
            mButton.setText("PLAY!");
            mTextView.setVisibility(View.VISIBLE);

            mTextView.setText(R.string.helpText);
            mBlockShooterThread.setGameState(BlockShooterThread.STATE_PLAY);

        }
        // we have entered game play, now we about to start running
        else if (mBlockShooterThread.getGameState() == BlockShooterThread.STATE_PLAY) {
            mButton.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.INVISIBLE);
            mTimerView.setVisibility(View.VISIBLE);
            mBlockShooterThread.setGameState(BlockShooterThread.STATE_RUNNING);

        }
        // this is a retry button
        else if (mButtonRetry.equals(v)) {

            mTextView.setText(R.string.helpText);

            mButton.setText("PLAY!");
            mButtonRetry.setVisibility(View.INVISIBLE);
            // mButtonRestart.setVisibility(View.INVISIBLE);

            mTextView.setVisibility(View.VISIBLE);
            mButton.setText("PLAY!");
            mButton.setVisibility(View.VISIBLE);

            mBlockShooterThread.setGameState(BlockShooterThread.STATE_PLAY);

        } else {
            Log.d("JB VIEW", "unknown click " + v.getId());

            Log.d("JB VIEW", "state is  " + mBlockShooterThread.mState);

        }
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, msg);
        } else {
            return mBlockShooterThread.doKeyDown(keyCode, msg);
        }
    }

    /**
     * Standard override for key-up.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return super.onKeyUp(keyCode, msg);
        } else {
            return mBlockShooterThread.doKeyUp(keyCode, msg);
        }
    }
}
