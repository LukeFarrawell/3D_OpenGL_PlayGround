package phased.game.util;

import java.util.*;

import phased.game.graphics.Texture;

public class Assets {
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture loadTexture(String fileName, String path) {
		Texture texture = textures.get(fileName);
		if(texture == null) {
			System.out.println("loading " + fileName);
			texture = Texture.loadTexture("res/images/" + fileName);
			textures.put(fileName, texture);
		}
		
		return texture;
	}
	
	public static Texture getTexture(String texture) {
		return textures.get(texture);
	}
}
