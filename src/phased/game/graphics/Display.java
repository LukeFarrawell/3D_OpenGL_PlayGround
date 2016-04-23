package phased.game.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

public class Display {
	public static int WIDTH = 1240;
	public static int HEIGHT = 720;
	public static int centerX = WIDTH / 2;
	public static int centerY = HEIGHT / 2;
	public static final String TITLE = "Testing";
	public static int VSYNC = 0;
	private static GLFWErrorCallback errorCallback;
	private static GLFWKeyCallback keyCallback;
	private long window;

	public Display() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if (glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
					glfwSetWindowShouldClose(window, GL_TRUE);
				}
			}
		});
		
		//glfwSetCursorPosCallback(window, new Input());
		
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(VSYNC);

		// Make the window visible
		glfwShowWindow(window);
		
		//allow openGL calls on the thread
		GL.createCapabilities();
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	public boolean isResized() {
		boolean flag = false;
		
		ByteBuffer x = null, y = null;
		
		glfwGetWindowSize(window, x, y);
		
		return flag;
	}
	
	public long getID() {
		return window;
	}
}
