# Lagerverwaltung mit Android-App
Eine Lagerverwaltungssoftware, die über eine Verwaltungoberfläche gepflegt werden kann. Mit hilfe der Android-App können Artikel aus dem Lager ausgebucht werden.

## Software:
Die Software funktioniert im Model-View-Controller Prinzip.
### Server:
Es gibt eine Server-Anwendung, welche sich mit der Datenbank verbindet.
Im Server sind alle Funktionen (SQL-Querys) enthalten, welche von der Verwaltungsoberfläche oder App gebraucht werden.

### Verwaltungsoberfläche:
Eine Verwaltungsoberfläche, welche sich mit dem Server verbindet und Daten in die Datenbank liest/schreibt. Über diese Verwaltungsoberfläche können Artikel, Kundenstamm, Bestellungen erstellt werden. 
Erstellte Artikel werden automatisch in einem freien Lagerplatz angelegt. Bestellungen können für alle Kunden im Kundenstamm erstellt und bestellte Artikel hinzugefügt werden.
Für jeden erstellten Artikel wird ein Barcode generiert, welcher zum Ausbuchen über die App nötig ist.
Für Lagerbestand und Kunden können jeweils Bestandslisten generiert/gedruckt werden. 
Bei Bestellungen können Lieferschein/Rechnungen erstellt/gedruckt werden.

--Alle verfügbaren Lagerplätze werden Random von der Software belegt und bei einem Lagerbestand von <1 wieder freigegeben.--

## Android-App:
Mit hilfe der App können Artikel aus dem Lager ausgebucht werden. Dazu verbindet sich die App mit dem Server um Daten aus der Datenbank zu lesen/schreiben.
Alle offenen Bestellungen, welche über die Verwaltungsoberfläche erstellt wurden, werden in der App angezeigt. 
Die in der Bestellung beinhaltenden Artikel können anhand eines Barcodes aus dem Lager ausgebucht werden.
Wenn alle Artikel der Bestellung ausgebucht wurden, wird der Status der Bestellung auf 'Done' gesetzt und verschwindet aus der Liste.