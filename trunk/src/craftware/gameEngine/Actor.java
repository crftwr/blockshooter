package craftware.gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Actor {
	
	public Actor()
	{
	}
	
	public void setBitmapResource( BitmapResource _bitmap_resource )
	{
		bitmap_resource = _bitmap_resource;
	}
	
	public void update()
	{
	}
	
	public void draw( Canvas canvas )
	{
		Bitmap bitmap = bitmap_resource.getBitmap(bitmap_index);
		canvas.drawBitmap( bitmap, x - bitmap.getWidth()/2, y - bitmap.getHeight()/2, null );
	}
	
	public void setPosition( int _x, int _y )
	{
		x = _x;
		y = _y;
	}
	
	public void setSize( int _w, int _h )
	{
		w = _w;
		h = _h;
	}
	
	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;
	
	private BitmapResource bitmap_resource;
	public int bitmap_index = 0;
}
