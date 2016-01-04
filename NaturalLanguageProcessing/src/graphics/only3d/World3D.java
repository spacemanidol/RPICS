package graphics.only3d;

import graphics.basicShapes.Polygon3D;
import graphics.basicmodels.Model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class World3D {
	public ArrayList<Polygon3D> polygons = new ArrayList<Polygon3D>();
	public ArrayList<Model> models = new ArrayList<Model>();
	private int cameraIndex = 0;

	public static int TILE_SIZE = 10;

	public World3D() {
	}

	public void paint(Graphics g, Camera c) {
		for (int i = 0; i < this.models.size(); i++) {
			Model m = (Model) this.models.get(i);
			m.draw(g, c, Color.red);
		}
		for (int i = 0; i < this.polygons.size(); i++) {
			Polygon3D poly = (Polygon3D) this.polygons.get(i);
			poly.draw(g, c, Color.black);
		}
	}

	public void setCameraIndex(int camera) {
		this.cameraIndex = camera;
	}

	public int getCameraIndex() {
		return this.cameraIndex;
	}
}