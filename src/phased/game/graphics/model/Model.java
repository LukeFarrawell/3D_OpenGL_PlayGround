package phased.game.graphics.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.joml.*;
import org.lwjgl.BufferUtils;

import com.jumi.JUMILoader;
import com.jumi.scene.JUMIScene;
import com.jumi.scene.objects.*;

import phased.game.graphics.*;

public class Model {
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

	private Matrix4f model;
	
	private ArrayList<Mesh> meshs;
	
	public Model() {
		meshs = new ArrayList<Mesh>();
		vertices = new ArrayList<Vertex>();
		textureCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
		faces = new ArrayList<Face>();
		model = new Matrix4f();

		//model.translate(0, 0, -25);
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
		// Create the Element Buffer object
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		// vertices
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);

		// normals
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);

		// uv
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
	}

	public void loadOBJModel(String path) {
		JUMIScene scene = JUMILoader.loadModel(path);
		System.out.println(scene.getAllMeshes().length);
		JUMIMesh sampleMesh = scene.getMeshByIndex(0); // Grab the first mesh in the scene

		//System.out.println(sampleMesh);

		JUMIMaterial sampleMaterial = sampleMesh.getMaterialByIndex(0); // Grab the first material defined on the mesh

		//System.out.println(sampleMaterial);

		//JUMITexture sampleTexture = sampleMaterial.getDiffuseTexture(); // Let's look closer at this material's defined diffuse texture
		//System.out.println(sampleTexture);

		for (JUMIMesh a : scene.getAllMeshes()) {
		   meshs.add(new Mesh(a));
			
			//a.triangulate(); // We want to render with GL_TRIANGLES
		   //a.setScale(0.1f); // Shrink it a little
		   //a.flipUVY(); // Our model uses DX coordinates so need to flip for OpenGL
		}

		// Begin loading geometry

		/*float[] modelVerts = sampleMesh.getVertices();
		float[] modelUVs = sampleMesh.getUVs();
		float[] modelNormals = sampleMesh.getNormals();
		int[] modelIndices = sampleMesh.getVertexIndices();
		
		System.out.println("Vertice count = " + modelVerts.length);
		float[] vertex = new float[(modelVerts.length / 3) * 8];
		
		for(int i = 0; i < (modelVerts.length / 3); i++) {
			vertex[(i * 8) + 0] = modelVerts[(i * 3) + 0];
			vertex[(i * 8) + 1] = modelVerts[(i * 3) + 1];
			vertex[(i * 8) + 2] = modelVerts[(i * 3) + 2];
			
		}
		
		for(int i = 0; i < (modelNormals.length / 3); i++) {
			vertex[(i * 8) + 3] = modelNormals[(i * 3) + 0];
			vertex[(i * 8) + 4] = modelNormals[(i * 3) + 1];
			vertex[(i * 8) + 5] = modelNormals[(i * 3) + 2];
		}
		
		for(int i = 0; i < (modelUVs.length / 2); i++) {
			vertex[(i * 8) + 6] = modelUVs[(i * 2) + 0];
			vertex[(i * 8) + 7] = modelUVs[(i * 2) + 1];
		}
		
		loadFloat(vertex, modelIndices);*/
	}
	
	public void loadOBJModelWorksBetter(String path) {
		ModelData data = OBJLoader.loadOBJ(path);
		
		loadFloat(data.getInterleaved(), data.getIndices());
	}
	
	public void loadOBJModelOld(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		} catch (FileNotFoundException e) {
			System.err.println("Cant load model: " + path);
			e.printStackTrace();
			System.exit(1);
		}

		String line = "";
		int lineNumber = 0;
		try {

			while ((line = reader.readLine()) != null) {
				lineNumber++;
				line = line.replaceAll("  ", " ");
				if (line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					vertices.add(new Vertex(vertices.size(), new Vector3f(Float.valueOf(currentLine[1].trim()),
							Float.valueOf(currentLine[2].trim()), Float.valueOf(currentLine[3].trim()))));
				} else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					textureCoords.add(
							new Vector2f(Float.valueOf(currentLine[1].trim()), Float.valueOf(currentLine[2].trim())));
				} else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					normals.add(new Vector3f(Float.valueOf(currentLine[1].trim()), Float.valueOf(currentLine[2].trim()),
							Float.valueOf(currentLine[3].trim())));
				} else if (line.startsWith("f ")) {
					parseFace(line);
					/*String[] currentLine = line.split(" ");
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");
					processVertex(vertex1, vertices, indices);
					processVertex(vertex2, vertices, indices);
					processVertex(vertex3, vertices, indices);*/
				}
			}
		} catch (IOException e) {
			System.err.println("Line error at: " + lineNumber);
			System.out.println("Line error was: " + line);
			e.printStackTrace();
		}

		vertex = new float[vertices.size() * 8];
		for (int i = 0; i < vertices.size(); i++) {
			vertex[(i * 8) + 0] = vertices.get(i).position.x;
			vertex[(i * 8) + 1] = vertices.get(i).position.y;
			vertex[(i * 8) + 2] = vertices.get(i).position.z;
		}

		// normal = new float[normals.size() * 3];
		for (int i = 0; i < normals.size(); i++) {
			vertex[(i * 8) + 3] = normals.get(i).x;
			vertex[(i * 8) + 4] = normals.get(i).y;
			vertex[(i * 8) + 5] = normals.get(i).z;
		}

		// uv = new float[textureCoords.size() * 2];
		for (int i = 0; i < textureCoords.size(); i++) {
			vertex[(i * 8) + 6] = textureCoords.get(i).x;
			vertex[(i * 8) + 7] = textureCoords.get(i).y;
		}

		index = new int[indices.size()];
		for (int i = 0; i < index.length; i++) {
			index[i] = indices.get(i);
		}

		for (int i = 0; i < 5; i++) {
			String str = "";
			for (int y = 0; y < 8; y++) {
				str += vertex[(i * 8) + y] + ", ";
			}
			System.out.println(str);
		}
		
		loadFloat(vertex, index);

		/*vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertex.length);
		verticesBuffer.put(vertex).flip();

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(index.length);
		indicesBuffer.put(index).flip();

		// Create the Element Buffer object
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

		// vertices
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);

		// normals
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);

		// uv
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);*/
	}

	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
		} else {
			dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
					vertices);
		}
	}
	
	private void parseFace(String line) {
		String[] values = line.split(" ");
		for (int i = 1; i < 4; i++) {
			if (i < values.length) {
				if (values[i].contains("/")) {
					String[] value = values[i].split("/");
					int index = Math.abs(Integer.parseInt(value[0])) - 1;
					Vertex currentVertex = vertices.get(index);

					int textureIndex = (value[1].length() != 0) ? Integer.parseInt(value[1]) - 1 : -1;
					int normalIndex = (value[2].length() != 0) ? Integer.parseInt(value[2]) - 1 : -1;

					if (!currentVertex.isSet()) {
						currentVertex.setTextureIndex(textureIndex);
						currentVertex.setNormalIndex(normalIndex);
						indices.add(index);
					} else {
						dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
					}
				} else {
					indices.add(Integer.parseInt(values[i]));
				}
			} else {
				System.out.println("test");
				break;
			}
		}
	}

	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex,
			List<Integer> indices, List<Vertex> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
			} else {
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
			}
		}
	}

	public void render(ShaderProgram shader) {

		FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
		model.get(modelBuffer);
		int modelLocation = glGetUniformLocation(shader.getID(), "model");
		glUniformMatrix4fv(modelLocation, false, modelBuffer);
		for(int i = 0; i < meshs.size(); i++) {
			meshs.get(i).render(shader);
		}
		
	}
	
	public void render(ShaderProgram shader, Texture texture) {
		FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
		model.get(modelBuffer);
		int modelLocation = glGetUniformLocation(shader.getID(), "model");
		glUniformMatrix4fv(modelLocation, false, modelBuffer);
		for(int i = 0; i < meshs.size(); i++) {
			meshs.get(i).setTexture(texture);
			meshs.get(i).render(shader);
		}
	}
	
	public void renderNoBind(ShaderProgram shader, Texture texture, FloatBuffer buffer) {
		int modelLocation = glGetUniformLocation(shader.getID(), "model");
		glUniformMatrix4fv(modelLocation, false, buffer);
		for(int i = 0; i < meshs.size(); i++) {
			meshs.get(i).renderNoBind(shader, texture);
		}
	}
	
	public Matrix4f getModel() {
		return model;
	}
	
	public Mesh getMesh(int i) {
		return meshs.get(i);
	}

	public void setModel(Matrix4f model) {
		this.model = model;
	}
	
}
