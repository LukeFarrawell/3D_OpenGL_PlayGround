package phased.game.cubeworld;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import net.royawesome.jlibnoise.module.source.Perlin;
import phased.game.Game;
import phased.game.cubeworld.Block;
import phased.game.graphics.ShaderProgram;
import phased.game.util.Assets;

public class Chunk {
	private int x, y, z;
	public static final int SIZE = 16;
	private Matrix4f model;

	public ArrayList<Block> blocks;
	
	public Block[][][] blocksArray;

	boolean isPrepared = false;

	private FloatBuffer vertex;
	private int index = 0;
	private int indiceCount = 0;

	private IntBuffer indices;

	private int vaoID;
	private int vboID;
	private int eboID;
	
	int abc = 0;
	
	public Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		blocksArray = new Block[World.chunkSize + 1][World.chunkSize + 1][World.chunkSize + 1];
		//this.blocks = new Block[SIZE][SIZE][SIZE];
		blocks = new ArrayList<Block>();
		//vertex = BufferUtils.createFloatBuffer((SIZE * SIZE * SIZE) * (11 * 24) * 2);
		//indices = BufferUtils.createIntBuffer((SIZE * SIZE * SIZE) * (3 * 12) * 2);
		
		//vertex = BufferUtils.createFloatBuffer(1148928);
		//indices = BufferUtils.createIntBuffer(156672);

		model = new Matrix4f();
	}

	public void addBlock(Block block) {
		blocksArray[block.getX() % World.chunkSize][block.getY() % World.chunkSize][block.getZ() % World.chunkSize] = block;
		//abc++;
		//System.out.println(block.getX() / 16 + ", " + block.getY() / 16 + ", " + block.getZ() / 16 + ", " + block);
		
/*		Game.count++;
		abc++;
		blocks.add(block);
		//blocks[block.getX()][block.getY()][block.getZ()] = block;
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		float data[] = block.getVertices();
		int elements[] = block.getIndices(index);
		vertex.put(data);
		indices.put(elements);
		
		index += (data.length / 11);
		indiceCount += elements.length;*/
	}
	
	public void optimise() {
		int xSize = 17;
		int ySize = 17;
		int zSize = 17;
		int test = 0;
		for (int x = 0; x < World.chunkSize; x++) {
			for (int y = 0; y < World.chunkSize; y++) {
				for (int z = 0; z < World.chunkSize; z++) {
					
					if (x == 0 || z == 0) {
						if (blocksArray[x][y][z] != null) {
							blocks.add(blocksArray[x][y][z]);
						}
						continue;
					}

					Block up = null;
					Block down = null;
					Block one = null;
					Block two = null;
					Block three = null;
					Block four = null;

					if (x != 0) {
						four = blocksArray[x - 1][y][z];
					}

					if (x != xSize - 1) {
						two = blocksArray[x + 1][y][z];
					}

					if (z != 0) {
						three = blocksArray[x][y][z - 1];
					}

					if (z != zSize - 1) {
						one = blocksArray[x][y][z + 1];
					}

					if (y != 0) {
						up = blocksArray[x][y - 1][z];
					}

					if (y != ySize - 1) {
						down = blocksArray[x][y + 1][z];
					}

					if (up == null || down == null || one == null || two == null || three == null || four == null) {
						Block block = blocksArray[x][y][z];
						if (block != null) {
							blocks.add(block);
						}
					}
				}
			}
		}
	}
	
	public void build() {
		vertex = BufferUtils.createFloatBuffer(blocks.size() * (24 * 11));
		indices = BufferUtils.createIntBuffer(blocks.size() * 36);
		
		for(Block block : blocks)  {
			abc++;
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();
			float data[] = block.getVertices();
			int elements[] = block.getIndices(index);
			vertex.put(data);
			indices.put(elements);
			
			index += (data.length / 11);
			indiceCount += elements.length;
		}
	}
	
	public void prepare() {
		optimise();
		build();
		
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
	}
	
	public void cleanup() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
	}

	public void render(ShaderProgram shader) {
		if (!isPrepared) {
			prepare();
			isPrepared = true;
		}
		Assets.loadTexture("sheet.png", "res/images/sheet.png").bind();
		FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
		model.get(modelBuffer);
		int modelLocation = glGetUniformLocation(shader.getID(), "model");
		glUniformMatrix4fv(modelLocation, false, modelBuffer);

		glBindVertexArray(vaoID);

		glDrawElements(GL_TRIANGLES, indiceCount, GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);
	}
	
	public void generate(Perlin noise) {
		for(int xx = (x * World.chunkSize); x < (x * World.chunkSize) + World.chunkSize; x++) {
			for(int zz = (z); z < (z * World.chunkSize) + World.chunkSize; z++) {
				int landHeight = (int) (Math.floor(noise.GetValue(x, 0, z) * 20 + 20));
				int dirtHeight = (int) (Math.floor((hash(x, z) >> 8 & 0xf) / 15f * 3) + 1);
				for(int yy = (y * World.chunkSize); yy < (y * World.chunkSize) + World.chunkSize; yy++) {
					if (y == 0) { //stone
						addBlock(new Block(2, 15, xx, yy, zz));
					} else if (y > landHeight)
						continue;
					if (y == landHeight) { //grass
						addBlock(new Block(1, 15, xx, yy, zz));
					} else if (y >= landHeight - dirtHeight) { //dirt
						addBlock(new Block(0, 15, xx, yy, zz));
					} else { //stone
						addBlock(new Block(2, 15, xx, yy, zz));
					}
				}
			}
		}
	}
	
	private static int hash(int x, int y) {
		int hash = x * 3422543 ^ y ^ 85151 * 432959;
		return hash ^ hash ^ (hash + 324319);
	}
}
