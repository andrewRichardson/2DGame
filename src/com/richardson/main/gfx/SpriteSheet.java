package com.richardson.main.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private int[] pixels;
	private final int SIZE, PADDING, WIDTH;
	
	public SpriteSheet(BufferedImage source, int spriteSize, int padding){
		pixels = Graphics.convertTo2D(source);
		SIZE = spriteSize;
		PADDING = padding;
		WIDTH = source.getWidth();
	}
	
	public int[] getSprite(int index){
		int[] sprite = new int[SIZE*SIZE];
		int x = index%(WIDTH/(SIZE+PADDING));
		int y = index/(WIDTH/(SIZE+PADDING));
		int i = 0;
		for(int yy = y*(SIZE+PADDING); yy < SIZE+y*(SIZE+PADDING); yy++){
			for(int xx = x*(SIZE+PADDING); xx < SIZE+x*(SIZE+PADDING); xx++){
				sprite[i++] = pixels[xx + yy*WIDTH];
			}
		}
		return sprite;
	}
	
	public int[] getSprite(int x, int y){
		int[] sprite = new int[SIZE*SIZE];
		int i = 0;
		for(int yy = y*(SIZE+PADDING); yy < SIZE+y*(SIZE+PADDING); yy++){
			for(int xx = x*(SIZE+PADDING); xx < SIZE+x*(SIZE+PADDING); xx++){
				sprite[i++] = pixels[xx + yy*WIDTH];
			}
		}
		return sprite;
	}
	
	public int getSize(){
		return SIZE;
	}
	
}
