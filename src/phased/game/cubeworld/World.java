package phased.game.cubeworld;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;

import java.util.*;

import org.joml.*;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;
import phased.game.Game;
import phased.game.graphics.*;
import phased.game.graphics.light.Light;
import phased.game.graphics.model.Model;
import phased.game.util.*;

public class World {
	private int seed = 123134;
	private final int MAX_LIGHTS = 4;

	private ArrayList<Block> blocksToRender;
	private Model model;

	private Random random = new Random();

	private ArrayList<Light> lights;

	private FPSCamera camera;

	private Block[][][] blocks;
	public static int xSize = 32, ySize = 4, zSize = 32;
	
	public static int chunkSize = 16;

	private Chunk[][][] chunks;
	
	private Perlin noise;

	public World(FPSCamera camera) {
		blocksToRender = new ArrayList<Block>();
		blocks = new Block[xSize][ySize][zSize];
		chunks = new Chunk[xSize][ySize][zSize];

		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				for(int z = 0; z < zSize; z++) {
					chunks[x][y][z] = new Chunk(x, y, z);
				}
			}
		}

		this.model = new Model();
		//this.model.loadOBJModel("res/models/cube.obj");
		this.lights = new ArrayList<Light>();
		this.camera = camera;
		
		noise = new Perlin();
		noise.setFrequency(0.01);
		noise.setLacunarity(2.0);
		noise.setNoiseQuality(NoiseQuality.BEST);
		noise.setOctaveCount(8);
		noise.setPersistence(0.5);
		noise.setSeed(random.nextInt(Integer.MAX_VALUE));
		
		generate();

		//Assets.loadTexture("clayGrey.png", "res/images/clayGrey.png");
		//Assets.loadTexture("clayGreen.png", "res/images/clayGreen.png");
		//Assets.loadTexture("clayBrown.png", "res/images/clayBrown.png");

		//model.getMesh(0).bind();
	}

	public void generate() {
		Random random = new Random();
		Perlin noise = new Perlin();
		noise.setFrequency(0.01);
		noise.setLacunarity(2.0);
		noise.setNoiseQuality(NoiseQuality.BEST);
		noise.setOctaveCount(8);
		noise.setPersistence(0.5);
		noise.setSeed(random.nextInt(Integer.MAX_VALUE));

		for(int xChunk = 0; xChunk < xSize; xChunk++) {
			for(int zChunk = 0; zChunk < zSize; zChunk++) {
				for(int yChunk = 0; yChunk < ySize; yChunk++) {
					
					for(int x = (xChunk * chunkSize); x < (xChunk * chunkSize) + chunkSize; x++) {
						for(int z = (zChunk * chunkSize); z < (zChunk * chunkSize) + chunkSize; z++) {
							int landHeight = (int) (Math.floor(noise.GetValue(x, 0, z) * 20 + 20));
							int dirtHeight = (int) (Math.floor((hash(x, z) >> 8 & 0xf) / 15f * 3) + 1);

							for(int y = (yChunk * chunkSize); y < (yChunk * chunkSize) + chunkSize; y++) {
								if (y == 0) { //stone
									chunks[xChunk][yChunk][zChunk].addBlock(new Block(2, 15, x, y, z));
								} else if (y > landHeight)
									continue;
								if (y == landHeight) { //grass
									chunks[xChunk][yChunk][zChunk].addBlock(new Block(1, 15, x, y, z));
								} else if (y >= landHeight - dirtHeight) { //dirt
									chunks[xChunk][yChunk][zChunk].addBlock(new Block(0, 15, x, y, z));
								} else { //stone
									chunks[xChunk][yChunk][zChunk].addBlock(new Block(2, 15, x, y, z));
								}
							}
						}
					}
				}
			}
		}
	}

	private static int hash(int x, int y) {
		int hash = x * 3422543 ^ y ^ 85151 * 432959;
		return hash ^ hash ^ (hash + 324319);
	}

	boolean enter = true;

	public void render(ShaderProgram shader) {
		long start = System.nanoTime();
		for (int i = 0; i < MAX_LIGHTS; i++) {
			int lightColour = glGetUniformLocation(shader.getID(), "lightColour[" + i + "]");
			int lightPosition = glGetUniformLocation(shader.getID(), "lightPosition[" + i + "]");
			int attenuation = glGetUniformLocation(shader.getID(), "attenuation[" + i + "]");

			if (i < lights.size()) {
				Light light = lights.get(i);
				glUniform3f(lightColour, light.getColour().x, light.getColour().y, light.getColour().z);
				glUniform3f(lightPosition, light.getPosition().x, light.getPosition().y, light.getPosition().z);
				glUniform3f(attenuation, light.getAttenuation().x, light.getAttenuation().y, light.getAttenuation().z);
			} else {
				glUniform3f(lightColour, 0, 0, 0);
				glUniform3f(lightPosition, 0, 0, 0);
				glUniform3f(attenuation, 1, 0, 0);
			}
		}

		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				for(int z = 0; z < zSize; z++) {
					chunks[x][y][z].render(shader);
				}
			}
		}
		long end = System.nanoTime();
		
		System.out.println((end - start) / 1000000);

	}
	
	public void destroy() {
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				for(int z = 0; z < zSize; z++) {
					chunks[x][y][z].cleanup();
				}
			}
		}
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public void addLight(Vector3f pos, Vector3f col) {
		lights.add(new Light(pos, col));
	}
}
