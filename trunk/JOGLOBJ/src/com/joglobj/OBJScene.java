package com.joglobj;


import java.util.ArrayList;
import com.obj.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.vecmath.*;

import com.sun.opengl.util.*;

/*
	Modified for pure JAVA-JOGL use by Jesse Fish

  Basic object file scene manager/loader.

  obj importer by Fabien Sanglard ( http://fabiensanglard.net/ )
  changed and compiled for processing&jogl by Victor Martins ( www.pixelnerve.com/v )

 This Java OBJ Loader support:
 * Groups (scene management)
 * Vertex, Normal, Texture coordinates
 * MTL (material) references.

This Java OBJ Loader DOES NOT support:
 * Relative vertex references.
 * Anything other than GL_TRIANGLE and GL_QUAD polygons
 */

public class OBJScene
{
	ArrayList <Vector3f>_vertexList;
	ArrayList<Vector3f> _normalList;
	ArrayList <Vector3f>_texcoordList;
	ArrayList <OBJMesh>_meshList;

	private WavefrontObject _obj;

	public WavefrontObject get_obj() {
		return _obj;
	}
	
	public ArrayList<OBJMesh> get_meshList() {
		return _meshList;
	}

	public OBJScene()
	{
		_vertexList = new ArrayList<Vector3f>();
		_normalList = new ArrayList<Vector3f>();
		_texcoordList = new ArrayList<Vector3f>();
		
		_meshList = new ArrayList<OBJMesh>();
	}

	public void load( String name,GL gl, GLU glu )
	{
		load( name, 1, 1, 1 ,gl,glu);
	}

	public void load( String name, float s ,GL gl, GLU glu)
	{
		load( name, s, s, s ,gl,glu);
	}

	public void load( String name, float sx, float sy, float sz ,GL gl, GLU glu)
	{
		WavefrontObject obj = new WavefrontObject( name, sx, sy, sz );  

		ArrayList <Group>groups = obj.getGroups();
		for( int gi=0; gi<groups.size(); gi++ )
		{
			Group g = (Group)groups.get( gi );
			Material gm = g.getMaterial();

			OBJMesh mesh = new OBJMesh();
			mesh.setName( g.getName() );

			mesh.material.ambient = new Color3f( gm.getKa().getX(), gm.getKa().getY(), gm.getKa().getZ() );
			mesh.material.diffuse = new Color3f( gm.getKd().getX(), gm.getKd().getY(), gm.getKd().getZ() );
			mesh.material.specular = new Color3f( gm.getKs().getX(), gm.getKs().getY(), gm.getKs().getZ() );

			if( gm.texName != null && gm.texName.length() > 0 )
			{
				XTexture tex = new XTexture( gm.texName ,gl,glu);
				tex.setWrap();
				mesh.material.texId = tex.getId();
			}

			for( int fi=0; fi<g.getFaces().size(); fi++ )
			{
				Face f = g.getFaces().get( fi );
				int[] idx = f.vertIndices;
				int[] nidx = f.normIndices;
				int[] tidx = f.texIndices;

				OBJFace face = new OBJFace();
				face.a = idx[0];
				face.b = idx[1];
				face.c = idx[2];
				face.na = nidx[0];
				face.nb = nidx[1];
				face.nc = nidx[2];
				face.ta = tidx[0];
				face.tb = tidx[1];
				face.tc = tidx[2];

				mesh.addFace( face );
			}

			for( int vi=0; vi<obj.getVertices().size(); vi++ )
			{
				Vertex v = (Vertex)obj.getVertices().get( vi );
				_vertexList.add( new Vector3f(v.getX(), v.getY(), v.getZ()) );
			}

			for( int vi=0; vi<obj.getNormals().size(); vi++ )
			{
				Vertex v = (Vertex)obj.getNormals().get( vi );
				_normalList.add( new Vector3f(v.getX(), v.getY(), v.getZ()) );
			}

			for( int vi=0; vi<obj.getTextures().size(); vi++ )
			{
				TextureCoordinate tc = (TextureCoordinate)obj.getTextures().get( vi );
				_texcoordList.add( new Vector3f( tc.getU(), tc.getV(), tc.getW()) );
			}

			/*for( int vi=0; vi<g.indices.size(); vi++ )
			{
				int tc = (int)g.indices.get( vi );
				mesh.addVertex( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
			}

			for( int vi=0; vi<g.vertices.size(); vi++ )
			{
				Vertex tc = (Vertex)g.vertices.get( vi );
				mesh.addVertex( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
			}

			for( int vi=0; vi<g.normals.size(); vi++ )
			{
				Vertex tc = (Vertex)g.normals.get( vi );
				mesh.addNormal( new Vector3( tc.getX(), tc.getY(), tc.getZ()) );
			}

			for( int vi=0; vi<g.texcoords.size(); vi++ )
			{
				TextureCoordinate tc = (TextureCoordinate)g.texcoords.get( vi );
				mesh.addTexCoord( new Vector3( tc.getU(), tc.getV(), tc.getW()) );
			}*/

			// Finally add mesh to scene
			addMesh( mesh );
		}
	}

	public void addMesh( OBJMesh mesh )
	{
		_meshList.add( mesh );
	}

	public void draw(GL gl, GLU glu, GLUT glut)
	{
		Vector3f n1, n2, n3;
		Vector3f p1, p2, p3;
		Vector3f tc1=null, tc2=null, tc3=null;

		// Render all scene
		for( int i=0; i<_meshList.size(); i++ )
		{
			OBJMesh m = (OBJMesh)_meshList.get( i );

			// If current material has texture, bind it
			if( m.material.texId > 0 )
			{
				//	gl.glDisable( GL.GL_TEXTURE_2D );
				//	gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
				gl.glEnable( GL.GL_TEXTURE_2D );
				gl.glBindTexture( GL.GL_TEXTURE_2D, m.material.texId );
			}
			else
			{
				gl.glDisable( GL.GL_TEXTURE_2D );
				gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
			}

			gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, new float[]{m.material.ambient.x, m.material.ambient.y, m.material.ambient.z, m.material.alpha}, 0 ); 
			gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, new float[]{m.material.diffuse.x, m.material.diffuse.y, m.material.diffuse.z, m.material.alpha}, 0 ); 
			gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[]{m.material.specular.x, m.material.specular.y, m.material.specular.z, m.material.alpha}, 0 ); 
			gl.glMateriali( GL.GL_FRONT_AND_BACK,GL.GL_SHININESS, 128 );

			// render triangles.. this is too basic. should be optimized
			gl.glBegin( GL.GL_TRIANGLES );
			gl.glColor4f( m.material.diffuse.x, m.material.diffuse.y, m.material.diffuse.z, m.material.alpha ); 
			boolean has_texture=false;
			if(_texcoordList!=null&&_texcoordList.size()>0)
			{
				has_texture=true;
			}
			for( int fi=0; fi<m.faceList.size(); fi++ )
			{
				OBJFace f = (OBJFace)m.faceList.get( fi );

				p1 = (Vector3f)_vertexList.get(f.a);
				p2 = (Vector3f)_vertexList.get(f.b);
				p3 = (Vector3f)_vertexList.get(f.c);
				n1 = (Vector3f)_normalList.get(f.na);
				n2 = (Vector3f)_normalList.get(f.nb);
				n3 = (Vector3f)_normalList.get(f.nc);

				//these should only be drawn if there are texture 2d coordinates
				if(has_texture){
					tc1 = (Vector3f)_texcoordList.get(f.ta);
					tc2 = (Vector3f)_texcoordList.get(f.tb);
					tc3 = (Vector3f)_texcoordList.get(f.tc);
				}

				gl.glNormal3f( n1.x, n1.y, n1.z );
				if(has_texture){
					gl.glTexCoord2f( tc1.x, tc1.y );
				}
				gl.glVertex3f( p1.x, p1.y, p1.z );    

				gl.glNormal3f( n2.x, n2.y, n2.z );
				if(has_texture){
					gl.glTexCoord2f( tc2.x, tc2.y );
				}
				gl.glVertex3f( p2.x, p2.y, p2.z );

				gl.glNormal3f( n3.x, n3.y, n3.z );
				if(has_texture){
					gl.glTexCoord2f( tc3.x, tc3.y );
				}
				gl.glVertex3f( p3.x, p3.y, p3.z );
			}
			gl.glEnd();
		}
	}

	/*  void debug()
  {
    for( int i=0; i<_meshList.size(); i++ )
    {
      Mesh m = (Mesh)_meshList.get(i);

      writeString( "Mesh name: " + m.name );
      writeString( "texcoordsize: " + _texcoordList.size() );
      writeString( "normalsize: " + _normalList.size() );
      writeString( "vertexsize: " + _vertexList.size() );
      writeString( "facesize: " + m.faceList.size() );

      for( int vi=0; vi<m.vertexList.size(); vi++ )
      {
        Vector3f v = (Vector3f)m.vertexList.get(vi);
        float x = v.x;
        float y = v.y;
        float z = v.z;
        //writeString( "Vertex  x: " + x + ", y: " + y + ", z: " + z );
      }

      for( int vi=0; vi<m.faceList.size(); vi++ )
      {
        OBJFace f = (OBJFace)m.faceList.get(vi);
        int a = f.a;
        int b = f.b;
        int c = f.c;
        //writeString( "Face  a: " + a + ", b: " + b + ", c: " + c );
      }
    }
  }*/

}


class OBJFace
{

	int a, b, c;  // vertex
	int na, nb, nc;  // normal
	int ta, tb, tc;  // texcoord

	int matId;

	OBJFace() 
	{
		a = b = c = 0;
	}

	OBJFace( int a, int b, int c ) 
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.matId = -1;
	}

	OBJFace( int a, int b, int c, int matId ) 
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.matId = matId;
	}

}
