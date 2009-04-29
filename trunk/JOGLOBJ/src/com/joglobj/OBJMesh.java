package com.joglobj;


import java.util.ArrayList;
import javax.vecmath.*;
public class OBJMesh
{
	String name;
	OBJMaterial material;  
	ArrayList <Vector3f>vertexList;
	ArrayList <Vector3f>texcoordList;
	ArrayList <Vector3f>normalList;
	ArrayList <OBJFace>faceList;

	public OBJMaterial getMaterial() {
		return material;
	}

	public void setMaterial(OBJMaterial material) {
		this.material = material;
	}

	OBJMesh()
	{
		vertexList = new ArrayList<Vector3f>();
		texcoordList = new ArrayList<Vector3f>();
		normalList = new ArrayList<Vector3f>();
		faceList = new ArrayList<OBJFace>();
		material = new OBJMaterial();
	}

	void setName( String n )
	{
		name = n;
	}

	void addVertex( Vector3f v )
	{
		vertexList.add( v );
	}

	void addTexCoord( Vector3f v )
	{
		texcoordList.add( v );
	}

	void addNormal( Vector3f v )
	{
		normalList.add( v );
	}

	// Add faces
	void addFace( OBJFace f )
	{
		faceList.add( f );
	}
	void addFace( int a, int b, int c )
	{
		faceList.add( new OBJFace(a, b, c) );
	}

}
