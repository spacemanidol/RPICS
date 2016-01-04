package graphics.basicmodels;

import graphics.basicShapes.Point3D;
import graphics.basicShapes.Polygon3D;
import graphics.only3d.World3D;

import java.util.ArrayList;

public class Rectangle extends Model {

	public Rectangle(World3D w, double x, double y, double z, double xs) {
		this(w, x, y, z, xs, xs, xs);
	}

	public Rectangle(World3D w, double x, double y, double z, double xs, double ys, double zs) {
		super(w, x, y, z);
		ArrayList<Point3D> topp = new ArrayList<Point3D>();
		topp.add(new Point3D(x + xs / 2, y + ys / 2, z + zs / 2));
		topp.add(new Point3D(x + xs / 2, y + ys / 2, z - zs / 2));
		topp.add(new Point3D(x - xs / 2, y + ys / 2, z - zs / 2));
		topp.add(new Point3D(x - xs / 2, y + ys / 2, z + zs / 2));
		addPolygon(new Polygon3D(topp));
		ArrayList<Point3D> bottomp = new ArrayList<Point3D>();
		bottomp.add(new Point3D(x + xs / 2, y - ys / 2, z + zs / 2));
		bottomp.add(new Point3D(x + xs / 2, y - ys / 2, z - zs / 2));
		bottomp.add(new Point3D(x - xs / 2, y - ys / 2, z - zs / 2));
		bottomp.add(new Point3D(x - xs / 2, y - ys / 2, z + zs / 2));
		addPolygon(new Polygon3D(bottomp));
		ArrayList<Point3D> leftp = new ArrayList<Point3D>();
		leftp.add(new Point3D(x - xs / 2, y + ys / 2, z + zs / 2));
		leftp.add(new Point3D(x - xs / 2, y + ys / 2, z - zs / 2));
		leftp.add(new Point3D(x - xs / 2, y - ys / 2, z - zs / 2));
		leftp.add(new Point3D(x - xs / 2, y - ys / 2, z + zs / 2));
		addPolygon(new Polygon3D(leftp));
		ArrayList<Point3D> rightp = new ArrayList<Point3D>();
		rightp.add(new Point3D(x + xs / 2, y + ys / 2, z + zs / 2));
		rightp.add(new Point3D(x + xs / 2, y + ys / 2, z - zs / 2));
		rightp.add(new Point3D(x + xs / 2, y - ys / 2, z - zs / 2));
		rightp.add(new Point3D(x + xs / 2, y - ys / 2, z + zs / 2));
		addPolygon(new Polygon3D(rightp));
	}
}
