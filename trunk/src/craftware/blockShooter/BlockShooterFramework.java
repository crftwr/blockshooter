package craftware.blockShooter;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import craftware.gameEngine.Actor;
import craftware.gameEngine.BitmapResource;
import craftware.gameEngine.Framework;
import craftware.gameEngine.Sound;

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
		
		@Override
		public void update()
		{
			x += speed_x;
			y += speed_y;
			
			if(x<0){ speed_x = Math.abs(speed_x); }
			if(x>virtual_surface_width){ speed_x = -Math.abs(speed_x); }
			if(y<0){ speed_y = Math.abs(speed_y); }
			if(y>virtual_surface_height){ speed_y = -Math.abs(speed_y); sound_taihou.play(); }
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
		
		test_bitmap = new BitmapResource(res, test_bitmap_id_list);
		
		sound_taihou = new Sound( context, R.raw.taihou );
		
		for( int i=0 ; i<10 ; ++i )
		{
			TestActor actor = new TestActor();
			actor.setBitmapResource(test_bitmap);
			appendActor(actor);
		}
		
		setClearMode( true, Color.BLUE );
	}
	
	@Override
	public void update()
	{
		if( input.key.up_pressed )
		{
			TestActor actor = new TestActor();
			actor.setBitmapResource(test_bitmap);
			appendActor(actor);
		}
		
		super.update();
	}
	
	public Random random;
	
	private BitmapResource test_bitmap;
	private Sound sound_taihou;
}
