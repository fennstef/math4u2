package math4u2.view.graph.drawarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.util.HashMap;

import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.view.graph.GraphInterface;

public interface DrawAreaInterface extends MathObject {
	boolean inMouseAction();
	
    void activate1To1Zoom(boolean b);

    Component add(Component b);

    void addGraph(GraphInterface graph);

    void addGraph(String functionName);

    GraphInterface getGraph(String name);

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

    void removeGraph(GraphInterface graph);

    void requestFocus();

    void setAxisColor(Color c);

    void setCursor(Cursor c);

    void setDetail(int i);

    void setFastZoom(boolean b);

    void setGridColor(Color c);

    void setGridMesh(double a, double b);

    void setStroke(int i);

    void register() throws BrokerException;

    void unregisterParameter();

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

    void setTitle(HashMap params);
    String getTitle();

	void setTitle(String text);

	void coordinateSystem(HashMap params);
	
	DrawAreaRegistryInterface getRegistry();
} //DrawAreaInterface
