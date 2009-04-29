package com.joglobj;
import javax.media.opengl.*; 
import javax.media.opengl.glu.GLU;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

class XTexture
{
	String fileName;
	Texture _tex;
	int    _id;

	int _width, _height;

	int[] imgBuffer;

	BufferedImage  _buffer;

	boolean isLoaded;

	XTexture()
	{
		_tex = null;
		isLoaded = false;
	}

	XTexture( String fName ,GL gl, GLU glu)
	{
		fileName = fName;
		_tex = null;
		_buffer = null;

		isLoaded = false;

		load( fName ,gl,glu);
	}

	void bind(GL gl, GLU glu, GLUT glut)
	{
		_tex.bind();
		gl.glBindTexture( GL.GL_TEXTURE_2D, _id );
	}

	void enable(GL gl)
	{
		gl.glEnable(GL.GL_TEXTURE_2D);			// Enable Texture Mapping
		_tex.bind();
		_tex.enable();
	}

	void disable(GL gl)
	{
		_tex.disable();
		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		gl.glDisable(GL.GL_TEXTURE_2D);
	}

	void setWrap()
	{
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
	}

	void setClamp()
	{
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP );
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP );
	}

	void setClampToEdge()
	{
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE );
		_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE );
	}

	void createGL(GL gl, GLU glu, GLUT glut, int w, int h )
	{
		_width = w;
		_height = h;

		int[] id = { 0 };

		// Creating texture.
		gl.glGenTextures( 1, id, 0 );
		_id = id[0];
		System.out.println( "texture created: " + _id );

		gl.glBindTexture( GL.GL_TEXTURE_2D, _id );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );

		gl.glTexImage2D( GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, _width, _height, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, null );
		//    gl._glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, GL.GL_RGBA, _width, _height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null );
	}

	void create( int w, int h )
	{
		_width = w;
		_height = h;
		_buffer = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );//_PRE );
		_tex = TextureIO.newTexture( _buffer, false );
		_id = _tex.getTextureObject();
		System.out.println( "texture created: " + _id );

		//      _tex.setTexParameteri( GL.GL_TEXTURE_WRAP_R, GL.GL_REPEAT );
		_tex.setTexParameterf( GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
		_tex.setTexParameterf( GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
		_tex.setTexParameterf( GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );//_MIPMAP_LINEAR );
		_tex.setTexParameterf( GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );//_MIPMAP_LINEAR );
	}

	void load( String fName ,GL gl, GLU glu)//, boolean mipmap )  
	{   
		fileName = fName;

		try
		{
			//println( "LOAD TEXTURE START" );

			_tex = TextureIO.newTexture( new File(fileName), true );  //mipmap );
			//println( "AFTER TEX" );

			_id = _tex.getTextureObject();
			//println( "ID: " + _id );

			_width = _tex.getImageWidth();
			_height = _tex.getImageHeight();

			//_tex.setTexParameterf( GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT ); 
			//_tex.setTexParameterf( GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );  
			_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP ); 
			_tex.setTexParameteri( GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP );  
			_tex.setTexParameteri( GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
			_tex.setTexParameteri( GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );//_MIPMAP_LINEAR );

			System.out.println("loading texture: " + fileName + " with id= " + _id );

			isLoaded = true;
		}
		catch( IOException e )
		{
			System.out.println( "*** texture error: " + e );
		}

		//println( "LOAD TEXTURE END" );

		//_id = _tex.getTarget();
		//println( "Texture: '" + fileName + "' with id: '" + _id + "'" );
		//println( "Estimated Memory Size: " + _tex.getEstimatedMemorySize() / 1024 + " KBytes" );

		//return tex;
	}

	
	//I can think of no time when I would use this
/*	void loadPImage( String fName )
	{
		fileName = fName;

		_img = loadImage( fName );
		if( _img == null )
		{
			System.out.println( "couldnt load texture: " + fileName );
			return;
		}      

		_width = _img.width;
		_height = _img.height;

		int[] texId = new int[1];

		//println( "gen tex" );
		vgl._gl.glGenTextures( 1, texId, 0 );
		_id = texId[0];
		//println( "tex id: " + _id );

		//println( "bind" );
		vgl._gl.glBindTexture( GL.GL_TEXTURE_2D, _id );

		//println( "pixelstore" );
		vgl._gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, 1 );
		vgl._gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_GENERATE_MIPMAP, GL.GL_TRUE );

		vgl._gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT ); 
		vgl._gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );  

		//println( "texparameter" );
		vgl._gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR );
		vgl._gl.glTexParameterf( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );

		//println( "teximage2d" );
		//    vgl._gl.glTexImage2D( GL.GL_TEXTURE_2D, 0, 4, _width, _height, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(_img.pixels) ); 
		vgl._glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, 4, _img.width, _img.height, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(_img.pixels) ); 
		//    vgl._glu.gluBuild2DMipmaps( GL.GL_TEXTURE_2D, 4, _img.width, _img.height, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(_img.pixels) ); 

		//_img = null;

		isLoaded = true;
		System.out.println("loading texture: " + fileName + " with id= " + _id );
	}*/

	
	//I can think of no time when I would use this
/*	void update(GL gl)
	{
		//    if( _img != null );
		//      _img.updatePixels();

		//    vgl._gl.glPixelStorei(GL.GL_UNPACK_ROW_LENGTH, _width );
		//    vgl._gl.glPixelStorei( GL.GL_UNPACK_SWAP_BYTES, 1 );
		//    vgl._gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, 4 );

		for( int j=0; j<_height; j++ )
		{
			for( int i=0; i<_width; i++ )
			{
				imgBuffer[i+j*_width] = (_img.pixels[i+j*_width]);
			}
		}

		gl.glBindTexture( GL.GL_TEXTURE_2D, _id );
		gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, 1 );
		gl.glTexSubImage2D( GL.GL_TEXTURE_2D, 0, 0, 0, _width, _height, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(imgBuffer) ); //IntBuffer.wrap(_img.pixels) );
		//    vgl._gl.glTexImage2D( GL.GL_TEXTURE_2D, 0, 4, _width, _height, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, IntBuffer.wrap(imgBuffer) ); //IntBuffer.wrap(_img.pixels) );

		//    vgl._gl.glPixelStorei( GL.GL_UNPACK_SWAP_BYTES, 0 );

		gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
		System.out.println("updated, id= " + _id );
	}*/

	void delete(GL gl)
	{
		int[] texId = { _id };
		try {
			gl.glDeleteTextures( 1, texId, 0 );
		} catch( GLException e )
		{ 
			System.out.println( e );
		}

	//	_img = null;

		_id = 0;

		isLoaded = false;
	}

	int getTarget()
	{
		return _tex.getTarget();
	}

	int getId()
	{
		return _id;
	}

	int getWidth()
	{
		return _width;
	}

	int getHeight()
	{
		return _height;
	}

}


