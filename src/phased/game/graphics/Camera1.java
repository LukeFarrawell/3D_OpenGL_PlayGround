package phased.game.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import phased.game.util.Input;

public class Camera1 {
	private float near = 0.1f;
	private float far = 100.0f;
	private float fov = (float) Math.toRadians(67.0f);
	private float aspect = Display.WIDTH / Display.HEIGHT;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	private Vector3f position;
	
	float cam_speed = 1.0f; // 1 unit per second
	float cam_yaw_speed = 10.0f; // 10 degrees per second
	float cam_pos[] = {0.0f, 0.0f, 2.0f}; // don't start at zero, or we will be too close
	float cam_yaw = 0.0f; // y-rotation in degrees
	
	float cam_x = 0.0f;
	
	public Camera1() {
		//projection matrix
		float range = (float) (Math.tan(fov * 0.5f) * near);
		float Sx = (2.0f * near) / (range * aspect + range * aspect);
		float Sy = near / range;
		float Sz = -(far + near) / (far - near);
		float Pz = -(2.0f * far * near) / (far - near);
		
		float proj_mat[] = {
				Sx, 0.0f, 0.0f, 0.0f,
				0.0f, Sy, 0.0f, 0.0f,
				0.0f, 0.0f, Sz, -1.0f,
				0.0f, 0.0f, Pz, 0.0f
			};
		
		float test[] = {
				1, 2, 3, 4,
				5, 6, 7, 8,
				9, 10, 11, 12,
				13, 14, 15, 16
		};
		projectionMatrix = new Matrix4f().set(proj_mat);
		projectionMatrix.lookAt(new Vector3f(1.2f, 1.2f, 1.2f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f));
		
		//view matrix
		position = new Vector3f();
		Matrix4f T = new Matrix4f().identity().translate(position);
		Matrix4f R = new Matrix4f().identity().rotateY(-cam_yaw);
		viewMatrix = R.mul(T);
	}
	
	public void upload(ShaderProgram shader) {
		
		int view_mat_location = glGetUniformLocation (shader.getID(), "view");
		int proj_mat_location = glGetUniformLocation (shader.getID(), "proj");
		
		FloatBuffer view = BufferUtils.createFloatBuffer(16);
		FloatBuffer proj = BufferUtils.createFloatBuffer(16);
		viewMatrix.get(view);
		projectionMatrix.get(proj);
		
		glUniformMatrix4fv(view_mat_location, false, view);
		glUniformMatrix4fv(proj_mat_location, false, proj);
	}
	
	public void update() {
		float speed = 0.05f;
		boolean moved = false;
		
		if(Input.isKeyDown(GLFW_KEY_W)) {
			position.y -=  speed;
			moved = true;
		}
		if(Input.isKeyDown(GLFW_KEY_S)) {
			position.y += cam_speed * speed;
			moved = true;
		}
		if(Input.isKeyDown(GLFW_KEY_A)) {
			position.x -= cam_speed * speed;
			moved = true;
		}
		if(Input.isKeyDown(GLFW_KEY_D)) {
			position.x += cam_speed * speed;
			moved = true;
		}
		if(Input.isKeyDown(GLFW_KEY_UP)) {
			cam_yaw += speed;
			moved = true;
		}
		if(Input.isKeyDown(GLFW_KEY_DOWN)) {
			cam_yaw -= speed;
			moved = true;
		}
		
		if(Input.isKeyDown(GLFW_KEY_LEFT)) {
			cam_x += speed;
			moved = true;
		}
		
		if(Input.isKeyDown(GLFW_KEY_RIGHT)) {
			cam_x -= speed;
			moved = true;
		}
		
		if(moved) {
			//Matrix4f T = new Matrix4f().identity().translate(position);
			//Matrix4f R = new Matrix4f().identity().rotateY(cam_yaw);
			//viewMatrix = R.mul(T);
			//viewMatrix = new Matrix4f();
			viewMatrix.rotate(cam_x, new Vector3f(0.0f, 0.0f, 1.0f));
			viewMatrix.rotate(cam_yaw, new Vector3f(0.0f, 1.0f, 0.0f));
			viewMatrix.translate(position);
			
			viewMatrix.translate(position.x, position.y, position.z);
			
			cam_yaw = 0.0f; // y-rotation in degrees
			position = new Vector3f();
			cam_x = 0.0f;
		}
	}
}
