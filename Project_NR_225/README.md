Security : 3 tipuri de utilizatori (admin, profesor, student) cu drepturi diferite in aplicatie. Adminul poate adauga studenti si utilizatori, cat si reseta parole. Profesorul poate adauga teme, poate da note si poate genera rapoarte despre notele studentilor, iar studentii pot vedea detalii despre propriile note. Toti utilizatori isi pot modifica parola asociata contului. Utilizatorii sunt stocati prin username si parola prin hash cu salt.

Database : Am folosit Apache Derby, baza de date fiind embeded / locala in aplicatie.

Rapoarte : Pentru generarea de rapoarte in pdf am folosit iTextPdf 7 pentru a converti cod din html (mai usor de facut designul raportului) in pdf.

Stilizare : Pentru stilizarea aplicatiei am incercat sa respect modelul de material design, folosind libraria JFoenix