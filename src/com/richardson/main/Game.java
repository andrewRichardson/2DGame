package com.richardson.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.richardson.main.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {

	// INIT VARS
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static String title = "2D Game";
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	private int fps, ups;
	private static boolean running = false;
	private boolean showFPS = true;
	private static Thread thread;
	// END INIT VARS

	// GRAPHICS VARS
	private BufferStrategy bs;
	private BufferedImage image;
	private int[] pixels;
	private SpriteSheet spriteSheet;

	public Game() {
		// INIT VARS
		Dimension d = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(d);
		frame = new JFrame(title);
		// END INIT VARS

		// GRAPHICS VARS
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		try {
			BufferedImage bi = ImageIO.read(new File("res/icons.png"));
			spriteSheet = new SpriteSheet(bi, 32, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// END GRAPHICS VARS
	}

	public static void main(String[] args) {
		Game game = new Game();

		System.setProperty("sun.java2d.opengl", "true");

		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.toFront();
		frame.setState(JFrame.NORMAL);
		frame.requestFocus();

		frame.setVisible(true);
		game.start();
	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, "Game Thread");
		thread.start();
	}

	public void run() {
		long oldTime = System.nanoTime();
		long timer = System.currentTimeMillis();

		double ns = 1000000000.0 / 60.0;
		long newTime;
		double delta = 0;

		while (running) {
			newTime = System.nanoTime();
			delta += (double) (newTime - oldTime) / ns;
			oldTime = newTime;
			if (delta >= 1) {
				delta--;
				ups++;
				update();
			}
			render();
			fps++;

			if (System.currentTimeMillis() - timer > 1000) {

				timer += 1000;
				if (showFPS)
					frame.setTitle(title + " | " + fps + " fps " + ups + " ups");
				else
					frame.setTitle(title);
				// System.out.println(ups + " ups, " + fps + " fps");
				fps = 0;
				ups = 0;
			}
		}
		stop();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() {

	}

	public void render() {
		// INIT CODE
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		// END INIT CODE

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		
		drawImage(spriteSheet.getSprite(0), 200, 200, spriteSheet.getSize(), spriteSheet.getSize());
		
		// CLOSING CODE
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g.dispose();
		bs.show();
		// END CLOSING CODE
	}
	
	private void drawImage(int[] image, int x, int y, int width, int height){
		int i = 0;
		for(int yy = y; yy < y+height; yy++){
			for(int xx = x; xx < x+width; xx++){
				if(((image[i] >> 24) & 0xff) == 255)
					pixels[xx + yy*WIDTH] = image[i++];
				else if(((image[i] >> 24) & 0xff) != 0){
					int alpha = (image[i] >> 24) & 0xff;
					int red = (image[i] >> 16) & 0xff;
				    int green = (image[i] >> 8) & 0xff;
				    int blue = (image[i]) & 0xff;
				    
				    int alpha2 = (pixels[xx + yy*WIDTH] >> 24) & 0xff;
					int red2 = (pixels[xx + yy*WIDTH] >> 16) & 0xff;
				    int green2 = (pixels[xx + yy*WIDTH] >> 8) & 0xff;
				    int blue2 = (pixels[xx + yy*WIDTH]) & 0xff;
				    
				    
					int r = alpha*red + alpha2*red2;
					int g = alpha*green + alpha2*green2;
					int b = alpha*blue + alpha2*blue2;
					int pixel = r;
					pixel = (pixel << 8) + g;
					pixel = (pixel << 8) + b;
					i++;
				}else{
					i++;
				}
			}
		}
	}

}
