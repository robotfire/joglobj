
import com.joglobj.*;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.j3d.loaders.Scene;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.GLUT;

import javax.swing.*;
import javax.media.opengl.*;
import java.awt.*;
import java.awt.event.*;


/**!
 * Class for the window the scene is seen in
 * @author Jesse Fish
 *
 */
public class ObjLoaderProt extends JFrame{
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;

	FPSAnimator animator;
	
	boolean fullscreen = false;
	boolean displayChanged = false;
	
	GraphicsEnvironment ge=null;
	GraphicsDevice gd=null;
	GraphicsDevice myDevice;
	public DisplayMode dm, dm_old;

	public static OBJScene scene;
	
	public ObjLoaderProt()
	{
		super();
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		// Save old displaymode and get new one to play with.
		dm_old = gd.getDisplayMode();
		dm = dm_old;
		myDevice=gd;
		
		GLCapabilities capabilities=new GLCapabilities();
		GLCanvas drawArea=new GLCanvas(capabilities);
		
		animator=new FPSAnimator(drawArea,60);
		drawArea.addGLEventListener(new Refresher());
		add(drawArea);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		animator.start();
		this.setLocationRelativeTo(null); // Center the frame
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		this.setVisible(true);

		//this.setLocation(300, 300);
	}
	
	public void start()
	{
		animator.start();
	}

	public void stop()
	{
		animator.stop();
	}

	public static void main(String args[])
	{
		ObjLoaderProt frame = new ObjLoaderProt();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Refresher implements GLEventListener
{
	private GLU glu;
	private GLUT glut;
	public void display(GLAutoDrawable gLDrawable) {
		// TODO Auto-generated method stub
		final GL gl = gLDrawable.getGL();

		//draw the scene
		gl.glDrawBuffer(GL.GL_FRONT);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		glu.gluLookAt(0,0, 100, 0, 0, 0, 0, 1, 0);

		gl.glPushMatrix();
		ObjLoaderProt.scene.draw(gl,glu,glut);
		gl.glPopMatrix();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	public void init(GLAutoDrawable gLDrawable) {
		GL gl = gLDrawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);              // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background		
		
		glu = new GLU();
		glut=new GLUT();

		//load this shit
		ObjLoaderProt.scene = new OBJScene();
		ObjLoaderProt.scene.load( "data/board.obj" ,gl,glu);
	//	ObjLoaderProt.scene.get_obj().getMaterials().
	}

	public void reshape(GLAutoDrawable gLDrawable,int x, int y, int width, int height) {

		final GL gl = gLDrawable.getGL();
		if (height <= 0) // avoid a divide by zero error!
		{height = 1;}
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 200.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}

