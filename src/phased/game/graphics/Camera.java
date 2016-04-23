package phased.game.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import phased.game.util.Input;

public class Camera {
	private Matrix4f mProj;
	private Matrix4f mView;

	private Quaternionf rotation;

	private Vector3f position;
	private Vector3f forward;
	private Vector3f right;
	private Vector3f up;

	private float angleX;

	public Camera() {
		mProj = new Matrix4f().perspective((float) Math.toRadians(90.0f), (Display.WIDTH / Display.HEIGHT), 0.0f,
				100000.0f);
		mView = new Matrix4f();

		position = new Vector3f(0, 0, 1);
		rotation = new Quaternionf();

		forward = new Vector3f();
		right = new Vector3f();
		up = new Vector3f();
	}
	
	public void update() {
		float speed = 0.05f;
		boolean moved = false;
		rotation = new Quaternionf();
		if(Input.isKeyDown(GLFW_KEY_W)) {
			forward(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_S)) {
			backward(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_A)) {
			left(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_D)) {
			right(speed);
		}
		
		if(Input.isKeyDown(GLFW_KEY_SPACE)) {
			up(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			down(speed);
		}
		if(Input.isKeyDown(GLFW_KEY_UP)) {
			rotation.rotate(speed, 0, 0);
			mView.rotate(rotation);
		}
		if(Input.isKeyDown(GLFW_KEY_DOWN)) {
			rotation.rotate(speed, 0, 0);
			mView.rotate(rotation);
		}
		if(Input.isKeyDown(GLFW_KEY_LEFT)) {
			rotation.rotate(0, 0, -speed);
			mView.rotate(rotation);
		}
		if(Input.isKeyDown(GLFW_KEY_RIGHT)) {
			rotation.rotate(0, 0, speed);
			mView.rotate(rotation);
		}
	}

	public void upload(ShaderProgram shader) {
		int view_mat_location = glGetUniformLocation(shader.getID(), "view");
		int proj_mat_location = glGetUniformLocation(shader.getID(), "proj");

		FloatBuffer view = BufferUtils.createFloatBuffer(16);
		FloatBuffer proj = BufferUtils.createFloatBuffer(16);
		mView.get(view);
		mProj.get(proj);

		glUniformMatrix4fv(view_mat_location, false, view);
		glUniformMatrix4fv(proj_mat_location, false, proj);
	}

	public void lookAt(Vector3f point) {
		lookAt(point, up.normalize());
	}

	public void lookAt(Vector3f point, Vector3f up) {
		// rotation = Transforms.createLookAtQuaternion(position, point, up,
		// rotation);
	}

	public void forward(float amount) {
		mView.translate(0, -amount, 0);
	}

	public void backward(float amount) {
		mView.translate(0, amount, 0);
	}

	public void left(float amount) {
		mView.translate(amount, 0, 0);
	}

	public void right(float amount) {
		mView.translate(-amount, 0, 0);
	}

	public void up(float amount) {
		mView.translate(0, 0, amount);
	}

	public void down(float amount) {
		mView.translate(0, 0, -amount);
	}
}
