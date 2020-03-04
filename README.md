 # KS - Projekt im Rahmen von Prof. Dr. Udo Mueller an der Hochschule Karlsruhe - Technik und Wirtschaft
 
Kommunikationssysteme – Entwicklung eines Prototyps zur Erfassung von Bewegungsprofilen in Kooperation mit [Andreas Foitzik](https://github.com/andreasfoitzik/KS)

**Note: Dieses Repository beinhaltet lediglich die Android App. Server und Webanwendung sind zu finden unter folgendem Repository https://github.com/andreasfoitzik/KS bzw. https://github.com/sven2101/Beacon-Tracking-Web-App**

## Beschreibung
Die Idee von betrance basiert auf iBeacons. iBeacons sind einfache Bluetooth Sender mit diesen man z.B. standortabhängige Funktionen freischalten kann. Kombiniert man diese mit einer Smartphone App und einer Webanwendung können sehr gute und interessante Systeme entstehen. 

Im Rahmen dieses Projekts haben wir uns für einen Bewegungstracker zur Erstellung von Bewegungsprofilen entschieden mit welchem es möglich sein soll, anhand der iBeacons und der App die Laufwege einer Person aufzuzeigen. Unser System basiert auf drei verschiedenen Objekten Benutzern, Netzwerken und iBeacons.

Ein Netzwerk ist immer eindeutig und kann keinen oder mehrere iBeacons beinhalten. Ein Benutzer kann vorerst keinem oder mehreren Netzwerken hinzugefügt werden. Ist ein Benutzer in einem Netzwerk, dann kann er auch die Bewegungen in einem Netzwerk verfolgen.  

## Architektur

* Android App und Browser Webapp (AngularJS) als Client
* NODE.JS als Anwendungsserver
* MongoDB als Datenbank

## Android Beacon Library

Damit die Kommunikation zwischen der Android App und den Beacons funktioniert wurde die [Android Beacon Library](https://altbeacon.github.io/android-beacon-library/index.html) verwendet.

## Aufbau


Komponente |      Beschreibung     
-------------- | ------------ 
BeaconNotificationApplication      |     Die Applikationsanwendung in der das Interface BootstrapNotifier implementiert ist und somit auch der Monitorningservice mit DidEnterRegion,DidExitRegion,.... Außerdem wird hier eine Notificatin für die Statusleiste in Android gesendet, sobald ein Beacon gefunden wurde bzw. der Status sich geändert hat.     
LoginActivity      |     In dieser Activity wird der Login und die Registrierung bereitgestellt. Es sind Plausibilitätsprüfungen implementiert um unzulässige eingaben zu Verhindern. Die Logindaten werden überprüft und bei erfolgreicher Logjn wird an die MainActivity weitergeleitet.
MainActivity      |     In dieser Acitivity wird die Sidebar und das Konstrukt für die Hauptanwendung erzeugt. Fragmente werden je nach Sidebarauswahl gewechselt(HomeFragment, RangingFragment, NetworkFragment und MonitoringFragment). Außerdem sind diverse Alerts, ein Menü und Broadcasts implementiert um die Anwendung funktionsfähig intuitiv zu gestalten.
NavDrawerListAdapter      |     Controller für die Verwendung der Sidebar.
NavDrawerItem      |     Gestaltung der einzelnen Items in der Sidebar. 
GridViewAdapter      |     Stellt die GridView Elemente in NetworkFragment und BeaconAcitivity bereit und agiert als Controller.  
HomeFragment      |     Fragment für den Willkommensbildschirm.
RangingFragment      |     Dieses Fragment teilt sich in HeadFragment(Such-Button) und BeaconFragment(ListView) auf. In RangingFragment kann nach Beacons gesucht werden, indem durch klicken auf ScanBeacons der Service RangingService aufgerufen wird. Außerdem wird beim aufrufen des Fragments überprüft, ob Bluetooth eingeschaltet ist. 
HeadFragment      |     Anzeige des ScanBeacons Buttons in RangingFragment.
BeaconFragment      |     Stellt die ListView für die Beacons zur Verfügung. Zudem kann sie ein und ausgeblendet werden, während dem Laden bzw. Suchen nach der Beacons.
RangingService      |     Wird in RangingFragment angestoßen. Sobald dieser Service gestartet ist sucht er 45 Sekunden nach Beacons in Reichweite mittels dem Interface BeaconConsumer und der Methode onBeaconServiceConnect. Wenn ein neuer Beacon gefunden wird, wird er zur ListView hinzugefügt. Falls es ein bestehender Beacon ist wird die Entfernung zum Beacon aktualisiert. Damit die ListView auf dem UI Thread aktualisiert werden kann muss ein Broadcast an die MainActivity gesendet werden, welche anschließend die ListView aktualisiert.
NetworkFragment      |     In diesem Fragment können Netwerke angelegt, gelöscht und bearbeitet werden. Das Kontextmenu kann über longpress aufgerufen werden. Beim Klicken auf ein vorhandenes Netzwerk wird die Beaconactivity ausgeführt. Dort können die zugehörigen Beacons angezeigt werden. Die Netwerke werden in einer GridView angezeigt und verwenden die Klasse GridViewAdapter. 
BeaconActivity      |     Zeigt die Beacons eines Netzwerks in einer neuen Activity an. Durch klicken können Beacons bearbeitet werden. Die Beacons sind ebenfalls in einer GridView und verwenden als Controller die Klasse GridViewAdapter.  
MonitoringFragment      |     Zeigt die aktuellen Monitoring Ereignisse in einer TextView an. Z.B Erscheinen eines Beacons oder ein Beacon ist außer Reichweite. 
SettingsActivity      |     Globale Einstellungen für die Anwendung. Es kann der Display Name geändert werden, die Scan Periode verändert werder, ein Beacon Layout hinzugefügt werden,….
   
 
Komponente |      Beschreibung     
-------------- | ------------ 
Res-Drawable |Hier befinden sich die Bilddateien im png Format bzw. die Icons/Komponenten für die Applikation. HDPI, LDPI, MDPI,… steht für die größte der Bilddateien bzw. Icons/Komponenten.
Res-Layout | In den einzelnen XML-dateien befinden sich die Layouts für die Fragmente bzw. Activitys die verwendet werden. Die Namen passen zu den Klassen und können somit zugeordnet werden. Die Layouts sind Teil einer Android App und bilden den Grafischen Teil der Android App in XML ab. Hier sind Buttons, Textviews, ListViews uvm definiert. 
Res-Menu | Hier wird das Menü oder die unterschiedlichen Menüleisten für die Anwendung in XML definiert. Betrance verfügt lediglich über eine einzige menu.xml. Da die komplette Anwendung nur ein Menü benötigt.
Res-Values | Definition von Variablen, welche in der Anwendung verwendet werden. 
Res-XML | Preferences, welche in der Anwendung verwendet werden. Preferences sind Daten, die in der Anwendung dauerhaft oder für eine begrenzte Zeit gespeichert werden. Z.B. Settings, Login Tokens usw..


