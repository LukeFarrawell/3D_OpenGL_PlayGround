package phased.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

import phased.game.cubeworld.*;
import phased.game.graphics.*;
import phased.game.graphics.light.Light;
import phased.game.graphics.model.Model;
import phased.game.playground.PlayGround;
import phased.game.util.Input;

public class Game implements Runnable{
	public static int count = 0;
	private Display display;
	private Thread thread;
	private boolean running = true;

	private FPSCamera camera;

	private ShaderProgram shaderProgram;
	private Scene scene;
	
	private World world;
	
	private Model model;
	private Model model2;
	private Texture texture;
	
	private long oldTime;
	
	private Light light;
	
	private Chunk chunk;
	
	private PlayGround playground;
	
	public Game() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		oldTime = System.currentTimeMillis();
		init();

		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				display.setTitle("Game - FPS: " + frames + ", World Size: " + World.xSize + "x"+World.ySize +"x" + World.zSize);
				//System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			if (glfwWindowShouldClose(display.getID()) == GL_TRUE) {
				running = false;
			}
		}		
	}
	
	public void render() {
		
		glClearColor(0.5f, 0.5f, 0.5f, 1);
		//glClearColor(1,1,1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		shaderProgram.bind();
		//texture.bind();
		
		//texture.bind();
		camera.upload(shaderProgram);
		world.render(shaderProgram);
		//playground.render(shaderProgram);
		//scene.render(shaderProgram);
		
		glfwSwapBuffers(display.getID());
		glfwPollEvents();
	}
	boolean pressed = false;
	public void update() {
		float speed = 0.1f;
		float speedTurn = 0.1f;
		
		if(Input.isKeyDown(GLFW_KEY_ENTER)) {
			playground.recreate();
			//world.destroy();
			//world = new World(camera);
			//world.addLight(new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0.00001f, 0.000001f)));
		}
		
		if(Input.isKeyDown(GLFW_KEY_MINUS)) {
			world.destroy();
			World.xSize--;
			World.zSize--;
			world = new World(camera);
			world.addLight(new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1)));
		}
		
		if(Input.isKeyDown(GLFW_KEY_EQUAL)) {
			world.destroy();
			World.xSize++;
			World.zSize++;
			world = new World(camera);
			world.addLight(new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1)));
		}
		
		if(Input.isKeyDown(GLFW_KEY_W)) {
			camera.moveForwards(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_S)) {
			camera.moveForwards(-speed);
		}
		if(Input.isKeyDown(GLFW_KEY_A)) {
			camera.moveRight(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_D)) {
			camera.moveRight(-speed);
		}
		if(Input.isKeyDown(GLFW_KEY_SPACE)) {
			camera.moveUp(-speed);
		}
		if(Input.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			camera.moveUp(speed);
		}
		
		if(Input.isKeyDown(GLFW_KEY_LEFT)) {
			camera.pitch(speedTurn);
		}
		if(Input.isKeyDown(GLFW_KEY_RIGHT)) {
			camera.pitch(-speedTurn);
		}
		
		if(Input.isKeyDown(GLFW_KEY_UP)) {
			camera.lookUp(speedTurn);
		}
		if(Input.isKeyDown(GLFW_KEY_DOWN)) {
			camera.lookUp(-speedTurn);
		}
		
		camera.update();
	}
	
	public void init() {
		display = new Display();
		Input.init(display.getID());
		camera = new FPSCamera();
		
		//texture = Texture.loadTexture("res/images/Wood.png");
		//Assets.loadTexture("sheet.png", "res/images/sheet.png");
		//Assets.loadTexture("white.png", "res/images/white.png");
		shaderProgram = new ShaderProgram();
		shaderProgram.attachVertexShader("res/shaders/lights3.vert");
		shaderProgram.attachFragmentShader("res/shaders/lights3.frag");
		shaderProgram.link();
		//playground = new PlayGround(this, camera);
		
		
		world = new World(camera);
		world.addLight(new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1)));
		
		/*scene = new Scene(camera);
		
		model = new Model();
		model2 = new Model();
		//world = new World(camera);
		
		//model.loadOBJModel("res/models/f14.obj");
		model.getModel().scale(4f);
		//model.getModel().rotateX((float) Math.toRadians(-90));
		model.getModel().translate(0, 0, 0);
		
		//model2.loadOBJModel("res/models/bumblebee.obj");
		model2.getModel().scale(0.001f);
		//model2.getModel().rotateX((float) Math.toRadians(-90));
		
		//model2.loadOBJModel("res/models/stanfordBunny.obj");
		//model.getModel().scale(0.01f);
		//model2.getModel().translate(0, 0, 100);
		//model.getModel().rotateY((float) Math.toRadians(90));
		//model.getModel().rotateX((float) Math.toRadians(-90));
		
		//scene.addModel(model);
		//scene.addModel(model2);
		
		world = new World(camera);
		world.addLight(new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1)));
		
		//scene.addLight(new Light(new Vector3f(74.04f, 10.455f, 71.54f), new Vector3f(0.9f, 0.2f, 0.3f), new Vector3f(1, 0.01f, 0.1f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 11110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 111110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(0, 0, 111110), new Vector3f(0.2f, 0.44f, 1f), new Vector3f(1, 0.01f, 0.02f)));
		//scene.addLight(new Light(new Vector3f(6, 0, 0), new Vector3f(0.3f, 0.9f, 0.4f), new Vector3f(1, 0.01f, 0.002f)));
		//scene.addLight(new Light(new Vector3f(0, 5, 0), new Vector3f(0.6f, 0.24f, 0.35f), new Vector3f(1, 0.01f, 0.002f)));
		
		float vertices[] = { 
			    -0.5f,  0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
			     0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
			     0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f

		};
		int elements[] = {
		        0, 1, 2,
		        2, 3, 0
		    };
		
		Model model3 = new Model();
		model3.loadFloat(vertices, elements);
		model3.getModel().scale(50);*/
		//scene.addModel(model3);
		//model.loadFloat(vertices, elements);
		
	}
}
