# Verlag

Project for my software architecture course.

# TO-DOs
- Entität Buch: @EqualsAndHashCode. Include bei ISBN drinnen behalten oder raus?
- Entität Buch: @ISBN prüft generell auf ISBN. Nicht nur auf ISBN-13 glaube ich. Daher Umbau ISBN-13 auf ISBN?
- schema.graphqls: Buch Nebentitel not null? Aber leerer String erlaubt? --> wie spezifizieren? Darf null sein? Wie wird null verarbeitet? Kommt dann ein leerer String an?
- bei allen Entitäten prüfen, dass ich nicht z.B. Verlag.verlagsname oder Ticket.ticketID stehen habe. Dann lieber Ticket.id oder Verlag.name.
- Annotationen bei mir einbauen. Alle neuen Dateien abgleichen. VerlagInput.java fehlen die Annotationen
- VerlagInputMapper: Kommentare raus
- z.B. PreisInput: Zeilenumbrüche bei den Attributen? Wie im Kunde-Projekt?
- bei Style-Regeln, die wir nicht definiert haben, an die von Jürgen halten. Also in seinem Projekt.
