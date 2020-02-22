# BattleShips

BattleShips ist ein Mobilegame für die Android-Plattform. Es ist ein Schiffe versenken mit
Multiplayerfunktionalität.

* Lena Papailiou
* Ramona Koksa
* Sandro Bürki

FS 2020

## Funktionalitäten

Im folgenden Abschnitt wird die Projektvision kurz erklärt. Weder sind sie nach Priorität
gegliedert, noch stecken sie den Scope der Applikation ab. Ob ein Feature es also schlussendlich in
die finale Version schafft, ist hier nicht gegeben. Vorgegeben sind die Zeit durch den Termin der finalen
Abgabe und die personellen Ressourcen mit den drei Teammitgliedern. Als Priorisierung und
genauere Aufteilung innerhalb der Features soll der Backlog dienen.

### Intro Board

Beim Starten der Applikation gelangt der Benutzer auf das Into Board. Hier soll
* ein Benutzername gewählt werden können, welcher dem Gegenspieler später angezeigt wird.
* der Spielmodi gewählt werden können. Unterschieden wird zwischen offline gegen eine KI und online.
* das Spiel gestartet werden können.
* ein Link zu den Spielregeln vorhanden sein. Optimalerweise eine In-App Page.
* ein Link zum Ranking vorhanden sein.

### Spiel

Jeder Spieler verfügt zu Beginn eines Spieles über insgesammt zehn Schiffe (in Klammern jeweils
ihre Grösse): ein Schlachtschiff (4 Zellen), zwei Kreuzer (je 3 Zellen), drei Zerstörer (je 2 Zellen)
und vier U-Boote (je 1 Zellen). Initial werden sie zufällig auf dem Spielfeld positioniert. Mit
Drag & Drop können sie verschoben werden. Durch einen Tap auf ein Schiff dreht sich dieses um 90°.

Für das Spiel an sich gelten die allseits bekannten Spielregeln. Pro Runde kann nur einmal geschossen
werden, ausser bei einem Treffer. Hierbei kann so lange weitergeschossen werden, bis Wasser getroffen
wurde. Sobald ein Zug vollzogen wurde, wird die Gegenseite darüber informiert. Gewonnen hat, wer
zuerst alle Schiffe des Gegners versenkt. Ein Spiel kann auch aufgegeben werden. Falls in einem Spiel
länger als 10 Tage nicht geschossen wurde, gewinnt automatisch der Spieler, welcher nicht am Zug war.

Während des Spiels kann zwischen zwei Boardansichten gewechselt werden. Auf dem eigenen Board sind
alle eigenen Schiffe zu sehen, sowie die bisherigen Schüsse des Gegners. Diese sind jeweils mit
entweder einer Explosion auf einem Schiff oder mit Wellen im Wasser gekennzeichnet. Auf dem Gegnerboard
sind man alle bisherigen Schüsse sowie Treffer markiert, welche man selbst bereits geschossen hat.

### Punktesystem

Beim Gewinnen eines Spiels erhält man 5 Punkte. Beim Verlieren 0. Auf der Startseite lässt sich eine
Page öffnen, welche das aktuelle Ranking aufzeigt.

## Offlinefähigkeit

Die Applikation soll Schüsse, welche offline getätigt wurden zwischenspeichern und bei der nächsten
funktionierenden Internetverbindung hochladen.
