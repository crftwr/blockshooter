package craftware.blockShooter;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;

import craftware.gameEngine.Actor;
import craftware.gameEngine.BitmapResource;
import craftware.gameEngine.Framework;

public class BlockShooterFramework extends Framework
{
	public class TestActor extends Actor
	{
		public TestActor()
		{
			super();
			
			x = random.nextInt() % 100;
			y = random.nextInt() % 100;
			speed_x = random.nextInt() % 10 - 5;
			speed_y = random.nextInt() % 10 - 5;
		}
		
		public void Update()
		{
			x += speed_x;
			y += speed_y;
			
			if(x<0){ speed_x = Math.abs(speed_x); }
			if(x>200){ speed_x = -Math.abs(speed_x); }
			if(y<0){ speed_y = Math.abs(speed_y); }
			if(y>200){ speed_y = -Math.abs(speed_y); }
		}
		
		public int speed_x;
		public int speed_y;
	}
	
	public BlockShooterFramework( Context context )
	{
		random = new Random();
		
		Resources res = context.getResources();
		
		int test_bitmap_id_list[] = {
			R.drawable.asteroid01,
			R.drawable.asteroid02,
			R.drawable.asteroid03,
			R.drawable.asteroid04,
		};
		
		BitmapResource test_bitmap = new BitmapResource(res, test_bitmap_id_list);
		
		for( int i=0 ; i<10 ; ++i )
		{
			TestActor actor = new TestActor();
			actor.SetBitmapResource(test_bitmap);
			AppendActor(actor);
		}
	}
	
	public void Update()
	{
		super.Update();
	}
	
	public Random random;
}