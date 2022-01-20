# ynvest - Online-Broker
![Header Image](src/main/resources/static/img/ynvest_header.jpg)

Der Online-Broker `ynvest` ermöglicht dem Kunden online Wertpapiere
zu handeln.

## Partner Projekte
### Externe Schnittstellen
- Stefan Butz: `yetra` (Börse)
  - JMS: Asynchrone Orderbestätigung
- Sina Amann: `eBank` (Bank)
  - REST: Kontostand aktualisieren
### Externe Abhängigkeiten
- Stefan Butz: `yetra` (Börse)
  - REST: Auftrag erstellen
  - REST: Nach Aktien suchen
  - REST: Aktienwerte mit ISIN-Liste abrufen
  - REST: Aktiendetails abrufen
  - JMS: Asynchrone Orderbestätigung
- Sina Amann: `eBank` (Bank)
  - REST: Konto erstellen
  - REST: Überweisung tätigen

## Besonderheiten
- Zur Ansteuerung der REST-Partnerschnittstellen wurde nicht
  die Klasse RestTemplate sondern WebClient verwendet. WebClient
  ist der offizielle Nachfolger der als veraltet markierten
  Klasse RestTemplate. Ein weiterer Vorteil von WebClient ist
  die einfache Authentifizierung über HTTP Basic Authentication.
- Um Bootstrap effizienter zu personalisieren, wurde die
  Node-Version von Bootstrap verwendet. Kompiliert werden die
  Dateien (*.scss) mittels eines Frontend Maven Plugins,
  welches vor dem Startup der Applikation die nötigen Dependencies
  über NPM installiert und schließlich die Dateien kompiliert.
- Auf einen bereitgestellten Testnutzer wurde verzichtet,
  um den Registrierprozess mit Verifizierung zu verdeutlichen.