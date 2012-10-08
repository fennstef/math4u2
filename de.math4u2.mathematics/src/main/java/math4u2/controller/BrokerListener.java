package math4u2.controller;

import java.util.*;

public interface BrokerListener extends EventListener {

	public void newObjectPublished(BrokerEvent e);

	public void objectDeleted(BrokerEvent e);

}//class BrokerListener
