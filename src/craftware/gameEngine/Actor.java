package craftware.gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Actor {
	
	public Actor()
	{
	}
	
	public void SetBitmapResource( BitmapResource _bitmap_array )
	{
		bitmap_array = _bitmap_array;
	}
	
	public void Update()
	{
	}
	
	public void Draw( Canvas canvas )
	{
		Bitmap bitmap = bitmap_array.getBitmap(bitmap_index);
		canvas.drawBitmap( bitmap, x - bitmap.getWidth()/2, y - bitmap.getHeight()/2, null );
	}
	
	public void SetPosition( int _x, int _y )
	{
		x = _x;
		y = _y;
	}
	
	public void SetSize( int _w, int _h )
	{
		w = _w;
		h = _h;
	}
	
	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;
	
	private BitmapResource bitmap_array;
	public int bitmap_index = 0;
}
