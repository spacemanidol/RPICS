package graphics.basicmodels;

import graphics.basicShapes.Polygon3D;
import graphics.basicShapes.Vector3D;
import graphics.only3d.Camera;
import graphics.only3d.World3D;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Model extends Vector3D {

	public ArrayList<Polygon3D> polygons;
	World3D w;

	public Model(World3D w) {
		this(w, 0, 0, 0);
	}

	public Model(World3D w, double x, double y, double z) {
		super(x, y, z);
		polygons = new ArrayList<Polygon3D>();
		this.w = w;
		w.models.add(this);
	}

	public void setX(double nx) {
		if (polygons != null) {
			for (int i = 0; i < this.polygons.size(); i++) {
				polygons.get(i).translateX(nx - getX());
			}
		}
		super.setX(nx);
	}

	public void setY(double ny) {
		if (polygons != null) {
			for (int i = 0; i < this.polygons.size(); i++) {
				polygons.get(i).translateY(ny - getY());
			}
		}
		super.setY(ny);
	}

	public void setZ(double nz) {
		if (polygons != null) {
			for (int i = 0; i < this.polygons.size(); i++) {
				polygons.get(i).translateZ(nz - getZ());
			}
		}
		super.setZ(nz);
	}

	public void draw(Graphics g, Camera c, Color color) {
		for (int i = 0; i < polygons.size(); i++) {
			polygons.get(i).getRotated(this, getZenith(), getAzumith()).draw(g, c, Color.black);
		}
	}

	public void rotateZenith(double degrees) {
		this.setZenith(getZenith() + degrees);
	}

	public void rotateAzumith(double degrees) {
		this.setAzimuth(getAzumith() + degrees);
	}

	public void addPolygon(Polygon3D poly) {
		polygons.add(poly);
	}
}
