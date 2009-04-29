package com.joglobj;
import javax.vecmath.*;

public class OBJMaterial
{
	Color3f ambient;
	Color3f diffuse;
	Color3f specular;
	float alpha;
	int texId;
	OBJMaterial()
	{
		texId = -1;
		alpha=1f;
	}
	public Color3f getAmbient() {
		return ambient;
	}
	public void setAmbient(Color3f ambient) {
		this.ambient = ambient;
	}
	public Color3f getDiffuse() {
		return diffuse;
	}
	public void setDiffuse(Color3f diffuse) {
		this.diffuse = diffuse;
	}
	public Color3f getSpecular() {
		return specular;
	}
	public void setSpecular(Color3f specular) {
		this.specular = specular;
	}
	public int getTexId() {
		return texId;
	}
	public void setTexId(int texId) {
		this.texId = texId;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
}

