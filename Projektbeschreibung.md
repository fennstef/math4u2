math4u2 -  Ein Bild sagt mehr als tausend Formeln?
=======

Natürlich nicht!  Aber manchmal  ist es schon sehr hilfreich, wenn die Aussage einer mathematischen Formel oder Idee mit einem prägnanten Bild oder einer Bildsequenz  noch verständlicher gemacht wird.

Das Projekt beschäftigt sich im Kern mit dem Versuch, die Idee eines  bild-gestützten mathematischen Argumentierens in eine virtuelle Umgebung zu transportieren - natürlich sollen sich die Ergebnisse auch als Bausteine in traditionellen Vorlesungen nutzen lassen.

Die Impulse dazu kamen von der Lehre im Fach Mathematik in verschiedenen technischen Studiengängen. Der Einsatz in  diesen Vorlesungen hat die Entwicklung der Funktionalität und die Erstellung der Lektionen entscheidend beeinflusst.

Entstanden ist math4u2:
- Kleine Modelle können mit math4u2 in einer Vorlesung schnell interaktiv aufgebaut und sofort interaktiv "erforscht" werden. Die Ergebnisse können abgespeichert, per Web verteilt und wieder in math4u2 geladen werden.
- Größere Modelle oder Argumentationssequenzen (Lektionen) werden mit Hilfe spezieller XML-Dokumente beschrieben und laufen beim Benutzer dann schrittweise, gegebenenfalls mit Animation, ab. Der Benutzer kann die Modelle jederzeit interaktiv verändern.
- Ein math4u2-Server erlaubt den Abruf eines Inhaltsverzeichnisses und von einzelnen Lektionen direkt vom math4u2-Tool aus.
Das System ist komplett in Java implementiert: Parser(yavacc), mathematischer Kern,  Analyse von Abhängigkeiten so, dass Änderungen nur einmal und nur zu  abhängigen Komponenten propagiert  werden, Darstellung von Formeln und Texten, parsen von XML-Dateien mittels pull-Parser, besondere Funktionen oder Graphiken können via Javassist  direkt in Java implementiert werden. 


**An der aktuell vorliegenden Version 2.3.1 haben mitgearbeitet:**
- Prof. Dr. Max Weiß: Initiative, Koordination, mathematischer Kern, Lektionen
- Stefan Fenn, Dipl. Inf. (FH) : Framework, graphische Oberfläche, Optimierung, Installation
- Michael Lichtenstern, Dipl. Inf (FH) : Webdesign, Serveranbindung
- Erich Seifert, Dipl. Inf (FH) : XML-Konzept, Formelsatz, Swing-Programmierung
- Christoph Beckmann, Dipl. Inf (FH) : Script-Parser, Formelsatz-Parser

**Projekt**:

Start im Herbst 2001, im Zeitraum 2001 bis 2004 gefördert durch das  Programm zur Förderung der Weiterentwicklung von Hochschule und (HWP).

Mathematischer Kern:
=======

Der mathematische Kern deckt mit der üblichen Term-Syntax folgende Gebiete ab:

**Analysis:**

Funktionen mit einer oder mehreren Veränderlichen, Ableitung

**Lineare Algebra:**

Matrizen und Vektoren, Determinante, inverse Matrix, Lösung von linearen Gleichungssystemen mit regulärer Koeffizientenmatrix

**Geometrie:**

Punkt, Strecke, Gerade, Kreis und Winkel, parametrisierte Kurven und Kurvenzüge.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image01.jpg?raw=true)

**Numerik:**

Fourier-Trafo

Listen und vor allem Folgen ermöglichen die kompakte Formulierung bekannter iterativer Lösungsverfahren der Numerik.
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image02.jpg?raw=true)

Mit math4u2 interaktiv arbeiten
=======

**Objekt definieren: Definitionsfeld und Definitionsliste**

Objekte werden durch Eingabe der entsprechenden Definition im Definitionsfeld (z.B. f(x):=x*sin(x)) erzeugt. Eine Definition wird durch Eingabe von RETURN wirksam. 
Jedes Objekt wird durch einen Eintrag in der Definitionsliste repräsentiert. 
Neue Einträge werden oben in der Liste angefügt.  
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image03.jpg?raw=true)

In komplizierteren Fällen unterstützt ein Wizard bei der Definition. 
Er gibt dem Benutzer einen Überblick über die verfügbaren Objekte und den aktuellen Stand der Definition:
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image04.jpg?raw=true)

**Die Online-Hilfe**

Die Online-Hilfe gibt dem Benutzer einen Überblick über die Bedienung und beschreibt detailliert die Möglichkeiten bis hin zur Java-Schnittstelle.
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image05.jpg?raw=true)

**Objekte Zeichnen: drag & drop**

Ein Objekt wird gezeichnet, indem man den entsprechenden Eintrag der Definitionsliste mit gedrückter linker Maustaste auf die gewünschte Zeichenfläche zieht und dort die Taste loslässt (drag & drop). 

Wenn zu einem Objekt eine Detailsicht existiert, dann kann man auch diese Detailsicht auf die Zeichenfläche ziehen. 

Je nach Typ des Objekts wird es jetzt auf der Zeichenfläche durch einen spezifischen Graphen dargestellt. Wichtige Beispiele sind: 
 
- Zu einer einsteligen Funktion wie f(x):=x*sin(x) wird der Funktionsgraph gezeichnet. 
- Ein Punkt p:=punkt(1,1) zeichet sich, er kann mit der Maus verschoben werden, seine Koordinaten ändern sich dann entsprechend. 
- Die anderen affinen Objekte (kreis,flaeche, kurve, bezier, kurvenzug und wikel) zeichnen sich wie gewohnt.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image06.jpg?raw=true)

(Lektion: Geometrie/Kurven/Zykloide )

Die mathematischen Grenzen der Zeichenflächen sind über entsprechende Attribute zugänglich.
Sie können interaktiv oder im Rahmen von Lektions-Skripten abgefragt und über entsprechende oder im Rahmen einer Animation  verändert werden. Dies erlaubt eine kontinuierliche Fokussierung des Bildausschnitts auf die aktuell wichtigen Aspekte der mathematischen Argumentation. Ausschnitt und Anzeige verschiedener Fenster können so gekoppelt werden.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image07.jpg?raw=true)

(Lektion: Analysis/Entwicklung/Fourierreihen/Beispiele/Rechteck - kurz )

**Objekte inspizieren und manipulieren: Detailsicht und Detailliste**

Für viele Objekte kann eine Detailsicht erzeugt werden. Eine Detailsicht dient dazu, einzelne Eigenschaften eines Objekts zu beobachten oder zu verändern. 

Die Form einer Detailsicht hängt vom Typ des zugehörigen Objekts ab. 
Man erzeugt eine Detailsicht eines Objekts, indem man das entsprechende Element der Definitionsliste mit gedrückter Maustaste nach rechts auf den Verankerungsplatz der Detailliste zieht: 
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image08.jpg?raw=true)

In der Detailliste können mehrere Detailsichten verwaltet werden. In der Regel wird man nur von den wenigen Objekten eine Detailsicht erzeugen, die aktuell von besonderem Interesse sind. Die nicht mehr benötigten Detailsichten können gelöscht  werden, ohne dass davon das eigentliche Objekt betroffen ist. 

**Arbeitsstand speichern**

Bei Bedarf  kann der  aktuelle Arbeitsstand gespeichert und später wieder geladen werden. Dies geschieht in Form eines Lektions-Skripts in einer XML-Datei. 

math4u2-Lektionen bearbeiten
=======

math4u2-Lektionen können lokal auf dem Rechner des Benutzers abgespeichert sein oder direkt von einem math4u2-Server bezogen werden.

**Der Themen-Navigator**

Der Themen-Navigator gibt einen baumartig gegliederten Überblick über die aktuell (lokal oder auf dem Server) verfügbaren Lektionen. Mit einem Doppelklick kann die gewünschte Lektion dort gestartet werden.

Bei Bedarf kann diese Datei mit einem entsprechenden Editor geöffnet und bearbeitet werden. 
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image09.jpg?raw=true)

Zentrales Element einer Lektion sind in der Regel ein oder mehrere Graphik-Fenster.  In Ihnen werden mathematische Elemente schrittweise aufgebaut und verändert – entweder durch Animation oder durch Experimentieren des Benutzers. 
Synchron dazu kann im Lektions-Fenster ein Text samt Formeln den Fortgang der Argumentation erläutern.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image10.jpg?raw=true)

(Lektion: Geometrie/Polygone/Punkt-in-Polygon-Test )

**Das Lektions-Fenster**

Über das Lektions-Fenster steuert man den Ablauf einer Lektion. Die Steuerelemente werden erst eingeblendet, wenn Sie eine Lektion gestartet haben.
![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image11.jpg?raw=true)

Der Benutzer wird schrittweise durch die Lektion geführt. Text und gegebenenfalls entsprechende Formeln erläutern die Aktionen auf der Zeichenfläche oder die formale Entwicklung der Lösung.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image12.jpg?raw=true)

Elemente zur Ein-/Ausgabe im Text oder direkt in den Formeln machen den Bezug von möglichen Manipulationen zum mathematischen Kontext deutlich.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image13.jpg?raw=true)

**Lektionen erstellen: Lektions-Skripte**

Lektionen werden in einer erweiterten XML-Syntax erstellt. 
Schrittweise können Objekte definiert und verändert, erläuternde Texte angezeigt und Animationen angestoßen werden.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image14.jpg?raw=true)

(Lektion: Analysis/Entwicklung/Fourierreihen/Beispiele/Rechteck )

Die Java-Schnittstelle erlaubt eine Definition von neuen Funktionen und Graphen. Durch Methoden zum Zugriff auf schon definierte Funktionen und Objekte ist eine nahtlose Integration in das Definitions-Gefüge einer Lektion möglich.

![image](https://github.com/fennstef/math4u2/blob/master/doc/images/image15.jpg?raw=true)

(Lektion: Chaos/Logistische Abbildung - Ein Weg ins Chaos )
