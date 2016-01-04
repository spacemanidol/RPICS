package graphics.basicmodels;

import graphics.basicShapes.Point3D;
import graphics.basicShapes.Polygon3D;
import graphics.only3d.World3D;

import java.util.ArrayList;

public class PointModel extends Model {

	public PointModel(World3D w, double x, double y, double z) {
		this(w, new Point3D(x, y, z));
	}

	public PointModel(World3D w, Point3D one) {
		super(w, one.getX(), one.getY(), one.getZ());
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(one.clone());
		addPolygon((new Polygon3D(points)));
	}

}
