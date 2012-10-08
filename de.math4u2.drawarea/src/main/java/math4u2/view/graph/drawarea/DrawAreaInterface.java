package math4u2.view.graph.drawarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;

import math4u2.view.graph.SimpleGraphInterface;


public interface DrawAreaInterface{
	boolean inMouseAction();
	
    void activate1To1Zoom(boolean b);

    Component add(Component b);

    void addGraph(SimpleGraphInterface graph);

    SimpleGraphInterface getGraph(String name);

    void coordinateSystem(double xNewMin, double xNewMax, double yNewMin,
            double yNewMax);

    boolean getActivate1To1Zoom();

    Color getAxisColor();

    int getDetail();

    Color getGridColor();

    double getGridMeshX();

    int getHeight();

    String getName();

    int getStroke();

    int getWidth();

    double getXMax();

    double getXMin();

    double getYMax();

    double getYMin();

    void graphHasChanged();

    boolean isFastZoom();

    void remove(Component b);

    void removeGraph(SimpleGraphInterface graph);

    void requestFocus();

    void setAxisColor(Color c);

    void setCursor(Cursor c);

    void setDetail(int i);

    void setFastZoom(boolean b);

    void setGridColor(Color c);

    void setGridMesh(double a, double b);

    void setStroke(int i);

    int xCoordToPix(double xCoord);

    double xPerPix();

    double xPixToCoord(int xPix);

    int yCoordToPix(double yCoord);

    double yPerPix();

    double yPixToCoord(int yPix);

    boolean containsGraph(Object key);

    void setCursorPosition(Point point);

    void setMakeNoRenew(boolean makeNoRenew);

    boolean isMakeNoRenew();

    String getTitle();

	void setTitle(String text);
} //DrawAreaInterface
