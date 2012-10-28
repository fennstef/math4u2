package math4u2.view.graph.drawarea;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import math4u2.controller.Broker;
import math4u2.mathematics.affine.MousePosition;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.listview.ViewFactorySingleton;

/**
 * @author Fenn Stefan
 * 
 * Listener, der die aktuellen Mauskoordinaten zu <code>MousePosition</code>
 * schickt.
 * 
 * @see math4u2.mathematics.affine.MousePosition  
 */
public class MouseDisplayListener implements MouseMotionListener {
    
    private Broker broker;
    private DrawAreaInterface da;
    
    public MouseDisplayListener(Broker broker, DrawAreaInterface da){
        this.broker = broker;
        this.da = da;
    }//Konstruktor

    public void mouseDragged(MouseEvent arg0) {
        sendMove(arg0);
    }//mouseDragged

    public void mouseMoved(MouseEvent arg0) {
        sendMove(arg0);
    }//mouseMoved

    public void sendMove(MouseEvent evt) {
    	MousePosition.setBroker(broker);
    	
		//globale Position bestimmen
    	try{
    		Component comp = evt.getComponent();
    		if(!comp.isVisible()) return;
    		Point p = comp.getLocationOnScreen();
    		p.x += evt.getX();
    		p.y += evt.getY();

    		//DrawArea-Position abziehen
    		Point daPoint = ((Component) da).getLocationOnScreen();
    		p.translate(-daPoint.x, -daPoint.y);
    		MousePosition.getInstance(ViewFactorySingleton.getInstance()).setMousePosition(da.xPixToCoord(p.x),
    				da.yPixToCoord(p.y));
    	}catch(Throwable t){
    		ExceptionManager.doError(t);
    	}
	}//sendMove
}//class MouseDisplayListener

