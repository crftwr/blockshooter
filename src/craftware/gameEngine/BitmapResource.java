package craftware.gameEngine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapResource {
	
	public BitmapResource( Resources res, int id_list[] )
	{
		bitmap_list = new Bitmap[id_list.length];
	
		for(int i=0 ; i<id_list.length ; ++i)
		{
			bitmap_list[i] = BitmapFactory.decodeResource( res, id_list[i] );
		}
	}
	
	public Bitmap getBitmap(int bitmap_index)
	{
		return bitmap_list[bitmap_index];
	}

	public Bitmap bitmap_list[];

}
