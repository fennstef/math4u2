package math4u2.exercises.scripting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;

/**
 * Es werden Animationen durchgeführt. Diese werden jeweils von einem
 * EActionContainer aufgerufen.
 * 
 * @author Erich Seifert, Fenn Stefan
 */
public class EActionAnimation extends EActionItem {
    /** Referenz zum Broker */
    private Broker broker;

    /** Zeitfunktion */
    private Function timeFunc;

    /** Name der Zeitfunktion */
    private String timeFuncId;

    /** Name des Parameters */
    private String attribName;

    /** Methode die zum setzen des Parameters aufgerufen wird */
    private Method setMethod;

    private String getMethodName;

    /** Methode die zum holen des Parameters aufgerufen wird */
    private Method getMethod;

    private String setMethodName;

    /** Parameter wird von fromVal bis zu toVal gestellt */
    private float fromVal, toVal;

    /** Endzeit der Animation in Millisekunden */
    private double endTime;

    /** Zeitpunkt, wann die Animation beginnen soll in Millisekunden */
    private double beginTime;

    /** Aktuelle Abspielzeit in Millisekunden */
    private double currentTime;

    /** Normierte Zeit in der die Animation läuft. Immer von 0..1 */
    private double realTime;

    private double time;

    /** aktueller Stellwert des Parameters */
    private double attribVal;

    /** vorherige aktuelle Zeit in Millisekunden */
    private long lastTime;

    /** aktuelle Zeit in Millisekunden */
    private long curTime;

    /** Zeit die verstrichen wäre, wenn die speedRate==1.0 sein würde */
    private long imaginaryTime;

    /** globale Geschwindigkeit der Animation */
    private static double speedRate = 1.0;

    public EActionAnimation(String objName, String getMethodName,
            String setMethodName, String attributeName, String timeFunction,
            Broker broker) {
        super(objName);
        this.broker = broker;
        this.attribName = attributeName.trim();
        this.timeFuncId = timeFunction;
        this.getMethodName = getMethodName;
        this.setMethodName = setMethodName;
    } //Konstruktor

    /**
     * Es werden vor der Animation alle Werte initialisiert. Außerdem wird die
     * aktuelle Zeit und evtl. der Startwert des Parameters geholt.
     */
    public void beforeFirstRun(long timeMillis) {
        super.beforeFirstRun(timeMillis);
        MathObject attrib = broker.getMathObject(attribName);
        if (attrib != null)
            animationParameters.add(attrib);

        if (obj != null) {
			Class modClass = obj.getClass();
			try {
				getMethod = modClass.getMethod(getMethodName,
						new Class[] { String.class });
			} catch (NoSuchMethodException ex) {
				ExceptionManager.doError("Methode nicht gefunden: "
						+ getMethodName, ex);
			}//catch

			try {
				setMethod = modClass.getMethod(setMethodName, new Class[] {
						String.class, Float.TYPE });
			} catch (NoSuchMethodException ex) {
				ExceptionManager.doError("Methode nicht gefunden: "
						+ setMethodName, ex);
			}//catch
		}//if

        if (timeFuncId != null && !timeFuncId.equals("t")) {
            MathObject mo = null;

            int tfNum = 0;
            Object tf = null;
            do {
                try {
                    tf = broker.getObject("TimeFunction" + tfNum);
                } catch (BrokerException e1) {
                    ExceptionManager.doError("TimeFunction nicht gefunden", e1);
                }
                tfNum++;
            } while (tf != null);

            String timeFuncstr = "TimeFunction" + tfNum + "(t):=" + timeFuncId;
            try {
                timeFuncId = parser.NEWParseDefinition(timeFuncstr, true, broker);
                mo = broker.getObject(timeFuncId);
            } catch (ParseException e) {
                ExceptionManager.doError("Fehler in der Definition: "+timeFuncstr,e);
            } catch (BrokerException e) {
                ExceptionManager.doError("Zeitfunktion nicht gefunden",e);
            }//catch

            if (mo instanceof Function) {
                timeFunc = (Function) mo;
            }//if
        }//if

        currentTime = 0.0;
        endTime = super.getEnd() * 1000.0 / speedRate;
        beginTime = super.getBegin() * 1000.0 / speedRate;
        realTime = 0.0;
        time = 0.0;
        attribVal = 0.0;

        if (Float.isNaN(fromVal)) {
            try {
                fromVal = ((Float) getAttribute()).floatValue();
            } catch (IllegalAccessException ex) {
                ExceptionManager.doError("Unerlaubter Zugriff",ex);
            } catch (InvocationTargetException ex) {
                ExceptionManager.doError("Ausführungsfehler",ex);
            }//catch
        }//if

        lastTime = timeMillis;
        curTime = lastTime;
        imaginaryTime = lastTime;
    }//beforeFirstRun

    /**
     * Nach der Animation wird die evtl. vorhandene Zeitfunktion gelöscht
     */
    public void afterLastRun(long timeMillis) {
        super.afterLastRun(timeMillis);
        try {
            setAttribute(new Float((float) toVal));
        } catch (IllegalAccessException ex) {
            ExceptionManager.doError("Unerlaubter Zugriff",ex);
        } catch (InvocationTargetException ex) {
            ExceptionManager.doError("Ausführungsfehler",ex);
        }//catch

        if (timeFunc != null) {
            try {
                broker.deleteObjectByKey(timeFuncId);
            } catch (BrokerException e) {
                ExceptionManager.doError("Objekt "+timeFuncId+" konnte nicht gelöscht werden.",e);
            } //catch
        } //if
    }//afterLastRun

    public void doRun(long timeMillis) {    	
        super.doRun(timeMillis);        
        if(BreakActionSingelton.getInstance().isBeforeBreak()) return;
        curTime = timeMillis;
        endTime = super.getEnd() * 1000.0;
        beginTime = super.getBegin() * 1000.0;
        double deltaT = (curTime - lastTime) * speedRate;
        currentTime = (double) (imaginaryTime - getStartTime() + deltaT)
                - beginTime;
        lastTime = curTime;
        imaginaryTime += deltaT;

        realTime = (currentTime / (double) endTime);
        if (realTime < 0 || realTime > 1.0) {
            return;
        } //if

        try {
            time = getTime(realTime);
        } catch (MathException e1) {
            ExceptionManager.doError("Auswertungsfehler",e1);
            return;
        }//catch
        
        attribVal = (1.0 - time) * fromVal + time * toVal;        
        try {
            setAttribute(new Float((float) attribVal));
        } catch (IllegalAccessException ex) {
            ExceptionManager.doError("Unerlaubter Zugriff",ex);
        } catch (InvocationTargetException ex) {
            ExceptionManager.doError("Ausführungsfehler",ex);
        }//catch        
    } //doRun

    /**
     * Berechnet die Zeit entweder nach der Zeitfunktion, oder gibt realTime
     * zurück.
     */
    private double getTime(double realTime) throws MathException {
        if (timeFunc != null) {
            return ((DoubleResult) timeFunc
                    .eval(new DoubleResult[] { new ScalarDoubleResult(realTime) }))
                    .evalScalar();
        } else {
            return realTime;
        }//else
    }//getTime

    public static double getSpeedRate() {
        return speedRate;
    }//getSpeedRate

    public static void setSpeedRate(double rate) {
        speedRate = rate;
    } //setSpeedRate

    public String getObjectName() {
        return objName;
    } //getObjectName

    protected String getAnimationName() {
        return objName + "." + attribName + " animation";
    } //getAnimationName

    public void setFrom(float from) {
        fromVal = from;
    } //setFrom

    public void setTo(float to) {
        toVal = to;
    } //setTo

    private Object getAttribute() throws IllegalAccessException,
            InvocationTargetException {
        return getMethod.invoke(obj, new Object[] { attribName });
    } //getAttribute

    private void setAttribute(Object value) throws IllegalAccessException,
            InvocationTargetException {
        setMethod.invoke(obj, new Object[] { attribName, value });
    } //setAttribute

    public String toString() {
        return "animation(name=" + attribName + "; to=" + toVal + ")";
    }//toString

    /**
     * Berechnung des Beginns in Abhängigkeit der Speedrate
     */
    public float getBegin() {
        double t = (float) (super.getBegin() * 1000.0 / speedRate);
        return (float) t / 1000.0f;
    }//getBegin

    /**
     * Berechnung der Endzeit in Abhängigkeit der Speedrate
     */
    public float getEnd() {
        // realTime läuft von 0 bis 1.
        // Es wird die Restzeit = ende * (1-realTime) berechnet.
        // Es wird die aktuell verstrichene Zeit hinzuaddiert.
        double t = super.getEnd() * 1000.0 * (1 - realTime)
                + (curTime - getStartTime());
        return (float) t / 1000.0f;
    }//getEnd

	@Override public boolean hasBreakAction() {
		return false;
	}
} //EActionAnimation
