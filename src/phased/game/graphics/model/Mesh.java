package phased.game.graphics.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;
import java.util.ArrayList;

import org.joml.*;
import org.lwjgl.BufferUtils;

import com.jumi.scene.objects.*;

import phased.game.graphics.*;
import phased.game.util.Assets;

public class Mesh {
	private ArrayList<Vertex> vertices;
	private ArrayList<Vector2f> textureCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<Integer> indices;
	private ArrayList<Face> faces;

	private float[] vertex, uv, normal;
	private int[] index;

	private int vaoID;
	private int vboID;
	private int eboID;

	private String textureName;
	private Texture texture;

	private String name;

	public Mesh(JUMIMesh mesh) {
		mesh.triangulate();
		JUMIMaterial material = mesh.getMaterialByIndex(0);
		Vector3f colour = new Vector3f(1, 1, 1);
		name = mesh.name;
		if (material != null) {
			// System.out.println("materla name = " + material);

			colour.x = material.diffuseColor.r;
			colour.y = material.diffuseColor.g;
			colour.z = material.diffuseColor.b;
			if (material.getDiffuseTexture() != null) {
				textureName = material.getDiffuseTexture().fileName;
				texture = Assets.loadTexture(textureName, "res/images/");
			} else {
				textureName = "white.png";
				texture = Assets.getTexture("white.png");
				System.out.println("white for " + mesh.name);
			}
		} else {
			textureName = "white.png";
			texture = Assets.getTexture("white.png");
			System.out.println("white for " + mesh.name);
		}
		texture = Assets.getTexture(textureName);

		float[] modelVerts = mesh.getVertices();
		float[] modelUVs = mesh.getUVs();
		float[] modelNormals = mesh.getNormals();
		int[] modelIndices = mesh.getVertexIndices();
		float[] vertex = new float[(modelVerts.length / 3) * 11];
		// System.out.println("triangles = " + modelVerts.length / 3 + ",
		// texture Coordinates = " + modelUVs.length / 2);
		for (int i = 0; i < (modelVerts.length / 3); i++) {
			// vertices
			vertex[(i * 11) + 0] = modelVerts[(i * 3) + 0];
			vertex[(i * 11) + 1] = modelVerts[(i * 3) + 1];
			vertex[(i * 11) + 2] = modelVerts[(i * 3) + 2];

			// normals
			vertex[(i * 11) + 3] = modelNormals[(i * 3) + 0];
			vertex[(i * 11) + 4] = modelNormals[(i * 3) + 1];
			vertex[(i * 11) + 5] = modelNormals[(i * 3) + 2];

			// uv
			if (i + 1 < modelUVs.length) {
				vertex[(i * 11) + 6] = modelUVs[(i * 2) + 0];
				vertex[(i * 11) + 7] = modelUVs[(i * 2) + 1];
			} else {
				vertex[(i * 11) + 6] = 0;
				vertex[(i * 11) + 7] = 0;
			}

			// rgb
			vertex[(i * 11) + 8] = colour.x;
			vertex[(i * 11) + 9] = colour.y;
			vertex[(i * 11) + 10] = colour.z;
		}

		/*
		 * for (int i = 0; i < (modelNormals.length / 3); i++) { vertex[(i * 8)
		 * + 3] = modelNormals[(i * 3) + 0]; vertex[(i * 8) + 4] =
		 * modelNormals[(i * 3) + 1]; vertex[(i * 8) + 5] = modelNormals[(i * 3)
		 * + 2]; }
		 * 
		 * for (int i = 0; i < (modelUVs.length / 2); i++) { vertex[(i * 8) + 6]
		 * = modelUVs[(i * 2) + 0]; vertex[(i * 8) + 7] = modelUVs[(i * 2) + 1];
		 * }
		 */
/*		for(int i = 0; i < vertex.length; i++) {
			if(i % 11 == 0) {
				System.out.println();
			}
			System.out.print(vertex[i] +", ");
		}
		System.out.println();
		for(int i = 0; i < modelIndices.length; i++) {
			if(i % 3 == 0) {
				System.out.println();
			}
			System.out.print(modelIndices[i] +", ");
		}*/
		loadFloat(vertex, modelIndices);
	}

	public void loadFloat(float[] vertex, int[] indices) {
		this.vertex = vertex;
		this.index = indices;
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertex.length);
		verticesBuffer.put(vertex).flip();

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(index.length);
		indicesBuffer.put(index).flip();

		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		// vertices
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 11 * Float.BYTES, 0);

		// normals
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 11 * Float.BYTES, 3 * Float.BYTES);

		// uv
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 11 * Float.BYTES, 6 * Float.BYTES);

		// rgb
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 11 * Float.BYTES, 8 * Float.BYTES);
	}

	static int id = -1;

	public void render(ShaderProgram shader) {

		glActiveTexture(GL_TEXTURE0);
		// Assets.getTexture("f14fuselage.jpg").bind();
		texture.bind();

		glBindVertexArray(vaoID);

		glDrawElements(GL_TRIANGLES, index.length, GL_UNSIGNED_INT, 0);

		glBindVertexArray(0);
	}

	public void renderNoBind(ShaderProgram shader, Texture newTexture) {

		// glActiveTexture(GL_TEXTURE0);
		if (texture.getID() != newTexture.getID()) {
			this.texture = newTexture;
			texture.bind();

		}
		glDrawElements(GL_TRIANGLES, index.length, GL_UNSIGNED_INT, 0);
	}

	public void bind() {
		glBindVertexArray(vaoID);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
