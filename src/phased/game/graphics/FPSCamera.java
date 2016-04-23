package phased.game.graphics;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.FloatBuffer;

import org.joml.*;
import org.lwjgl.BufferUtils;

public class FPSCamera {
	// Position, Rotation of the camera
	private Matrix4f viewMatrix;
	private Vector3f velocity = new Vector3f(0, 0, 0);
	private Vector3f position = new Vector3f(0, 0, 0);
	private Quaternionf orientation = new Quaternionf(0, 0, 0, 1);

	// Projection data
	private Matrix4f projectionMatrix;
	float fov = 45f;
	float aspectRatio = 16f / 9f;
	float nearPlane = 0.1f;
	float farPlane = 1000f;
	float halfPi = (float) Math.PI / 2f;

	public FPSCamera(FPSCamera camera) {
		viewMatrix = new Matrix4f(camera.getViewMatrix());
		projectionMatrix = new Matrix4f(camera.getProjectionMatrix());
		orientation = camera.getOrientaion();
		position = camera.getPosition();
		velocity = camera.getVelocity();
	}

	public FPSCamera(float fov, float aspectRatio, float nearPlane, float farPlane) {
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;

		createProjMat(fov, aspectRatio, nearPlane, farPlane);
	}

	public FPSCamera() {
		createProjMat(fov, aspectRatio, nearPlane, farPlane);
		moveTo(0, 0.2f, 0);
/*		this.fov = (float) Math.toRadians(45.0f);
		this.aspectRatio = (Display.WIDTH / Display.HEIGHT);
		this.nearPlane = 0.01f;
		this.farPlane = 10000f;
		this.projectionMatrix = new Matrix4f().perspective(fov, aspectRatio, nearPlane, farPlane);
		this.viewMatrix = new Matrix4f();
		
		float range = (float) (Math.tan(fov * 0.5f) * nearPlane);
		float Sx = (2.0f * nearPlane) / (range * aspectRatio + range * aspectRatio);
		float Sy = nearPlane / range;
		float Sz = -(farPlane + nearPlane) / (farPlane - nearPlane);
		float Pz = -(2.0f * farPlane * nearPlane) / (farPlane - nearPlane);
		
		float proj_mat[] = {
				Sx, 0.0f, 0.0f, 0.0f,
				0.0f, Sy, 0.0f, 0.0f,
				0.0f, 0.0f, Sz, -1.0f,
				0.0f, 0.0f, Pz, 0.0f
			};
		
		this.orientation = new Quaternionf();*/
		//this.orientation.rotate((float)Math.toRadians(90), 0f, 0f);
		//projectionMatrix = new Matrix4f().set(proj_mat);
		
		
		//projectionMatrix.lookAt(new Vector3f(1.2f, 1.2f, 1.2f), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f));
	}

	private void createProjMat(float fov, float aspectRatio, float nearPlane, float farPlane) {
		float yScale = (float) (1f / Math.tan(fov / 2f * (float) (Math.PI / 180d)));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;

		projectionMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();

		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		projectionMatrix.m33 = 0;
	}

	public void setFOV(float fov) {
		createProjMat(fov, aspectRatio, nearPlane, farPlane);
	}

	public FPSCamera moveForwards(float delta) {
		velocity.z -= delta;
		return this;
	}

	public FPSCamera moveRight(float delta) {
		velocity.x -= delta;
		return this;
	}

	public FPSCamera moveUp(float delta) {
		velocity.y -= delta;
		return this;
	}

	public FPSCamera moveTo(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		return this;
	}

	public FPSCamera moveTo(Vector3f vector) {
		position = new Vector3f(vector);
		return this;
	}

	public FPSCamera lookUp(float angle) {
		Quaternionf q = new Quaternionf();
		orientation.rotateX(angle, q);
		orientation = q;
		return this;
	}

	public FPSCamera lookRight(float angle) {
		orientation = new Quaternionf().rotateY(angle).mul(orientation);
		return this;
	}

	public FPSCamera roll(float angle) {
		orientation.rotateZ(-angle);
		return this;
	}

	public FPSCamera pitch(float angle) {
		orientation.rotateY(angle);
		return this;
	}

	public FPSCamera yaw(float angle) {
		orientation.rotateX(angle);
		return this;
	}

	public void update() {
		viewMatrix = new Matrix4f();
		orientation.normalize();
		orientation.invert();
		orientation.get(viewMatrix);
		orientation.invert();
		orientation.transform(velocity);
		position.add(velocity);
		viewMatrix.translate(new Vector3f(position).negate());
		velocity.zero();
	}

	public void upload(ShaderProgram shader) {
		int view_mat_location = glGetUniformLocation(shader.getID(), "view");
		int proj_mat_location = glGetUniformLocation(shader.getID(), "proj");

		FloatBuffer view = BufferUtils.createFloatBuffer(16);
		FloatBuffer proj = BufferUtils.createFloatBuffer(16);
		viewMatrix.get(view);
		projectionMatrix.get(proj);

		glUniformMatrix4fv(view_mat_location, false, view);
		glUniformMatrix4fv(proj_mat_location, false, proj);
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public Vector3f getVelocity() {
		return new Vector3f(velocity);
	}

	public Quaternionf getOrientaion() {
		return new Quaternionf(orientation);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
}
