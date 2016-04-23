package phased.game.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import org.joml.Vector3f;

import phased.game.graphics.light.Light;
import phased.game.graphics.model.Model;

public class Scene {
	private static final int MAX_LIGHTS = 4;
	private ArrayList<Model> models;
	private ArrayList<Light> lights;

	private FPSCamera camera;
	
	public Scene(FPSCamera camera) {
		this.models = new ArrayList<Model>();
		this.lights = new ArrayList<Light>();
		this.camera = camera;
	}

	boolean test = true;
	public void render(ShaderProgram shader) {
		if(test) {
			//lights.add(new Light(new Vector3f(0, 0, 0), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.002f)));
			test = false;
		}
		test = true;
		//lights.get(0).getPosition().set(new Vector3f(0, 0, 0));
		//lights.get(0).getColour().set( new Vector3f(1, 1, 1));
		
		//lights.get(1).getColour().set( new Vector3f(0, 1, 0.6f));
		//lights.get(1).getPosition().set(new Vector3f(-3, 20, 0));
		//lights.get(1).setAttenuation(new Vector3f(1, 0.01f, 0.01f));
		//lights.get(1).setPosition(camera.getPosition());
		//System.out.println(camera.getPosition());
		for (int i = 0; i < MAX_LIGHTS; i++) {
			int lightColour = glGetUniformLocation(shader.getID(), "lightColour[" + i + "]");
			int lightPosition = glGetUniformLocation(shader.getID(), "lightPosition[" + i + "]");
			int attenuation = glGetUniformLocation(shader.getID(), "attenuation[" + i + "]");
			
			if (i < lights.size()) {
				Light light = lights.get(i);
				glUniform3f(lightColour, light.getColour().x, light.getColour().y, light.getColour().z);
				glUniform3f(lightPosition, light.getPosition().x, light.getPosition().y, light.getPosition().z);
				glUniform3f(attenuation, light.getAttenuation().x, light.getAttenuation().y, light.getAttenuation().z);
			}else {
				glUniform3f(lightColour, 0, 0, 0);
				glUniform3f(lightPosition, 0, 0, 0);
				glUniform3f(attenuation, 1, 0, 0);
			}
		}
		for (int i = 0; i < models.size(); i++) {
			models.get(i).render(shader);
		}
	}

	public void addModel(Model model) {
		models.add(model);
	}

	public void addModel(String path) {
		Model model = new Model();
		model.loadOBJModel(path);
		models.add(model);
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public void addLight(Vector3f pos, Vector3f col) {
		lights.add(new Light(pos, col));
	}

	public void clearModels() {
		models.clear();
	}
}
