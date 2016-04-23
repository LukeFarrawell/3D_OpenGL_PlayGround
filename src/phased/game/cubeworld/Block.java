package phased.game.cubeworld;

import phased.game.graphics.Texture;

public class Block {
	private Texture texture;
	private int x, y, z;

	private int textureLocationX, textureLocationY;

	public Block(int textureLocationX, int textureLocationY, int x, int y, int z) {
		this.textureLocationX = textureLocationX;
		this.textureLocationY = textureLocationY;
		//this.texture = texture;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float[] getVertices() {
		float srcX = textureLocationX * 16;
		float srcY = textureLocationY * 16;
		float srcWidth = 16;
		float srcHeight = 16;

		float u = srcX / 256;
		float v = srcY / 256;
		float u2 = (srcX + srcWidth) / 256;
		float v2 = (srcY + srcHeight) / 256;
		float[] data = {
				x - 1f, 	y,	 z,		 	0.0f, 	1.0f, 	0.0f, 		u, v, 1.0f, 1.0f, 1.0f, 
				x,	 	y - 1f,	 z,		 	1.0f, 	0.0f, 	0.0f, 		u, v, 1.0f, 1.0f, 1.0f, 
				x,		y - 1f,	 z - 1f,	0.0f, 	-1.0f,	0.0f,		u2, v, 1.0f, 1.0f, 1.0f, 
				x,	 	y,	 z - 1f,		0.0f, 	1.0f, 	0.0f, 		u2, v2, 1.0f, 1.0f, 1.0f,
				
				x,	 	y,	 z - 1f,		0.0f, 	0.0f, 	-1.0f,		u, v2, 1.0f, 1.0f, 1.0f, 
				x,	 	y - 1f,	 z - 1f, 	1.0f, 	0.0f, 	0.0f, 		u2, v, 1.0f, 1.0f, 1.0f, 
				x,	 	y - 1f,	 z - 1f, 	0.0f, 	0.0f, 	-1.0f,		u, v, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y,	 z, 			-1.0f,	0.0f, 	0.0f,		u2, v2, 1.0f, 1.0f, 1.0f, 
				
				x - 1f,	 y,	 z, 			0.0f, 	0.0f, 	1.0f, 		u, v2, 1.0f, 1.0f, 1.0f, 
				x,		 y,	 z - 1f, 		1.0f, 	0.0f, 	0.0f, 		u2, v2, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y - 1f,	 z, 	0.0f, 	0.0f, 	1.0f, 		u, v, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y - 1f,	 z, 	0.0f, 	-1.0f,	0.0f,		u, v2, 1.0f, 1.0f, 1.0f, 
				
				x - 1f,	 y - 1f,	 z, 	-1.0f,	0.0f, 	0.0f,		u2, v, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y,	 z - 1f, 		0.0f, 	0.0f, 	-1.0f,		u2, v2, 1.0f, 1.0f, 1.0f, 
				x,		 y,	 z, 			0.0f, 	0.0f, 	1.0f, 		u2, v2, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y - 1f, z - 1f,	-1.0f,	0.0f, 	0.0f,		u, v, 1.0f, 1.0f, 1.0f, 
				
				x,		 y - 1f, z, 		0.0f, 	0.0f, 	1.0f, 		u2, v, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y,	 z - 1f, 		0.0f, 	1.0f, 	0.0f, 		u, v2, 1.0f, 1.0f, 1.0f, 
				x,		 y,	 z, 			0.0f, 	1.0f, 	0.0f, 		u2, v, 1.0f, 1.0f, 1.0f, 
				x,		y,	 z, 			1.0f, 	0.0f, 	0.0f, 		u, v2, 1.0f, 1.0f, 1.0f, 
				
				x - 1f,	 y,	 z - 1f,		-1.0f,	0.0f, 	0.0f,		u, v2, 1.0f, 1.0f, 1.0f, 
				x,		 y - 1f, z, 		0.0f, 	-1.0f,	0.0f,		u2, v2, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y - 1f, z - 1f,	0.0f, 	0.0f, 	-1.0f,		u2, v, 1.0f, 1.0f, 1.0f, 
				x - 1f,	 y - 1f, z - 1f,	0.0f, 	-1.0f,	0.0f,		u, v, 1.0f, 1.0f, 1.0f
		};
		return data;
	}

	public int[] getIndices(int index) {
		int elements[] = {
				17 + index, 0 + index, 18 + index, 
				17 + index, 18 + index, 3 + index, 
				
				20 + index, 15 + index, 12 + index, 
				20 + index, 12 + index, 7 + index, 
				
				19 + index, 1 + index, 5 + index, 
				19 + index, 5 + index, 9 + index, 
				
				4 + index, 6 + index, 22 + index, 
				4 + index, 22 + index, 13 + index, 
				
				8 + index, 10 + index, 16 + index, 
				8 + index, 16 + index, 14 + index, 
				
				11 + index, 23 + index, 2 + index, 
				11 + index, 2 + index, 21 + index
				//index, index + 1, index + 2,
				//index + 2, index + 3, index
		};

		return elements;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
