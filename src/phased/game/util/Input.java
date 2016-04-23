package phased.game.util;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import phased.game.graphics.Display;

public class Input extends GLFWCursorPosCallback{
	private static long windowID;
	
	public static double mouseX, mouseY;

	public static void init(long windowID) {
		Input.windowID = windowID;
	}

	public static boolean isKeyDown(int key) {
		return glfwGetKey(windowID, key) == GL_TRUE;
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		Input.mouseX = xpos;
		Input.mouseY = ypos;
	}
	
	public static void setMouse(float x, float y) {
		glfwSetCursorPos(windowID, x, y);
	}
	
	public static void centerMouse() {
		glfwSetCursorPos(windowID, Display.WIDTH / 2, Display.HEIGHT / 2);
	}
}
