package phased.game.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class VertexBufferObject {

	private int vaoID;
	private int vboID;
	private int eboID;

	private int size = 1000;

	private FloatBuffer vertex;
	private int index = 0;
	private int indiceCount = 0;
	private IntBuffer indices;
	private Matrix4f model;
	private FloatBuffer modelBuffer;

	public VertexBufferObject(int size) {
		this.size = size;
		vertex = BufferUtils.createFloatBuffer(size * 44);
		indices = BufferUtils.createIntBuffer(size * 6);
		model = new Matrix4f();
	}

	public void prepare() {
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		vertex.flip();
		indices.flip();

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertex, GL_STATIC_DRAW);

		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

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

		vertex = null;
		indices = null;
		
		modelBuffer = BufferUtils.createFloatBuffer(16);
		model.get(modelBuffer);
	}
	
	public void render(ShaderProgram shader) {
		int modelLocation = glGetUniformLocation(shader.getID(), "model");
		glUniformMatrix4fv(modelLocation, false, modelBuffer);

		glBindVertexArray(vaoID);

		glDrawElements(GL_TRIANGLES, indiceCount, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}

	public void addData(float[] newVertices, int[] newIndices) {
		vertex.put(newVertices);
		indices.put(newIndices);
	}
}
