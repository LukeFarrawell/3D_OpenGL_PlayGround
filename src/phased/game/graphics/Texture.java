package phased.game.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

	private final int id;
	private final int width;
	private final int height;

	public Texture(int width, int height, ByteBuffer data) {
		id = glGenTextures();
		this.width = width;
		this.height = height;

		glBindTexture(GL_TEXTURE_2D, id);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
	}
	
	public int getID() {
		return id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public static Texture loadTexture(String path) {
		/* Prepare image buffers */
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		/* Load image */
		stbi_set_flip_vertically_on_load(1);
		ByteBuffer image = stbi_load(path, w, h, comp, 4);
		if (image == null) {
			throw new RuntimeException(
					"Failed to load a texture file!" + System.lineSeparator() + stbi_failure_reason() +"\n path = " + path);
		}

		/* Get width and height of image */
		int width = w.get();
		int height = h.get();

		return new Texture(width, height, image);
	}
}