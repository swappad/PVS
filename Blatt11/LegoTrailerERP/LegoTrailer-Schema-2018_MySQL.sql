
/*---------------------------------------------------------------------------------*/
/*             Datenbankschema für Beispielfirma LegoTrailer AG                    */
/*     für Vorlesung und Übungen zu 'Informationssysteme', Sommersemester 2014     */
/*                         Autor: Peter Dadam                                      */
/*                         Stand: 2018-04-11                                       */
/*																				   */
/*    Kommentare:	- Fixes für MySQL eingefügt      				               */
/*               	- DROP TABLE und Datum an MySQL angepasst					   */
/* 					  Vielen Dank @Pascal Weber          						   */
/*                     Kommentar: Fixes für MySQL eingefügt                        */
/*---------------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------------*/
/*                        USE DATABASE Statements                           */
/*---------------------------------------------------------------------------------*/
CREATE DATABASE legotrailer_db;
USE legotrailer_db;


/*---------------------------------------------------------------------------------*/
/*                                TeileTypen                                       */
/*---------------------------------------------------------------------------------*/

CREATE TABLE TeileTypen (
  TeileID    VARCHAR(8) NOT NULL,
  TeileName  VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (TeileID));

  
/*---------------------------------------------------------------------------------*/
/*                                 Farbcodes                                       */
/*---------------------------------------------------------------------------------*/  
  
CREATE TABLE Farbcodes (
  Farbcode   INTEGER NOT NULL,
  FarbeText  VARCHAR(10) NOT NULL UNIQUE,
  PRIMARY KEY (Farbcode));
  

/*---------------------------------------------------------------------------------*/
/*                                  Teile                                          */
/*---------------------------------------------------------------------------------*/
/* 'Bestand' kann auch Zugänge aufgrund von in Arbeit befindlichen Aufträgen sowie */
/* von erledigten Bestellanforderungen der Fertigung Artikel oder Einzelteile      */
/* enthalten, die bereits "verplant" sind. Diese Menge wird im 'Reserviert'-       */
/* Attribut angezeigt.                                                             */
/* Der "verfügbare" Bestand ergibt sich daher aus Bestand ./. Reserviert.          */
/* Nach Ausfassen eines Artikels für eine Bestellanforderung oder (reservierten)   */
/* Teilen für dei Fertigung müssen 'Bestand' und 'Reserviert' daher entsprechend   */
/* herabgesetzt werden.                                                            */
/*---------------------------------------------------------------------------------*/                      

CREATE TABLE Teile(
  TeileID     		VARCHAR(8) NOT NULL,
  Farbe       		INTEGER NOT NULL,
  KalkKosten  		DECIMAL(6,2),
  Bestand     		INTEGER NOT NULL,
  MinBestand  		INTEGER NOT NULL,
  Reserviert    	INTEGER NOT NULL, 
  PRIMARY KEY (TeileID, Farbe),
  CONSTRAINT Teile_FK1 FOREIGN KEY (TeileID) REFERENCES TeileTypen(TeileID),
  CONSTRAINT Teile_FK2 FOREIGN KEY (Farbe) REFERENCES Farbcodes(Farbcode));


/*---------------------------------------------------------------------------------*/
/*                                Stückliste                                       */
/*---------------------------------------------------------------------------------*/

CREATE TABLE Stueckliste(
TeileID	    VARCHAR(8) NOT NULL,
Farbe       INTEGER NOT NULL,
UTeilID     VARCHAR(8) NOT NULL,
UTeilFarbe  INTEGER NOT NULL,
Anzahl      INTEGER NOT NULL,
PRIMARY KEY (TeileID, Farbe, UTeilID, UTeilFarbe),
CONSTRAINT Stueckliste_FK1 FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe),
CONSTRAINT Stueckliste_FK2 FOREIGN KEY (UTeilID, UTeilFarbe) REFERENCES Teile(TeileID, Farbe));


/*---------------------------------------------------------------------------------*/
/*                               Teilebedarf                                       */
/*---------------------------------------------------------------------------------*/
/*    Materialisierte Stücklistenauflösung für die Produkte der LegoTrailer AG     */
/*  (als Alternative zur Stücklistenauflösung per Rekursion zur Ausführungszeit)   */
/*---------------------------------------------------------------------------------*/

CREATE TABLE Teilebedarf (
TeileID		VARCHAR(8) NOT NULL,
Farbe	   	INTEGER NOT NULL,
UTeilID		VARCHAR(8) NOT NULL,
UTeilFarbe	INTEGER NOT NULL,
Bedarf		INTEGER NOT NULL,
PRIMARY KEY (TeileID, Farbe, UTeilID, UTeilFarbe),
CONSTRAINT Teilebedarf_FK1 FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe),
CONSTRAINT Teilebedarf_FK2 FOREIGN KEY (UTeilID, UTeilFarbe) REFERENCES Teile(TeileID, Farbe));


/*---------------------------------------------------------------------------------*/
/*                                Preisliste                                       */
/*---------------------------------------------------------------------------------*/

CREATE TABLE Preisliste (
  TeileID VARCHAR(8) NOT NULL,
  Farbe   INTEGER NOT NULL,
  Preis   DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (TeileID, Farbe),
  FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe));


/*---------------------------------------------------------------------------------*/
/*                                   Kunden                                        */
/*---------------------------------------------------------------------------------*/   

CREATE TABLE Kunden(
  KdNr     INTEGER NOT NULL,
  KdName   VARCHAR(30) NOT NULL,
  KdStadt  VARCHAR(30) NOT NULL,
  Bonitaet INTEGER CHECK (Bonitaet IS NULL OR Bonitaet BETWEEN -2 and 2),
  PRIMARY KEY (KdNr));


/*---------------------------------------------------------------------------------*/
/*                        Auftrags-Datensatz (AuftragsDS)                          */
/*---------------------------------------------------------------------------------*/
/* 'LgLieferung' steht für "Lagerlieferung" und bedeutet (falls = 1), dass der     */
/* gesamte Auftrag aus dem verfügbaren Lagerbestand bedient werden kann.           */
/*                                                                                 */
/* Die Einträge im Attribut 'AuftrStatus' haben die folgende Bedeutung:            */ 
/*   0 :  unbearbeitet                                                             */
/*   1 :  Verfügbare Bestands-Artikel reserviert und Fertigungsauftrag initiiert   */
/*   2 :  Fertigung beendet und Ware in Bestand + Reservierung eingebucht          */
/*   3 :  Ware im Bestand (+ evtl. Reserv.) ausgebucht und steht zur Auslieferung  */
/*        bereit                                                                   */
/*   4 :  Ware wurde ausgeliefert und Rechnungsschreibung wurde initiiert          */
/*   5 :  Rechnungsschreibung ist erfolgt und die Bearbeitung des Auftrags ist     */
/*        damit abgeschlossen.                                                     */
/*                                                                                 */
/* Hat 'AuftrStatus' den Wert 1 oder 2, dann fungieren die Auftrags-Tupel und die  */
/* zugehörigen AuftragsPos-Tupel gleichzeitig auch als 'Fertigungsauftrag' (siehe  */
/* Attribute 'AuftrAnFertigung', 'AuftrPosInArbeit' und 'FertigungBeendet').       */ 
/* In 'AuftrPosInArbeit' (initial = Anzahl der Auftragspositionen) wird fest-      */
/* gehalten, wieviele Auftragspositionen aktuell noch in der Fertigung "in Arbeit" */
/* sind.                                                                           */
/*                                                                                 */
/* Die Einträge im Attribut 'RechnungsStatus' haben die folgende Bedeutung:        */
/*   0 :  noch keine Rechnung zu erstellen                                         */
/*   1 :  Rechnung zu erstellen                                                    */
/*   2 :  Rechnung wurde erstellt und an den Kunden versandt                       */
/*   3 :  Rechnung wurde bezahlt                                                   */
/*---------------------------------------------------------------------------------*/  

CREATE TABLE AuftragsDS (
  AuftrNr           INTEGER NOT NULL,
  KdNr              INTEGER NOT NULL, 
  KdAuftrNr         VARCHAR(80),  /* optional */
  KdAuftrDatum      DATE NOT NULL,  /* gemäß Kundenauftrag */
  ErfassungsDatum   DATE NOT NULL, /* MySQL-Fix in ALTER TABLE */
  LgLieferung       INTEGER
						CHECK ( LgLieferung IS NULL OR LgLieferung IN ( 0, 1 ) ),
  AuftrStatus       INTEGER NOT NULL DEFAULT 0
						CHECK ( AuftrStatus BETWEEN 0 AND 5 ),
  AuftrAnFertigung  DATE,
  AuftrPosInArbeit  INTEGER,
  FertigungBeendet  DATE,
  WareAusliefBereit DATE,      /* Ware steht zur Auslieferung an Kunden bereit */  
  AusliefDatum      DATE,      /* Datum der Auslieferung an den Kunden */
  RechnungsStatus   INTEGER NOT NULL DEFAULT 0 
                        CHECK ( RechnungsStatus BETWEEN 0 AND 3 ),
  RechnungsDatum    DATE,
  ZahlungsEingang   DATE,
  PRIMARY KEY (AuftrNr),
  CONSTRAINT AuftragsDS_FK1 FOREIGN KEY (KdNr) REFERENCES Kunden(KdNr));

/* Fix für MySQL: DEFAULT Wert muss eine Konstante sein, deshalb kein CURRENT DATE möglich! */  
ALTER TABLE AuftragsDS MODIFY ErfassungsDatum DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL;
 
  
/*---------------------------------------------------------------------------------*/
/*                  Auftragsposition-Datensatz (AuftragsPosDS)                     */
/*---------------------------------------------------------------------------------*/
/* Kann der Auftrag als 'Lagerlieferung' durchgeführt werden, sind die auf         */
/* 'AnzVonKundeBestellt' folgenden Attribute irrelevant; 'Fertigungsstatus' hat    */
/* in diesem Fall den Wert -1.                                                     */
/* Müssen hingegen Artikel für den Auftrag hergestellt werden, dann beinhalten     */
/* diese Atribute die folgenden Informationen:                                     */
/*   'AnzReserviert'      : Anzahl dieses Artikels, die für diese Auftragsposition */
/*                          im entsprechenden Tupel der 'Teile'-Relation als       */
/*                          'reserviert' ausgewiesen sind                          */
/*   'AnzNochZuFertigen   : Anzahl der für diese Auftragsposition zu fertigende    */
/*                          Artikel                                                */
/*                          Müssen Einzelteile für die Herstellung des Artikels    */
/*                          bestellt werden, wird seitens der Fertigung die        */
/*                          gesamte Menge an benötigten Einzelteilen an den        */
/*                          Einkauf als Bedarf gemeldet. Das AuftragsPos-Tupel     */
/*                          nimmt hierbei die Rolle einer Bestellanforderung, dem  */
/*                          in der Relation 'BestellAnforderungen' die einzelnen   */
/*                          Bestellpositionen via (AuftrNr, AuftrPos) zugeordnet   */
/*                          sind.                                                  */ 
/*   'BestAnfErstellt     : Datum, wann BestellAnforderung initiiert wurde         */
/*   'BestAnfErledigt     : Datum (trägt Einkauf ein), wann Teile eingetroffen     */
/*                          (Einkauf erhöht Bestand + Reservierung entsprechend)   */
/*   'FertigungGestartet' : Startdatum der Fertigung und Ausfassen d. Einzelteile; */
/*                          Bestand und Reservierung werden entspr. reduziert.     */
/*   'FertigungBeendet'   : Endedatum der Fertigung und Einbuchen der Artikel in   */
/*                          Bestand und Reservierung, außerdem Herunterzählen von  */
/*                          'AuftrPosInArbeit' im 'Auftraege'-Tupel. Falls dieses  */
/*                          den Wert 0 annimmt, dort 'AuftrStatus' auf 2 setzen u. */
/*                          'FertigungBeendet' mit dem aktuellen Datum versehen.   */
/*                                                                                 */
/*   'FertigungsStatus' kann die folgenden Werte bzw. Bedeutungen annehmen:        */
/*       -1 :  nicht  zutreffend (da "Lagerware")                                  */
/*        0 :  noch unbearbeitet                                                   */
/*        1 :  Die Bestellanforderung wurde initiiert                              */
/*        2 :  Die bestellte Ware ist eingetroffen und wurde in Bestand und        */
/*             Reservierung eingebucht                                             */
/*        3 :  Die Fertigung wurde gestartet und die Teile aus Bestand und         */
/*             Reservierung ausgebucht                                             */
/*        4 :  Die Fertigung wurde abgeschlossen und der Artikel in Bestand (und   */
/*             ggf. bei 'reserviert') ausgebucht                                   */
/*        5 :  Die Auftragsbearbeitung hat den Vorgang abgeschlossen               */ 
/*                                                                                 */
/* Wichtige Konvention: Aus realisierungstechnischen Gründen muss die Attribut-    */
/* kombination(AuftrNr, TeileID, Farbe) eindeutig sein, d.h. in einem Auftrag kann */
/* nicht mehrmals derselbe Artikel bestellt werden (siehe UNIQUE-Constraint)       */
/*---------------------------------------------------------------------------------*/ 

CREATE TABLE AuftragsPosDS(
  AuftrNr                 INTEGER NOT NULL,
  AuftrPos                INTEGER NOT NULL,
  TeileID                 VARCHAR(8) NOT NULL,
  Farbe                   INTEGER NOT NULL,
  VkPreis                 DECIMAL(6,2), /* Verkaufspreis, NULL, falls Listenpreis   */
  AnzVonKundeBestellt     INTEGER NOT NULL,
  AnzFuerKundeReserviert  INTEGER,  /* nur relevant, falls nicht 'Lagerlieferung'   */
  AnzNochZuFertigen       INTEGER,  /* nur relevant, falls nicht 'Lagerlieferung'   */
  BestAnfordErstellt      DATE,
  BestAnfordErledigt      DATE,
  FertigungGestartet      DATE,     /* nur relevant, falls nicht 'Lagerlieferung'   */
  FertigungBeendet        DATE,     /* nur relevant, falls nicht 'Lagerlieferung'   */
  FertigungsStatus        INTEGER NOT NULL DEFAULT -1,    		
  PRIMARY KEY (AuftrNr, AuftrPos),
  FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe),
  UNIQUE (AuftrNr, TeileID, Farbe),
  CONSTRAINT AuftragsPosDS_FK1 FOREIGN KEY (AuftrNr) REFERENCES AuftragsDS(AuftrNr) ON DELETE CASCADE ); 

/* Bugfix für Derby: DEFAULT -1 und CHECK vertragen sich nicht im CREATE-TABLE-Stmt */  
ALTER TABLE AuftragsPosDS ADD CONSTRAINT FertStatus_Constr
     CHECK ( FertigungsStatus BETWEEN -1 AND 5 );  
  
 
/*---------------------------------------------------------------------------------*/
/*                             Bestellanforderungen                                */
/*---------------------------------------------------------------------------------*/
/* In diese Relation werden alle Bestellanforderungen aller Auftragspositionen     */
/* eingetragen, für deren Herstellung Teile über den Einkauf beschafft werden      */
/* müssen. Der Einkauf fasst evtl. mehrere, dasselbe Teil betreffende Bestell-     */
/* anforderungen in einer Bestellposition zusammen. Der Zusammenhang wird via      */
/* (BestNr, BestPos) hergestellt.                                                  */
/* 'BestAnfStatus' kann folgende Werte mit folgenden Bedeutungen einnehmen:        */
/*    0 :  noch nicht bearbeitet                                                   */
/*    1 :  Ware wurde bestellt                                                     */
/*    2 :  Ware ist eingegangen                                                    */
/*    3 :  Ware im Bestand + Reservierung verbucht                                 */
/*    4 :  Die Fertigung hat den Zugang zur Kenntnis genommen; der Vorgang ist     */
/*         damit abgeschlossen                                                     */             
/*---------------------------------------------------------------------------------*/
	
CREATE TABLE Bestellanforderungen (
	AuftrNr             INTEGER NOT NULL,   /* referenziert AuftrasPos-Tupel  */
	AuftrPos            INTEGER NOT NULL,   /* referenziert AuftragsPos-Tupel */
	UTeilID             VARCHAR(8) NOT NULL, 
	UTeilFarbe          INTEGER NOT NULL,
	Anzahl              INTEGER NOT NULL,
	BestErledigtDatum   DATE,
	BestNr              INTEGER,            /* Bestelldatum = Bearbeitungsdatum */
	BestPos             INTEGER,
	BestAnfStatus       INTEGER NOT NULL DEFAULT 0 CHECK( BestAnfStatus BETWEEN 0 AND 4 ),
	PRIMARY KEY (AuftrNr, AuftrPos, UTeilID, UTeilFarbe),
	FOREIGN KEY (AuftrNr, AuftrPos) REFERENCES AuftragsPosDS(AuftrNr, AuftrPos),
	FOREIGN KEY (UTeilID, UTeilFarbe) REFERENCES Teile(TeileID, Farbe) );

	
/*---------------------------------------------------------------------------------*/
/*                                    Lieferanten                                  */
/*---------------------------------------------------------------------------------*/	

CREATE TABLE Lieferanten(
  LiefNr     INTEGER NOT NULL,
  LiefName   VARCHAR(30) NOT NULL,
  LiefStadt  VARCHAR(30) NOT NULL,
  Bewertung  INTEGER CHECK (Bewertung IS NULL OR Bewertung BETWEEN -2 AND 2),
  PRIMARY KEY (LiefNr));	

  
/*---------------------------------------------------------------------------------*/
/*                                      Liefert                                    */
/*---------------------------------------------------------------------------------*/  
  
CREATE TABLE Liefert(
  LiefNr  INTEGER NOT NULL,
  TeileID VARCHAR(8) NOT NULL,
  Farbe   INTEGER NOT NULL,
  Preis   DECIMAL(6,2),
  PRIMARY KEY (LiefNr, TeileID, Farbe),
  FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe),
  FOREIGN KEY (LiefNr) REFERENCES Lieferanten(LiefNr));
	
  
/*---------------------------------------------------------------------------------*/
/*                                   Bestellungen                                  */
/*---------------------------------------------------------------------------------*/
/* Bestellungen werden entweder aufgrund von Bestellanforderungen (durch die       */
/* Fertigungsabteilung, -> AuftragsBest = 1) oder selbstständig seitens der        */
/* Einkaufsabteilung (-> AuftragsBest = 0), etwa wegen Unterschreitung eines       */
/* Mindestbestandes ausgelöst.                                                     */
/* Mit den Lieferanten ist vereinbart, dass keine Teillieferungen stattfinden,     */
/* d.h. die Bestellung wird immer komplett in einer Warenlieferung angeliefert.    */
/* Die Einträge im Attribut 'Status' haben die folgende Bedeutung:                 */ 
/*   0 :  Bestellung läuft                                                         */
/*   1 :  Die Ware wurde angeliefert                                               */
/*   2 :  Die Ware wurde im Lager eingelagert und im Bestand eingebucht.           */ 
/*        Falls Beschaffung aufgrund Bestellanforderung seitens der Fertigung:     */
/*        Einbuchung auch bei Reservierung sowie setzen 'BestAnfStatus' = 3 im     */ 
/*        zugehörigen Tupel in 'Bestellanforderungen'; der Vorgang ist damit       */
/*        abgeschlossen.                                                           */
/*---------------------------------------------------------------------------------*/

CREATE TABLE Bestellungen(
  BestNr           INTEGER NOT NULL,
  LiefNr           INTEGER NOT NULL,
  BestDatum        DATE, /* MySQL-Fix in ALTER TABLE */
  WareErhalten     DATE, /* Datum des Wareneingangs */
  AuftragsBest     INTEGER NOT NULL DEFAULT 1 CHECK (AuftragsBest BETWEEN 0 AND 1),
  LgZugangGebucht  DATE, /* Datum Verbuchung Lagerzugang */
  Status           INTEGER NOT NULL DEFAULT 0 CHECK (Status BETWEEN 0 AND 2),
  PRIMARY KEY (BestNr),
  FOREIGN KEY (LiefNr) REFERENCES Lieferanten(LiefNr) );
  
/* Fix für MySQL: DEFAULT Wert muss eine Konstante sein, deshalb kein CURRENT DATE möglich! */  
ALTER TABLE Bestellungen MODIFY BestDatum DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL;

  
/*---------------------------------------------------------------------------------*/
/*                                   BestellPos                                    */
/*---------------------------------------------------------------------------------*/  
/* Eingehende Bestellanforderungen, die sich auf dasselbe Teil beziehen, können    */
/* vom Einkauf zu einer BestellPosition zusammengefasst werden. Die Zuordnung      */
/* ergibt sich durch den entsprechenden BestNr/BestPos-Eintrag in der Tabelle      */
/* 'BestellAnfordPos'.                                                             */ 
/*---------------------------------------------------------------------------------*/ 

CREATE TABLE BestellPos(
  BestNr           INTEGER NOT NULL,
  BestPos          INTEGER NOT NULL,
  TeileID          VARCHAR(8) NOT NULL,
  Farbe            INTEGER NOT NULL,
  Anzahl           INTEGER NOT NULL,
  PRIMARY KEY (BestNr, BestPos),
  FOREIGN KEY (TeileID, Farbe) REFERENCES Teile(TeileID, Farbe),
  FOREIGN KEY (BestNr) REFERENCES Bestellungen(BestNr) ON DELETE CASCADE);


/*---------------------------------------------------------------------------------*/
/*                                  Abteilungen                                    */
/*---------------------------------------------------------------------------------*/

CREATE TABLE Abteilungen (
  AbtID     CHAR(2) NOT NULL,
  AbtName   VARCHAR(30) NOT NULL UNIQUE,
  MgrPersNr INTEGER,
  PRIMARY KEY (AbtID) );

  
/*----------------------------------------------------------------------------------*/
/*                                 Mitarbeiter             	                        */
/*----------------------------------------------------------------------------------*/

CREATE TABLE Mitarbeiter (
  PersNr     INTEGER NOT NULL,
  Name       VARCHAR(30) NOT NULL,
  Vorname    VARCHAR(30) NOT NULL,
  UserID     VARCHAR(30) NOT NULL,
  Gehalt     DECIMAL(8,2),
  AbtID      CHAR(2) REFERENCES Abteilungen,
  PRIMARY KEY (PersNr) );	
 
  
	
/*----------------------------------------------------------------------------------*/
/*                           Einfügen Datensätze             	                    */
/*----------------------------------------------------------------------------------*/

INSERT INTO TeileTypen(TeileID, TeileName) VALUES
('AAG',   'Aufbauanhänger geschlossen'),                        
('AKK',   'Kippanhänger kurz'),                                 
('AKL',   'Kippanhänger lang'),
('CAH',   'Chassis Aufbauanhänger'),                            
('CKK',   'Chassis Kippanhänger kurz'),                         
('CKL',   'Chassis Kippanhänger lang'),                         
('FA',    'Felge mit Achse'),                                   
('FG',    'Fahrgestell'),                                       
('GO25',  'Gelenkplatte 2 x 5 oben'),                           
('GU25',  'Gelenkplatte 2 x 5 unten'),                          
('GV25',  'Gelenkverbinder 2 x 5'),                             
('K14',   'Klotz 1 x 4'),                                       
('K16',   'Klotz 1 x 6'),                                       
('K18',   'Klotz 1 x 8'),                                       
('K28',   'Klotz 2 x 8'),                                       
('KR24',  'Klotz 2 x 4 für Rad'),                               
('P18',   'Platte 1 x 8'),                                      
('P24',   'Platte 2 x 4'),                                      
('P616',  'Platte 6 x 16'),                                     
('P624',  'Platte 6 x 24'),                                     
('PZ',    'Platte mit Zapfen'),                                 
('RF',    'Reifen'),                                            
('RK11',  'Rundklotz 1 x 1'),                                   
('TL134', 'Tür links 1 x 3 x 4'),                               
('TR134', 'Tür rechts 1 x 3 x 4');


INSERT INTO Farbcodes(Farbcode, Farbetext) VALUES
(0, 'neutral'),
(1, 'rot'),
(2, 'blau');


INSERT INTO Teile(TeileID, Farbe, KalkKosten, Bestand, MinBestand, Reserviert) VALUES
('AAG',1,342.20,7,0,5),
('AAG',2,376.40,8,0,4),
('AKK',1,227.20,6,0,2),
('AKK',2,337.20,10,0,0),
('AKL',1,370.90,0,0,0),
('AKL',2,386.10,0,0,0),
('CAH',1,222.40,2,0,1),
('CAH',2,244.60,4,0,3),
('CKK',1,237.00,6,0,6),
('CKK',2,269.70,4,0,2),
('CKL',1,264.20,0,0,0),
('CKL',2,290.60,2,0,0),
('FA',1,2.60,10,5,0),
('FA',2,2.90,15,5,0),
('FG',1,88.80,6,4,6),
('FG',2,97.70,10,4,6),
('GO25',0,3.00,20,10,0),
('GU25',0,3.00,20,10,0),
('GV25',0,1.10,20,10,0),
('K14',0,1.20,40,20,0),
('K14',1,1.20,30,20,0),
('K14',2,1.30,15,20,0),
('K16',0,1.90,10,15,0),
('K16',1,1.90,15,20,0),
('K16',2,2.10,25,20,0),
('K18',1,2.20,22,20,0),
('K18',2,2.40,8,20,0),
('K28',0,2.60,12,10,0),
('K28',1,2.60,8,10,0),
('K28',2,2.90,6,10,0),
('KR24',0,3.00,18,20,0),
('P18',1,2.30,18,20,0),
('P18',2,2.50,22,20,0),
('P24',0,2.90,14,20,0),
('P616',0,5.60,16,20,0),
('P624',0,6.20,10,15,0),
('PZ',0,2.40,30,20,0),
('RF',0,2.80,30,20,0),
('RK11',1,1.20,30,20,0),
('RK11',2,1.30,30,20,0),
('TL134',1,4.30,15,10,0),
('TL134',2,4.70,8,10,0),
('TR134',1,4.30,15,10,0),
('TR134',2,4.70,8,10,0);


INSERT INTO Stueckliste(TeileID, Farbe, UTeilID, UTeilFarbe, Anzahl) VALUES
('AAG', 1, 'CAH'  , 1 , 1  ),
('AAG', 1, 'K14'  , 1 , 9  ),
('AAG', 1, 'K16'  , 1 , 28 ),
('AAG', 1, 'P624' , 0 , 1  ),
('AAG', 1, 'TL134', 1 , 1  ),
('AAG', 1, 'TR134', 1 , 1  ),
('AAG', 2, 'CAH'  , 2 , 1  ),
('AAG', 2, 'K14'  , 2 , 9  ),
('AAG', 2, 'K16'  , 2 , 28 ),
('AAG', 2, 'P624' , 0 , 1  ),
('AAG', 2, 'TL134', 2 , 1  ),
('AAG', 2, 'TR134', 2 , 1  ),
('AKK', 1, 'CKK'  , 1 , 1  ),
('AKK', 1, 'K14'  , 1 , 6  ),
('AKK', 1, 'K16'  , 1 , 8  ),
('AKK', 2, 'CKK'  , 2 , 1  ),
('AKK', 2, 'K14'  , 2 , 6  ),
('AKK', 2, 'K16'  , 2 , 8  ),
('AKL', 1, 'CKL'  , 1 , 1  ),
('AKL', 1, 'K14'  , 1 , 2  ),
('AKL', 1, 'K16'  , 1 , 16 ),
('AKL', 2, 'CKL'  , 2 , 1  ),
('AKL', 2, 'K14'  , 2 , 2  ),
('AKL', 2, 'K16'  , 2 , 16 ),
('CAH', 1, 'FG'   , 1 , 1  ),
('CAH', 1, 'K18'  , 1 , 2  ),
('CAH', 1, 'P18'  , 1 , 2  ),
('CAH', 1, 'P624' , 0 , 1  ),
('CAH', 1, 'PZ'   , 0 , 1  ),
('CAH', 1, 'RK11' , 1 , 4  ),
('CAH', 2, 'FG'   , 2 , 1  ),
('CAH', 2, 'K18'  , 2 , 2  ),
('CAH', 2, 'P18'  , 2 , 2  ),
('CAH', 2, 'P624' , 0 , 1  ),
('CAH', 2, 'PZ'   , 0 , 1  ),
('CAH', 2, 'RK11' , 2 , 4  ),
('CKK', 1, 'FG'   , 1 , 1  ),
('CKK', 1, 'GO25' , 0 , 2  ),
('CKK', 1, 'GU25' , 0 , 2  ),
('CKK', 1, 'GV25' , 0 , 2  ),
('CKK', 1, 'K16'  , 0 , 1  ),
('CKK', 1, 'P616' , 0 , 2  ),
('CKK', 1, 'PZ'   , 0 , 1  ),
('CKK', 2, 'FG'   , 2 , 1  ),
('CKK', 2, 'GO25' , 0 , 2  ),
('CKK', 2, 'GU25' , 0 , 2  ),
('CKK', 2, 'GV25' , 0 , 2  ),
('CKK', 2, 'K16'  , 0 , 1  ),
('CKK', 2, 'P616' , 0 , 2  ),
('CKK', 2, 'PZ'   , 0 , 1  ),
('CKL', 1, 'FG'   , 1 , 1  ),
('CKL', 1, 'GO25' , 0 , 2  ),
('CKL', 1, 'GU25' , 0 , 2  ),
('CKL', 1, 'GV25' , 0 , 2  ),
('CKL', 1, 'K16'  , 0 , 1  ),
('CKL', 1, 'K18'  , 1 , 2  ),
('CKL', 1, 'P18'  , 1 , 2  ),
('CKL', 1, 'P624' , 0 , 2  ),
('CKL', 1, 'PZ'   , 0 , 1  ),
('CKL', 1, 'RK11' , 1 , 4  ),
('CKL', 2, 'FG'   , 2 , 1  ),
('CKL', 2, 'GO25' , 0 , 2  ),
('CKL', 2, 'GU25' , 0 , 2  ),
('CKL', 2, 'GV25' , 0 , 2  ),
('CKL', 2, 'K16'  , 0 , 1  ),
('CKL', 2, 'K16'  , 2 , 4  ),
('CKL', 2, 'P624' , 0 , 2  ),
('CKL', 2, 'PZ'   , 0 , 1  ),
('CKL', 2, 'RK11' , 2 , 4  ),
('FG' , 1, 'FA'   , 1 , 4  ),
('FG' , 1, 'K28'  , 0 , 2  ),
('FG' , 1, 'KR24' , 0 , 2  ),
('FG' , 1, 'P24'  , 0 , 4  ),
('FG' , 1, 'RF'   , 0 , 4  ),
('FG' , 2, 'FA'   , 2 , 4  ),
('FG' , 2, 'K28'  , 0 , 2  ),
('FG' , 2, 'KR24' , 0 , 2  ),
('FG' , 2, 'P24'  , 0 , 4  ),
('FG' , 2, 'RF'   , 0 , 4  );


INSERT INTO Teilebedarf(TeileID, Farbe, UTeilID, UTeilFarbe, Bedarf) VALUES
('AAG', 1, 'FA',    1,  4),
('AAG', 1, 'K14',   1,  9),
('AAG', 1, 'K16',   1, 28),
('AAG', 1, 'K18',   1,  2),
('AAG', 1, 'K28',   0,  2),
('AAG', 1, 'KR24',  0,  2),
('AAG', 1, 'P18',   1,  2),
('AAG', 1, 'P24',   0,  4),
('AAG', 1, 'P624',  0,  2),
('AAG', 1, 'PZ',    0,  1),
('AAG', 1, 'RF',    0,  4),
('AAG', 1, 'RK11',  1,  4),
('AAG', 1, 'TL134', 1,  1),
('AAG', 1, 'TR134', 1,  1),
('AAG', 2, 'FA',    2,  4),
('AAG', 2, 'K14',   2,  9),
('AAG', 2, 'K16',   2, 28),
('AAG', 2, 'K18',   2,  2),
('AAG', 2, 'K28',   0,  2),
('AAG', 2, 'KR24',  0,  2),
('AAG', 2, 'P18',   2,  2),
('AAG', 2, 'P24',   0,  4),
('AAG', 2, 'P624',  0,  2),
('AAG', 2, 'PZ',    0,  1),
('AAG', 2, 'RF',    0,  4),
('AAG', 2, 'RK11',  2,  4),
('AAG', 2, 'TL134', 2,  1),
('AAG', 2, 'TR134', 2,  1),
('AKK', 1, 'FA',    1,  4),
('AKK', 1, 'GO25',  0,  2),
('AKK', 1, 'GU25',  0,  2),
('AKK', 1, 'GV25',  0,  2),
('AKK', 1, 'K14',   1,  6),
('AKK', 1, 'K16',   0,  1),
('AKK', 1, 'K16',   1,  8),
('AKK', 1, 'K28',   0,  2),
('AKK', 1, 'KR24',  0,  2),
('AKK', 1, 'P24',   0,  4),
('AKK', 1, 'P616',  0,  2),
('AKK', 1, 'PZ',    0,  1),
('AKK', 1, 'RF',    0,  4),
('AKK', 2, 'FA',    2,  4),
('AKK', 2, 'GO25',  0,  2),
('AKK', 2, 'GU25',  0,  2),
('AKK', 2, 'GV25',  0,  2),
('AKK', 2, 'K14',   2,  6),
('AKK', 2, 'K16',   0,  1),
('AKK', 2, 'K16',   2,  8),
('AKK', 2, 'K28',   0,  2),
('AKK', 2, 'KR24',  0,  2),
('AKK', 2, 'P24',   0,  4),
('AKK', 2, 'P616',  0,  2),
('AKK', 2, 'PZ',    0,  1),
('AKK', 2, 'RF',    0,  4),
('AKL', 1, 'FA',    1,  4),
('AKL', 1, 'GO25',  0,  2),
('AKL', 1, 'GU25',  0,  2),
('AKL', 1, 'GV25',  0,  2),
('AKL', 1, 'K14',   1,  2),
('AKL', 1, 'K16',   0,  1),
('AKL', 1, 'K16',   1, 16),
('AKL', 1, 'K18',   1,  2),
('AKL', 1, 'K28',   0,  2),
('AKL', 1, 'KR24',  0,  2),
('AKL', 1, 'P18',   1,  2),
('AKL', 1, 'P24',   0,  4),
('AKL', 1, 'P624',  0,  2),
('AKL', 1, 'PZ',    0,  1),
('AKL', 1, 'RF',    0,  4),
('AKL', 1, 'RK11',  1,  4),
('AKL', 2, 'FA',    2,  4),
('AKL', 2, 'GO25',  0,  2),
('AKL', 2, 'GU25',  0,  2),
('AKL', 2, 'GV25',  0,  2),
('AKL', 2, 'K14',   2,  2),
('AKL', 2, 'K16',   0,  1),
('AKL', 2, 'K16',   2, 20),
('AKL', 2, 'K28',   0,  2),
('AKL', 2, 'KR24',  0,  2),
('AKL', 2, 'P24',   0,  4),
('AKL', 2, 'P624',  0,  2),
('AKL', 2, 'PZ',    0,  1),
('AKL', 2, 'RF',    0,  4),
('AKL', 2, 'RK11',  2,  4),
('CAH', 1, 'FA',    1,  4),
('CAH', 1, 'K18',   1,  2),
('CAH', 1, 'K28',   0,  2),
('CAH', 1, 'KR24',  0,  2),
('CAH', 1, 'P18',   1,  2),
('CAH', 1, 'P24',   0,  4),
('CAH', 1, 'P624',  0,  1),
('CAH', 1, 'PZ',    0,  1),
('CAH', 1, 'RF',    0,  4),
('CAH', 1, 'RK11',  1,  4),
('CAH', 2, 'FA',    2,  4),
('CAH', 2, 'K18',   2,  2),
('CAH', 2, 'K28',   0,  2),
('CAH', 2, 'KR24',  0,  2),
('CAH', 2, 'P18',   2,  2),
('CAH', 2, 'P24',   0,  4);


INSERT INTO Preisliste (TeileID, Farbe, Preis)
  (SELECT t.TeileID, t.Farbe, t.KalkKosten * 1.4 
   FROM   Teile AS t
   WHERE  TeileID IN ('AAG', 'AKK', 'AKL', 'CAH', 'CKK', 'CKL', 'FG'));

   
INSERT INTO Kunden(KdNr, KdName, KdStadt, Bonitaet) VALUES
(100, 'Walter',              'Siegen',     0),
(105, 'Aberer',              'Stuttgart', -1),
(112, 'Zander',              'Ulm',        2),
(123, 'Babel',               'Senden',     1),
(126, 'Moehne',              'Hamburg',    2),
(136, 'Zimmer',              'Ulm',       -1),
(137, 'Becker',              'Stuttgart',  0),
(142, 'Schmidt',             'München',    1),
(149, 'Womser',              'Trier',     -1),
(150, 'Dehling',             'Augsburg',   2),
(161, 'Kahlert',             'Stuttgart' , 1),
(164, 'Lummer',              'Bonn',      -2),
(177, 'Zwack',               'Senden',     2),
(182, 'Manger',              'Bochum',     1),
(184, 'Merz',                'Ulm',        2),
(185, 'Ketzer',              'Bremen',     0),
(188, 'Breu',                'Karlsruhe',  0),
(192, 'Dreher',              'Ulm',       -1),
(204, 'Fischer GmbH',        'München',    1),
(219, 'Beyer AG',            'Bretten',    2),
(222, 'Voigt & Co.',         'Flensburg', -1),
(237, 'Graf KG',             'Ulm',        1),
(251, 'Herrmann KG',         'Schwerin',  -1),
(260, 'Koch GmbH & Co.KG',   'Senden',     1),
(263, 'Fobke GmbH',          'Bremen',     2);


INSERT INTO AuftragsDS(AuftrNr, KdNr, KdAuftrNr, KdAuftrDatum, ErfassungsDatum, 
                       LgLieferung, AuftrStatus, AuftrAnFertigung, AuftrPosInArbeit, 
					   FertigungBeendet, WareAusliefBereit, AusliefDatum, 
                       RechnungsStatus, RechnungsDatum, Zahlungseingang) VALUES
(15,177,'12456-LG','2013-01-20','2013-01-23',0,1,'2013-01-24',1,NULL,NULL,NULL,0,NULL,NULL),
(16,123,'34567','2013-01-24','2013-01-26',0,1,'2013-01-30',1,NULL,NULL,NULL,0,NULL,NULL),
(17,136,'4973','2013-02-01','2013-02-03',1,5,NULL,NULL,NULL,'2013-02-08','2013-02-08',3,'2013-02-08','2013-02-22'),
(18,126,'111-4512','2013-01-27','2013-01-30',0,1,'2013-02-03',3,NULL,NULL,NULL,0,NULL,NULL),
(19,123,'CK-3301','2013-02-08','2013-02-10',1,5,NULL,NULL,NULL,'2013-02-15','2013-02-16',3,'2013-02-17','2013-02-28'),
(20,150,'77832','2013-02-12','2013-02-14',1,5,NULL,NULL,NULL,'2013-02-17','2013-02-21',2,'2013-02-22',NULL),
(21,123,'33011','2013-02-24','2013-02-27',1,3,NULL,NULL,NULL,'2013-03-02',NULL,0,NULL,NULL),
(22,251,'8891','2013-03-14','2013-03-16',0,1,'2013-03-19',2,NULL,NULL,NULL,0,NULL,NULL),
(24,219,'76345-002','2013-03-16','2013-03-20',0,1,'2013-03-23',1,NULL,NULL,NULL,0,NULL,NULL),
(26,150,'6780','2013-03-23','2013-03-26',NULL,0,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL),
(27,123,'45-211-38','2013-04-18','2013-04-20',NULL,0,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL);


INSERT INTO AuftragsPosDS(AuftrNr, AuftrPos, TeileID, Farbe, VkPreis, AnzVonKundeBestellt, 
                          AnzFuerKundeReserviert, AnzNochZuFertigen, BestAnfordErstellt, 
						  BestAnfordErledigt, FertigungGestartet, FertigungBeendet, 
						  Fertigungsstatus) VALUES
(15,1,'AAG',1,NULL,7,5,2,'2013-02-01',NULL,NULL,NULL,1),
(15,2,'AAG',2,NULL,4,4,0,NULL,NULL,NULL,NULL,0),
(15,3,'CAH',1,NULL,1,1,0,NULL,NULL,NULL,NULL,0),
(15,4,'CAH',2,NULL,1,1,0,NULL,NULL,NULL,NULL,0),
(15,5,'FG',1,NULL,3,3,0,NULL,NULL,NULL,NULL,0),
(15,6,'FG',2,NULL,3,3,0,NULL,NULL,NULL,NULL,0),
(16,1,'CKK',1,NULL,2,2,0,NULL,NULL,NULL,NULL,0),
(16,2,'FG',1,NULL,4,3,1,NULL,NULL,'2013-02-06',NULL,3),
(17,1,'AAG',2,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(17,2,'AKK',2,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(18,1,'CKK',1,NULL,6,4,2,'2013-02-08',NULL,NULL,NULL,1),
(18,2,'CKK',2,NULL,2,2,0,NULL,NULL,NULL,NULL,0),
(18,3,'FG',1,NULL,5,0,5,'2013-02-08',NULL,NULL,NULL,1),
(18,4,'AKL',2,NULL,1,0,1,NULL,NULL,'2013-02-08',NULL,3),
(18,5,'FG',2,NULL,2,2,0,NULL,NULL,NULL,NULL,0),
(19,1,'AAG',2,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(20,1,'CKL',2,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(20,2,'CAH',1,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(20,3,'CAH',2,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(20,4,'CKK',2,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(21,1,'AKK',1,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(21,2,'AKK',2,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(22,1,'CKL',2,NULL,1,0,1,NULL,NULL,NULL,NULL,0),
(22,2,'AAG',1,NULL,1,0,1,NULL,NULL,NULL,NULL,0),
(24,1,'CAH',2,NULL,2,2,0,NULL,NULL,NULL,NULL,0),
(24,2,'FG',2,NULL,4,1,3,NULL,NULL,NULL,NULL,0),
(24,3,'AKK',1,NULL,2,2,0,NULL,NULL,NULL,NULL,0),
(26,1,'AAG',2,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(27,1,'AKL',1,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,-1),
(27,2,'AKL',2,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,-1);


INSERT INTO Bestellanforderungen(AuftrNr, AuftrPos, UTeilID, UTeilFarbe, Anzahl, BestErledigtDatum,
                                 BestNr, BestPos, BestAnfStatus) VALUES
(15,1,'FA',1,8,'2013-02-23',1,1,2),
(15,1,'K14',1,18,'2013-02-27',2,1,2),
(15,1,'K16',1,56,'2013-02-24',3,1,2),
(15,1,'K18',1,4,'2013-02-27',2,2,2),
(15,1,'K28',0,4,'2013-02-28',4,1,2),
(15,1,'KR24',0,4,'2013-02-24',5,1,2),
(15,1,'P18',1,4,'2013-02-14',6,1,2),
(15,1,'P24',0,8,'2013-02-14',6,2,2),
(15,1,'P624',0,4,'2013-02-21',7,1,2),
(15,1,'PZ',0,2,'2013-02-28',8,1,2),
(15,1,'RF',0,8,'2013-02-13',9,1,2),
(15,1,'RK11',1,8,'2013-02-28',10,1,2),
(15,1,'TL134',1,2,'2013-02-13',11,1,2),
(15,1,'TR134',1,2,'2013-02-13',11,2,2),
(18,1,'FA',1,8,NULL,NULL,NULL,0),
(18,1,'GO25',0,4,NULL,NULL,NULL,0),
(18,1,'GU25',0,4,NULL,NULL,NULL,0),
(18,1,'GV25',0,4,NULL,NULL,NULL,0),
(18,1,'K16',0,2,NULL,NULL,NULL,0),
(18,1,'K28',0,4,NULL,NULL,NULL,0),
(18,1,'KR24',0,4,NULL,NULL,NULL,0),
(18,1,'P24',0,8,NULL,NULL,NULL,0),
(18,1,'P616',0,4,NULL,NULL,NULL,0),
(18,1,'PZ',0,2,NULL,NULL,NULL,0),
(18,1,'RF',0,8,NULL,NULL,NULL,0),
(18,3,'FA',1,20,NULL,NULL,NULL,0),
(18,3,'K28',0,10,NULL,NULL,NULL,0),
(18,3,'KR24',0,10,NULL,NULL,NULL,0),
(18,3,'P24',0,20,NULL,NULL,NULL,0),
(18,3,'RF',0,20,NULL,NULL,NULL,0);


INSERT INTO Lieferanten(LiefNr, LiefName, LiefStadt, Bewertung) VALUES
  (500, 'Märzen AG',           'Ulm',       -1),
  (503, 'Fischer GmbH',        'München',    0),
  (506, 'Kalbe & Söhne',       'Augsburg',   2),
  (522, 'Faulstich AG',        'Weinheim',   2),
  (527, 'Meier KG',            'Ulm',        1),
  (528, 'Schröter & Co.',      'Singen',     2),
  (540, 'Koch GmbH & Co.KG',   'Senden',    -2),
  (545, 'Damm Ltd.',           'Hamburg',    1),
  (556, 'Schwarz KG',          'Ulm',        2),
  (557, 'Otto GmbH',           'Köln',       1),
  (558, 'Mayer GmbH & Co.KG.', 'Stuttgart', -2),
  (559, 'Franck & Sohn',       'Konstanz',   0),
  (563, 'Hennig KG',           'Heilbronn',  1),
  (572, 'Aberer',              'Stuttgart',  2),
  (575, 'Zander',              'Ulm',        2),
  (584, 'Decker',              'München',    1),
  (644, 'Lammert',             'Augsburg',   2);

  
INSERT INTO Liefert(LiefNr, TeileID, Farbe, Preis) VALUES
  (500, 'FA',     1,   2.5),
  (500, 'FA',     2,   2.5),
  (503, 'GO25',   0,   3.0),
  (503, 'GU25',   0,   3.0),
  (503, 'GV25',   0,   1.0),
  (506, 'K14',    0,   1.3),
  (506, 'K14',    1,   1.4),
  (506, 'K16',    0,   1.8),
  (506, 'K16',    1,   1.8),
  (506, 'K18',    1,   2.4),
  (506, 'K28',    0,   2.6),
  (506, 'K28',    1,   2.7),
  (506, 'K28',    2,   2.7),
  (522, 'K14',    0,   1.2),
  (522, 'K14',    1,   1.2),
  (522, 'K14',    2,   1.3),
  (522, 'K16',    0,   1.7),
  (522, 'K16',    1,   1.9),
  (522, 'K16',    2,   1.8),
  (522, 'K18',    1,   2.2),
  (522, 'K18',    2,   2.2),
  (527, 'K28',    0,   2.4),
  (527, 'K28',    1,   2.5),
  (527, 'K28',    2,   2.5),
  (540, 'KR24',   0,   3.0),
  (545, 'P18',    1,   2.2),
  (545, 'P18',    2,   2.2),
  (545, 'P24',    0,   2.9),
  (545, 'TL134',  1,   4.3),
  (545, 'TL134',  2,   4.2),
  (545, 'TR134',  1,   4.3),
  (545, 'TR134',  2,   4.2),
  (556, 'P616',   0,   5.5),
  (556, 'P624',   0,   6.2),
  (557, 'PZ',     0,   2.4),
  (559, 'RF',     0,   2.7),
  (563, 'RK11',   1,   1.2),
  (563, 'RK11',   2,   1.2),
  (572, 'TL134',  1,   4.2),
  (572, 'TL134',  2,   4.4),
  (572, 'TR134',  1,   4.2),
  (572, 'TR134',  2,   4.4),
  (575, 'FA',     1,   2.6),
  (575, 'FA',     2,   2.7),
  (584, 'RF',     0,   2.6),
  (644, 'GO25',   0,   2.8),
  (644, 'GU25',   0,   2.8),
  (644, 'GV25',   0,   0.9);
   
 
INSERT INTO Bestellungen(BestNr, LiefNr, BestDatum, WareErhalten, AuftragsBest, LgZugangGebucht, Status) VALUES
(1,500,'2013-02-03','2013-02-23', 1, NULL,1),
(2,522,'2013-02-03','2013-02-27', 1, NULL,1),
(3,506,'2013-02-03','2013-02-24', 1, NULL,1),
(4,527,'2013-02-03','2013-02-28', 1, NULL,1),
(5,540,'2013-02-03','2013-02-24', 1, NULL,1),
(6,545,'2013-02-03','2013-02-14', 1, NULL,1),
(7,556,'2013-02-03','2013-02-21', 1, NULL,1),
(8,557,'2013-02-03','2013-02-28', 1, NULL,1),
(9,584,'2013-02-03','2013-02-13', 1, NULL,1),
(10,563,'2013-02-03','2013-02-28', 1, NULL,1),
(11,572,'2013-02-03','2013-02-13', 1, NULL,1),
(12,644,'2013-04-03',NULL, 1, NULL,0),
(13,522,'2013-04-18',NULL, 1, NULL,0);


INSERT INTO BestellPos(BestNr, BestPos, TeileID, Farbe, Anzahl) VALUES
(1,1,'FA',1,8),
(2,1,'K14',1,18),
(2,2,'K18',1,4),
(3,1,'K16',1,56),
(4,1,'K28',0,4),
(5,1,'KR24',0,4),
(6,1,'P18',1,4),
(6,2,'P24',0,8),
(7,1,'P624',0,4),
(8,1,'PZ',0,2),
(9,1,'RF',0,8),
(10,1,'RK11',1,8),
(11,1,'TL134',1,2),
(11,2,'TR134',1,2),
(12,1,'GO25',0,10),
(12,2,'GU25',0,10),
(12,3,'GV25',0,15),
(13,1,'K14',1,10),
(13,2,'K18',1,10);

  
INSERT INTO Abteilungen(AbtID, AbtName, MgrPersNr) VALUES
  ('BH', 'Buchhaltung', 				NULL),
  ('EK', 'Einkauf', 					NULL),
  ('FE', 'Forschung und Entwicklung', 	NULL),
  ('FP', 'Fuhrpark', 					NULL),
  ('FT', 'Fertigung', 					NULL),
  ('GF', 'Geschäftsführung', 			NULL),
  ('LG', 'Lager', 						NULL),
  ('PA', 'Personal', 					NULL),
  ('PL', 'Planung', 					NULL),
  ('RA', 'Rechtsabteilung', 	 		NULL),
  ('VK', 'Verkauf', 					NULL);
 

/* Erst die Leiter einfügen, da sonst Probleme wg. referentieller Integrität */

INSERT INTO Mitarbeiter(PersNr, Name, Vorname, UserID, Gehalt, AbtID) VALUES
  (49, 'Moser', 		'Luise', 	'moser_luise', 		9500.0, 'GF'),
  (47, 'Meyer', 		'Peter', 	'meyer_peter', 		4200.0, 'LG'),
  (21, 'Amman', 		'Thea', 	'amman_thea', 		7800.0, 'PA'),
  (45, 'Maier', 		'Achim', 	'maier_achim', 		7300.0, 'FT'),
  (71, 'Zemmler', 		'Max', 		'zemmler_max', 		6100.0, 'PL'),
  (40, 'Kramer', 		'Inge', 	'kramer_inge', 		7500.0, 'RA'),
  (64, 'Wammer', 		'Ruth', 	'wammer_ruth', 		6300.0, 'FP'),
  (51, 'Müller', 		'Ludwig', 	'müller_ludwig', 	8100.0, 'VK'),
  (76, 'Zoller', 		'Hans', 	'zoller_hans', 		6200.0, 'FE'),
  (62, 'Unger', 		'Martin', 	'unger_martin', 	6300.0, 'BH'),
  (65, 'Wanner', 		'Peter', 	'wanner_peter', 	7200.0, 'EK');

UPDATE Abteilungen SET MgrPersNr = 62 WHERE AbtID = 'BH';
UPDATE Abteilungen SET MgrPersNr = 65 WHERE AbtID = 'EK';
UPDATE Abteilungen SET MgrPersNr = 76 WHERE AbtID = 'FE';
UPDATE Abteilungen SET MgrPersNr = 64 WHERE AbtID = 'FP';
UPDATE Abteilungen SET MgrPersNr = 45 WHERE AbtID = 'FT';
UPDATE Abteilungen SET MgrPersNr = 49 WHERE AbtID = 'GF';
UPDATE Abteilungen SET MgrPersNr = 47 WHERE AbtID = 'LG';
UPDATE Abteilungen SET MgrPersNr = 21 WHERE AbtID = 'PA';
UPDATE Abteilungen SET MgrPersNr = 71 WHERE AbtID = 'PL';
UPDATE Abteilungen SET MgrPersNr = 40 WHERE AbtID = 'RA';
UPDATE Abteilungen SET MgrPersNr = 51 WHERE AbtID = 'VK';

ALTER TABLE Abteilungen
  ADD CONSTRAINT MgrPersNr_fk FOREIGN KEY (MgrPersNr) REFERENCES Mitarbeiter(PersNr);

INSERT INTO Mitarbeiter(PersNr, Name, Vorname, UserID, Gehalt, AbtID) VALUES
(20, 'Maier', 		'Ilse', 	'maier_ilse', 		3400.0, 'FE'),
(22, 'Bauer', 		'Isolde', 	'bauer_isolde', 	2000.0, 'LG'),
(23, 'Beck', 		'Theresa', 	'beck_theresa', 	2700.0, 'FP'),
(24, 'Aberer', 		'Karl', 	'aberer_karl', 		3400.0, 'EK'),
(25, 'Becker', 		'Susanne', 	'becker_susanne', 	3000.0, 'PA'),
(26, 'Becker', 		'Max', 		'becker_max', 		1800.0, 'LG'),
(27, 'Binder', 		'Jakob', 	'binder_jakob', 	2300.0, 'FP'),
(28, 'Brunner', 	'Bernd', 	'brunner_bernd', 	1750.0, 'LG'),
(29, 'Buck', 		'Hans', 	'buck_hans', 		1800.0, 'FT'),
(30, 'Cramer', 		'Pia', 		'cramer_pia', 		3300.0, 'BH'),
(31, 'Decker', 		'Paul', 	'decker_paul', 		4300.0, 'VK'),
(32, 'Deiters', 	'Uwe', 		'deiters_uwe', 		2800.0, 'FP'),
(33, 'Blick', 		'Hans', 	'blick_hans', 		3200.0, 'BH'),
(34, 'Zack', 		'Ulrich', 	'zack_ulrich', 		1800.0, 'FT'),
(35, 'Fliege', 		'Helga', 	'fliege_helga', 	2500.0, 'FP'),
(36, 'Frommert', 	'Margit', 	'frommert_margit',	3100.0, 'EK'),
(37, 'Gerke', 		'Sonja', 	'gerke_sonja', 		2700.0, 'BH'),
(38, 'Grupp', 		'Luise', 	'grupp_luise', 		1900.0, 'FT'),
(39, 'Heller', 		'Martin', 	'heller_martin', 	2900.0, 'BH'),
(41, 'Kratzer', 	'Lothar', 	'kratzer_lothar', 	3400.0, 'FE'),
(42, 'Lauer', 		'Frida', 	'lauer_frida', 		2300.0, 'BH'),
(43, 'Lummer', 		'Hans', 	'lummer_hans', 		2900.0, 'BH'),
(44, 'Mack', 		'Petra', 	'mack_petra', 		2100.0, 'RA'),
(46, 'Mayer', 		'Jürgen', 	'mayer_jürgen', 	2800.0, 'PA'),
(48, 'Moser', 		'Hartmut', 	'moser_hartmut', 	3800.0, 'VK'),
(50, 'Müller', 		'Tobias', 	'müller_tobias', 	2900.0, 'PA'),
(52, 'Müller', 		'Heinz', 	'müller_heinz', 	3300.0, 'BH'),
(53, 'Müller', 		'Heinrich',	'müller_heinrich', 	3600.0, 'VK'),
(54, 'Neubert', 	'Irene', 	'neubert_irene', 	1800.0, 'FT'),
(55, 'Ohlig', 		'Paula', 	'ohlig_paula', 		2900.0, 'BH'),
(56, 'Opitz', 		'Karla', 	'opitz_karla', 		1800.0, 'LG'),
(57, 'Pauler', 		'Thomas', 	'pauler_thomas', 	2000.0, 'FT'),
(58, 'Pfleiderer',	'Ilse', 	'pfleiderer_ilse', 	3000.0, 'BH'),
(59, 'Schmalz', 	'Bernd', 	'schmalz_bernd', 	3500.0, 'VK'),
(60, 'Schmidt', 	'Karl', 	'schmidt_karl', 	2700.0, 'FP'),
(61, 'Trencker', 	'Paula', 	'trenker_paula', 	4300.0, 'PL'),
(63, 'Wacker', 		'Petra', 	'wacker_petra', 	3700.0, 'VK'),
(66, 'Wauke', 		'Magda', 	'wauke_magda', 		2700.0, 'BH'),
(67, 'Weck', 		'Helga', 	'weck_helga', 		1900.0, 'GF'),
(68, 'Wecker', 		'Lutz', 	'wecker_lutz', 		3200.0, 'BH'),
(69, 'Zander', 		'Philip', 	'zander_philip', 	3800.0, 'PL'),
(70, 'Zapf', 		'Jens', 	'zapf_jens', 		3300.0, 'BH'),
(72, 'Zenker', 		'Beate', 	'zenker_beate', 	2000.0, 'FT'),
(73, 'Zick', 		'Udo', 		'zick_udo', 		2900.0, 'FP'),
(74, 'Zimmer', 		'Renate', 	'zimmer_renate', 	1850.0, 'FT'),
(75, 'Zobel', 		'Rita', 	'zobel_rita', 		3200.0, 'PA'),
(77, 'Zorke', 		'Inge', 	'zorke_inge', 		3900.0, 'VK'),
(78, 'Zundel', 		'Moritz', 	'zundel_moritz', 	4100.0, 'VK');
