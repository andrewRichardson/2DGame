package com.richardson.main.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Graphics {

	public Graphics(){
		
	}
	
	public static int[] convertTo2D(BufferedImage image) {
	
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    final int width = image.getWidth();
	    final int height = image.getHeight();
	    final boolean hasAlphaChannel = image.getAlphaRaster() != null;
	
	    int[] result = new int[height*width];
	    if (hasAlphaChannel) {
	    	final int pixelLength = 4;
	        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	        	int argb = 0;
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row*width + col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	        }
	    } else {
	        final int pixelLength = 3;
	        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            //argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row*width + col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	        }
	    }
	
	    return result;
	}
	
	public static int[] overlayAlphaImage(int[] source, int[] overlay, int imageWidth, int imageHeight, int screenWidth){
		int i = 0;
		for(int y = 0; y < imageHeight; y++){
			for(int x = 0; x < imageWidth; x++){
				int alpha = (overlay[i] >> 24) & 0xff;
				int red = (overlay[i] >> 16) & 0xff;
			    int green = (overlay[i] >> 8) & 0xff;
			    int blue = (overlay[i]) & 0xff;
			    
			    int alpha2 = (source[x + y*screenWidth] >> 24) & 0xff;
				int red2 = (source[x + y*screenWidth] >> 16) & 0xff;
			    int green2 = (source[x + y*screenWidth] >> 8) & 0xff;
			    int blue2 = (source[x + y*screenWidth]) & 0xff;
			    
			    
				int r = alpha*red + alpha2*red2;
				int g = alpha*green + alpha2*green2;
				int b = alpha*blue + alpha2*blue2;
				int pixel = r;
				pixel = (pixel << 8) + g;
				pixel = (pixel << 8) + b;
				i++;
				source[x + y*screenWidth] = pixel;
			}
		}
		return source;
	}
	
}
