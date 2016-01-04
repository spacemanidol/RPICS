package graphics.basicmodels;

import java.util.ArrayList;

import graphics.basicShapes.Point3D;
import graphics.basicShapes.Polygon3D;
import graphics.only3d.World3D;

public class Line extends Model {

	public Line(World3D w, Point3D one, Point3D two) {
		super(w);
		ArrayList<Point3D> points = new ArrayList<Point3D>();
		points.add(one);
		points.add(two);
		addPolygon((new Polygon3D(points)));
	}

}
