package phased.game.graphics.model;

import org.joml.Vector3f;

public class Face {
	public Vector3f vertexIndex;
	public Vector3f normalIndex;
	public Vector3f texcoordIndex;

	public Face() {
		vertexIndex = new Vector3f();
		normalIndex = new Vector3f();
		texcoordIndex = new Vector3f();
	}
	
	public Face(Vector3f vertex, Vector3f normal, Vector3f texCoord) {
		this.vertexIndex = vertex;
		this.normalIndex = normal;
		this.texcoordIndex = texCoord;
	}
}
