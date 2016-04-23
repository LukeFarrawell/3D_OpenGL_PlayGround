package phased.game.graphics.model;
public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;
	private float[] interleaved;

	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getIndices() {
		return indices;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}
	
	public void interleave() {
		interleaved = new float[vertices.length * 8];

		System.out.println(interleaved.length);
		System.out.println(vertices.length);
		for(int i = 0; i < (vertices.length / 3); i++) {
			interleaved[(i * 8) + 0] = vertices[(i * 3) + 0];
			interleaved[(i * 8) + 1] = vertices[(i * 3) + 1];
			interleaved[(i * 8) + 2] = vertices[(i * 3) + 2];
			
		}
		
		for(int i = 0; i < (normals.length / 3); i++) {
			interleaved[(i * 8) + 3] = normals[(i * 3) + 0];
			interleaved[(i * 8) + 4] = normals[(i * 3) + 1];
			interleaved[(i * 8) + 5] = normals[(i * 3) + 2];
		}
		
		for(int i = 0; i < (textureCoords.length / 2); i++) {
			interleaved[(i * 8) + 6] = textureCoords[(i * 2) + 0];
			interleaved[(i * 8) + 7] = textureCoords[(i * 2) + 1];
		}
	}

	public float[] getInterleaved() {
		return interleaved;
	}

	public void setInterleaved(float[] interleaved) {
		this.interleaved = interleaved;
	}

}