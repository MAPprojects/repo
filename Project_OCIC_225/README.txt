Rapoarte:
    Pentru salvarea rapoartelor in format pdf am folosit libraria iText.
    http://www.java2s.com/Code/Jar/i/Downloaditextpdf540jar.htm

Baza de date:
    Pentru salvarea datelor am folosit Microsoft SQL Server, fara ORM.
    Datele sunt incarcate paginat din baza de date.

Security:
    Exista doua tipuri de utilizatori:
        - profesorul: are acces la operatiile CRUD pentru entitatile Student,Tema de laborator si Nota
        - student : poate vizualiza informatiile despre el(nume, grupa, media curenta etc),toate temele de laborator,notele acestuia

Mail:
    Pentru trimiterea de mailuri am folosit API-ul Simple Java Mail.
    http://www.simplejavamail.org
