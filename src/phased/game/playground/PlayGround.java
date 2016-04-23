package phased.game.playground;

import org.joml.Vector3f;

import phased.game.Game;
import phased.game.graphics.*;
import phased.game.graphics.light.Light;
import phased.game.graphics.model.Model;

public class PlayGround {

	private Scene scene;
	private FPSCamera camera;
	private Game game;
	
	private VertexBufferObject floor;
	
	
	public PlayGround(Game game, FPSCamera camera) {
		this.camera = camera;
		this.game = game;
		scene = new Scene(camera);
		
		Model model = new Model();
		model.loadOBJModel("res/models/stargate/stargate.obj");
		scene.addModel(model);
		
		//scene.addLight(new Light(new Vector3f(74.04f, 10.455f, 71.54f), new Vector3f(0.9f, 0.2f, 0.3f), new Vector3f(1, 0.01f, 0.1f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 11110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 111110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 111110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(6, 0, 0), new Vector3f(0.3f, 0.9f, 0.4f), new Vector3f(1, 0.01f, 0.002f)));
		//scene.addLight(new Light(new Vector3f(0, 5, 0), new Vector3f(0.6f, 0.24f, 0.35f), new Vector3f(1, 0.01f, 0.002f)));
		
		//floor = new VertexBufferObject(10 * 10);
		//createEnvironment();
		//floor(0, 0, 0, 0);
		//floor.prepare();
	}

	public void createEnvironment() {
		Model ground = new Model();
		ground.loadOBJModel("res/models/floor.obj");
		scene.addModel(ground);
	}
	public void recreate() {
		scene = new Scene(camera);
		Model ground = new Model();
		ground.loadOBJModel("res/models/floor.obj");
		scene.addModel(ground);
	}
	public void render(ShaderProgram shader) { 
		//floor.render(shader);
		scene.render(shader);
	}

	public void floor(float x, float y, float z, int count) {
		float[] vertices = {
				x + 1, y, z + 1, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 
				x + 1, y, z - 1, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 
				x, y, z + 1, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 
				x, y, z - 1, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f 
		};
		
		int[] indices = {
				0, 1, 3, 
				2, 0, 3 
		};
		
/*		float[] vertices = {
				x + 1, y, z + 1, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 
				x + 1, y, z - 1, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 
				x, y, 1.0f, z, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 
				x, y, -1.0f, z, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f  
		};
		
		int[] indices = {
				(count * 6) + 0, (count * 6) + 1, (count * 6) + 3, 
				(count * 6) + 2, (count * 6) + 0, (count * 6) + 3,
		};*/
		
		floor.addData(vertices, indices);
	}
}
