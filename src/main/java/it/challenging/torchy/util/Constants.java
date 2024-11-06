/*
 * Copyright (c) 2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.util;

public class Constants {

    public static final String NOTE_LOST_PASSWORD                 = "Salve, la nuova password da cambiare entro 24h è la seguente \n ";
    public static final String OGGETTO_LOST_PASSWORD              = "Nuovo accesso a Torchy ";
    public static final String OGGETTO_LOST_PASSWORD_4MAPP        = "Nuovo accesso a 4Mapp ";

    public static final String STATO_CF_DISPONIBILE     = "CF Disponibile";
    public static final String STATO_CF_INVIATO         = "CF Inviato";
    public static final String STATO_QM_OK              = "Qualification Meeting  OK";
    public static final String STATO_QM_KO              = "Qualification Meeting  KO";
    public static final String STATO_FOLLOW_UP_POOL     = "Follow up Pool";
    public static final String STATO_FOLLOW_UP_POSITIVO = "Follow up Positivo";
    public static final String STATO_STAFFING           = "Staffing";
    public static final String STATO_TEMPORARY          = "Temporary";
    public static final String STATO_RECRUITING         = "Recruiting";
    public static final String STATO_HEAD_HUNTING       = "Head Hunting";

    // ---------------- AI ------------------
    //DEFAULT
    public static final String AI_MESSAGE_DEFAULT                    = "Non riesco a soddisfare la tua richiesta, prova a riformulare la domanda.";

    //Candidati Message
    public static final String AI_MESSAGE_CANDIDATO_NOME             = "Non riesco a trovare il nome che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_COGNOME          = "Non riesco a trovare il cognome che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_ANNI_ESPERIENZA  = "Non riesco a trovare candidati con questi anni di esperienza, provane di diversi o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_CELLULARE        = "Non riesco a trovare candidati con questo cellulare, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_CITTA            = "Non riesco a trovare candidati che vivono in questa città, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_SKILL            = "Non riesco a trovare candidati con queste skill, provane di diverse o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_FACOLTA          = "Non riesco a trovare candidati cha hanno studiato in questa facoltà, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_EMAIL            = "Non riesco a trovare candidati con questa email, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_JOB_TITLE        = "Non riesco a trovare candidati con questo job title, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_DISPONIBILITA    = "Non riesco a trovare candidati con questa disponibilità, prova a cercare per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_OWNER            = "Non riesco a trovare candidati con questo owner, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_LIVELLO          = "Non riesco a trovare candidati con questo livello di studio, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_TIPO_RICERCA     = "Non riesco a trovare candidati con questo tipo di ricerca, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_TIPO_CANDIDATURA = "Non riesco a trovare candidati con questo tipo di candidatura, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_TIPO             = "Non riesco a trovare candidati con questo tipo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_MODALITA         = "Non riesco a trovare candidati con questa modalità, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_RAL              = "Non riesco a trovare candidati con questa RAL, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_CANDIDATO_RATING           = "Non riesco a trovare candidati con questo rating, provane uno diverso o cerca per altri parametri.";

    //Need Message
    public static final String AI_MESSAGE_NEED_TIPO                  = "Non riesco a trovare need di questo tipo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_STATO                 = "Non riesco a trovare need con questo stato, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_OWNER                 = "Non sono presenti need appartenenti a questo owner, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_SCREENING             = "Non riesco a trovare need in screening, prova a cercare per altri parametri.";
    public static final String AI_MESSAGE_NEED_PUBBLICAZIONE         = "Non riesco a trovare need in pubblicazione, prova a cercare per altri parametri.";
    public static final String AI_MESSAGE_NEED_PRIORITA              = "Non riesco a trovare need con questa priorità, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_WEEK                  = "Non riesco a trovare need pubblicati nella settimana richiesta, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_DATA_RICHIESTA        = "Non riesco a trovare need pubblicati nella data richiesta, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_SKILL                 = "Non riesco a trovare need con queste skill, provane diverse o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_LOCATION              = "Non sono presenti need in queste località, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_ANNI_ESPERIENZA       = "Non sono presenti need con questi anni di esperienza, provane diversi o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_RISORSE               = "Non riesco a trovare need con questo numero di risorse, provane diverse o cerca per altri parametri.";
    public static final String AI_MESSAGE_NEED_DESCRIZIONE           = "Non riesco a trovare need con questa descrizione, provane una diversa o cerca per altri parametri.";

    //Aziende Message
    public static final String AI_MESSAGE_AZIENDE_SETTORE            = "Non riesco a trovare il settore che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_TIPOLOGIA          = "Non riesco a trovare clienti di questo tipo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_STATO              = "Non riesco a trovare lo stato selezionato, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_OWNER              = "Non riesco a trovare l'owner che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_SEMPLICITA         = "Non sono presenti clienti con questo livello di semplicità, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_POTENZIALITA       = "Non sono presenti clienti con questo livello di potenzialità, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_SITO               = "Non riesco a trovare il sito che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_SCADENZA_CONTRATTO = "Non riesco a trovare clienti con contratto in scadenza nel periodo da te indicato, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_CODICE_DESTINATARIO= "Non riesco a trovare clienti con questo codice destinatario, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_PEC                = "Non riesco a trovare clienti con questa pec, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_EMAIL              = "Non riesco a trovare clienti con questa email, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_PROVINCE           = "Non sono presenti clienti in questa provincia, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_INDIRIZZO          = "Non sono presenti clienti a questo indirizzo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_CAP                = "Non sono presenti clienti in questo CAP, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_CITTA              = "Non sono presenti clienti in questa città, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_PI                 = "Non riesco a trovare la partita iva che stai cercando, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_CF                 = "Non riesco a trovare il codice fiscale che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_SEDE_LEGALE        = "Non sono presenti clienti con questa sede legale, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_SEDE_OPERATIVA     = "Non sono presenti clienti con questa sede operativa, provane una diversa o cerca per altri parametri.";
    public static final String AI_MESSAGE_AZIENDE_DESCRIZIONE        = "Non riesco a trovare clienti con questa ragione sociale, provane uno diverso o cerca per altri parametri.";

    //KeyPeople Message
    public static final String AI_MESSAGE_KEY_PEOPLE_AZIONI          = "Non riesco a trovare azioni per questo contatto, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_CELLULARE       = "Non riesco a trovare il cellulare che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_ULTIMA_ATTIVITA = "Non sono presenti contatti che hanno eseguito un'attività in questo periodo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_TIPOLOGIA       = "Non riesco a trovare contatti di questo tipo, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_OWNER           = "Non contatti con questo owner, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_EMAIL           = "Non riesco a trovare l'email che stai cercando, provane uno diverso o cerca per altri parametri.";
    public static final String AI_MESSAGE_KEY_PEOPLE_NOME            = "Non riesco a trovare contatti con questo nome, provane uno diverso o cerca per altri parametri.";

    //KEY WORD
    public static final String ALMENO           = "almeno";
    public static final String PIU              = "più";
    public static final String MASSIMO          = "massimo";
    public static final String MENO             = "meno";
    public static final String PARENTESI_CHIUSA = " )";

    //KEY FIND
    public static final String FIND_CERCA     = "cerca";
    public static final String FIND_CERCAMI   = "cercami";
    public static final String FIND_DAI       = "dai";
    public static final String FIND_DAMMI     = "dammi";
    public static final String FIND_RICERCA   = "ricerca";
    public static final String FIND_RICERCAMI = "ricercami";
    public static final String FIND_RITROVA   = "ritrova";
    public static final String FIND_RITROVAMI = "ritrovami";
    public static final String FIND_TROVA     = "trova";
    public static final String FIND_TROVAMI   = "trovami";

    //KEY ORDER
    public static final String ORDER_ORDINA     = "ordina";
    public static final String ORDER_ORDINAMI   = "ordinami";
    public static final String ORDER_RIORDINAMI = "riordinami";
    public static final String ORDER_RIORDINA   = "riordina";
    public static final String ORDER_RIORDINATI = "riordinati";
    public static final String ORDER_ORDINATI   = "ordinati";
    public static final String ORDER_PER        = "per";

    //KEY LIMIT
    public static final String LIMIT_PRIMI  = "primi";
    public static final String LIMIT_ULTIMI = "ultimi";

    //KEY AZIENDE TEXT
    public static final String AZIENDE_AZIENDE  = "aziende";
    public static final String AZIENDE_AZIENDA  = "azienda";
    public static final String AZIENDE_CLIENTE  = "cliente";
    public static final String AZIENDE_CLIENTI  = "clienti";
    public static final String AZIENDE_BUSINESS = "business";

    //KEY AZIENDE QUERY
    public static final String AZIENDE_DENOMINAZIONE           = "denominazione";
    public static final String AZIENDE_DESCRIZIONE             = "descrizione";
    public static final String AZIENDE_NOME                    = "nome";
    public static final String AZIENDE_CHIAMANO                = "chiamano";
    public static final String AZIENDE_RAGIONE_SOCIALE         = "ragione sociale";
    public static final String AZIENDE_SEDE_OPERATIVA          = "sede operativa";
    public static final String AZIENDE_SEDE_LEGALE             = "sede legale";
    public static final String AZIENDE_PI                      = "partita iva";
    public static final String AZIENDE_CF                      = "cf";
    public static final String AZIENDE_CODICE_FISCALE          = "codice fiscale";
    public static final String AZIENDE_INDIRIZZO               = "indirizzo";
    public static final String AZIENDE_CAP                     = "cap";
    public static final String AZIENDE_CITTA                   = "citta";
    public static final String AZIENDE_PROVINCIA               = "provincia";
    public static final String AZIENDE_EMAIL                   = "email";
    public static final String AZIENDE_PEC                     = "pec";
    public static final String AZIENDE_CODICE_DESTINATARIO     = "codice destinatario";
    public static final String AZIENDE_SITO                    = "sito";
    public static final String AZIENDE_INTERNET                = "internet";
    public static final String AZIENDE_TIPOLOGIA               = "tipologia";
    public static final String AZIENDE_STATO                   = "stato";
    public static final String AZIENDE_STATUS                  = "status";
    public static final String AZIENDE_POTENZIALITA            = "potenzialità";
    public static final String AZIENDE_SEMPLICITA              = "semplicità";
    public static final String AZIENDE_IDA                     = "ida";
    public static final String AZIENDE_SCADE                   = "scade";
    public static final String AZIENDE_SCADENZA_DEL_CONTRATTO  = "scadenza del contratto";
    public static final String AZIENDE_SCADENZA_CONTRATTO      = "scadenza contratto";
    public static final String AZIENDE_SETTORE                 = "settore";
    public static final String AZIENDE_SETTORE_DI_MERCATO      = "settore di mercato";
    public static final String AZIENDE_SETTORE_MERCATO         = "settore mercato";
    public static final String AZIENDE_OWNER                   = "owner";
    public static final String AZIENDE_PROPRIETARIO            = "proprietario";

    //KEY AZIENDE WHERE
    public static final String AZIENDE_WHERE_DESCRIZIONE         = " and c.descrizione = ";
    public static final String AZIENDE_WHERE_SEDE_OPERATIVA      = " and c.sede_operativa = ";
    public static final String AZIENDE_WHERE_SEDE_LEGALE         = " and c.sede_legale = ";
    public static final String AZIENDE_WHERE_CF                  = " and c.cf = ";
    public static final String AZIENDE_WHERE_PI                  = " and c.pi = ";
    public static final String AZIENDE_WHERE_CITTA               = " and c.citta = ";
    public static final String AZIENDE_WHERE_CAP                 = " and c.cap = ";
    public static final String AZIENDE_WHERE_INDIRIZZO           = " and c.indirizzo = ";
    public static final String AZIENDE_WHERE_PROVINCE            = " and c.provincia = ";
    public static final String AZIENDE_WHERE_EMAIL               = " and c.email = ";
    public static final String AZIENDE_WHERE_PEC                 = " and c.pec = ";
    public static final String AZIENDE_WHERE_CODICE_DESTINATARIO = " and c.codice_destinatario = ";
    public static final String AZIENDE_WHERE_SITO                = " and c.sito = ";
    public static final String AZIENDE_WHERE_POTENZIALITA        = " and c.potenzialita = ";
    public static final String AZIENDE_WHERE_SEMPLICITA          = " and c.semplicita = ";
    public static final String AZIENDE_WHERE_OWNER               = " and c.owner in ( ";
    public static final String AZIENDE_WHERE_STATI               = " and c.status = ";
    public static final String AZIENDE_WHERE_TIPOLOGIA           = " and c.tipologia in ( ";
    public static final String AZIENDE_WHERE_SETTORE             = " and c.settoreMercato = ";

    //KEY CANDIDATI TEXT
    public static final String CANDIDATI_CANDIDATI    = "candidati";
    public static final String CANDIDATI_CANDIDATO    = "candidato";
    public static final String CANDIDATI_SVILUPPATORI = "sviluppatori";

    //KEY CANDIDATI QUERY
    public static final String CANDIDATI_NOME                 = "nome";
    public static final String CANDIDATI_CHIAMANO             = "chiamano";
    public static final String CANDIDATI_COGNOME              = "cognome";
    public static final String CANDIDATI_EMAIL                = "email";
    public static final String CANDIDATI_CELLULARE            = "cellulare";
    public static final String CANDIDATI_TELEFONINO           = "telefonino";
    public static final String CANDIDATI_TELEFONO             = "telefono";
    public static final String CANDIDATI_CITTA                = "citta";
    public static final String CANDIDATI_VIVE                 = "vive";
    public static final String CANDIDATI_ABITA                = "abita";
    public static final String CANDIDATI_DATA_DI_NASCITA      = "data di nascita";
    public static final String CANDIDATI_NATO                 = "nato";
    public static final String CANDIDATI_ANNI                 = "anni";
    public static final String CANDIDATI_ANNI_DI_ESPERIENZA   = "anni di esperienza";
    public static final String CANDIDATI_ESPERIENZA           = "esperienza";
    public static final String CANDIDATI_MODALITA             = "modalita";
    public static final String CANDIDATI_LIVELLO_SCOLASTICO   = "livello scolastico";
    public static final String CANDIDATI_TITOLO               = "titolo";
    public static final String CANDIDATI_TITOLO_DI_STUDIO     = "titolo di studio";
    public static final String CANDIDATI_TIPOLOGIA            = "tipologia";
    public static final String CANDIDATI_JOB_TITLE            = "job title";
    public static final String CANDIDATI_SKILL                = "skill";
    public static final String CANDIDATI_SKILLATO             = "skillato";
    public static final String CANDIDATI_CONOSCE              = "conosce";
    public static final String CANDIDATI_DISPONIBILITA        = "disponibilità";
    public static final String CANDIDATI_DISPONIBILE          = "disponibile";
    public static final String CANDIDATI_STATO                = "stato";
    public static final String CANDIDATI_DATA_ULTIMO_CONTATTO = "ultimo contatto";
    public static final String CANDIDATI_RATING               = "rating";
    public static final String CANDIDATI_RAL                  = "ral";
    public static final String CANDIDATI_TIPO                 = "tipo";
    public static final String CANDIDATI_TIPO_CANDIDATURA     = "tipo candidatura";
    public static final String CANDIDATI_TIPO_RICERCA         = "tipo ricerca";
    public static final String CANDIDATI_FACOLTA              = "facolta";
    public static final String CANDIDATI_HA_STUDIATO          = "ha studiato";
    public static final String CANDIDATI_OWNER                = "owner";
    public static final String CANDIDATI_PROPRIETARIO         = "proprietario";

    //KEY CANDIDATI WHERE
    public static final String CANDIDATI_WHERE_NOME           = " and c.nome = ";
    public static final String CANDIDATI_WHERE_COGNOME        = " and c.cognome = ";
    public static final String CANDIDATI_WHERE_ESP_MAG        = " and c.anni_esperienza_ruolo >= ";
    public static final String CANDIDATI_WHERE_ESP_MIN        = " and c.anni_esperienza_ruolo <= ";
    public static final String CANDIDATI_WHERE_ESP_EQ         = " and c.anni_esperienza_ruolo = ";
    public static final String CANDIDATI_WHERE_CELLULARE      = " and c.cellulare = ";
    public static final String CANDIDATI_WHERE_CITTA          = " and c.residenza = ";
    public static final String CANDIDATI_WHERE_SKILL          = " and sc.id_skill in ( ";
    public static final String CANDIDATI_WHERE_FACOLTA        = " and fac.id_facolta in ( ";
    public static final String CANDIDATI_WHERE_EMAIL          = " and c.email = ";
    public static final String CANDIDATI_WHERE_JOB_TITLE      = " and tc.id_tipologia in ( ";
    public static final String CANDIDATI_WHERE_DISPONIBILITA  = " and c.disponibilita = ";
    public static final String CANDIDATI_WHERE_OWNER          = " and co.id_owner in ( ";
    public static final String CANDIDATI_WHERE_LIVELLI        = " and lc.id_livello in ( ";
    public static final String CANDIDATI_WHERE_TIPO_RICERCA   = " and trc.id_tipologia in ( ";
    public static final String CANDIDATI_WHERE_TIPO_CANDI     = " and tcc.id_tipologia in ( ";
    public static final String CANDIDATI_WHERE_TIPO           = " and ttc.id_tipo in ( ";
    public static final String CANDIDATI_WHERE_MODALITA       = " and c.modalita = ";
    public static final String CANDIDATI_WHERE_RAL            = " and c.ral = ";
    public static final String CANDIDATI_WHERE_RATING_MAG     = " and c.rating >= ";
    public static final String CANDIDATI_WHERE_RATING_MIN     = " and c.rating <= ";
    public static final String CANDIDATI_WHERE_RATING_EQ      = " and c.rating = ";

    //KEY KEYPEOPLE TEXT
    public static final String KEY_PEOPLE_KEY_PEOPLE = "key people";
    public static final String KEY_PEOPLE_KEYPEOPLE  = "keypeople";
    public static final String KEY_PEOPLE_CONTACT    = "contact";
    public static final String KEY_PEOPLE_CONTACTS   = "contacts";

    //KEY KEYPEOPLE QUERY
    public static final String KEY_PEOPLE_NOME                 = "nome";
    public static final String KEY_PEOPLE_EMAIL                = "email";
    public static final String KEY_PEOPLE_CELLULARE            = "cellulare";
    public static final String KEY_PEOPLE_CLIENTE              = "cliente";
    public static final String KEY_PEOPLE_AZIENDA              = "azienda";
    public static final String KEY_PEOPLE_BUSINESS             = "business";
    public static final String KEY_PEOPLE_RUOLO                = "ruolo";
    public static final String KEY_PEOPLE_DATA_ULTIMA_ATTIVITA = "ultima attività";
    public static final String KEY_PEOPLE_OWNER                = "owner";
    public static final String KEY_PEOPLE_PROPRIETARIO         = "proprietario";
    public static final String KEY_PEOPLE_AZIONI               = "azioni";
    public static final String KEY_PEOPLE_TIPO                 = "tipo";
    public static final String KEY_PEOPLE_TIPOLOGIA            = "tipologia";

    //KEY KEY_PEOPLE WHERE
    public static final String KEY_PEOPLE_WHERE_NOME      = " and k.nome = ";
    public static final String KEY_PEOPLE_WHERE_EMAIL     = " and k.email = ";
    public static final String KEY_PEOPLE_WHERE_OWNER     = " and ko.id_owner in ( ";
    public static final String KEY_PEOPLE_WHERE_TIPOLOGIA = " and k.tipo in ( ";
    public static final String KEY_PEOPLE_WHERE_CELLULARE = " and k.cellulare = ";



    //KEY NEED TEXT
    public static final String NEED_NEED = "need";

    //KEY NEED QUERY
    public static final String NEED_PROGRESSIVO = "progressivo";
    public static final String NEED_DESCRIZIONE = "descrizione";
    public static final String NEED_CHIAMATO = "chiamato";
    public static final String NEED_ANNI_DI_ESPERIENZA = "anni di esperienza";
    public static final String NEED_ESPERIENZA = "esperienza";
    public static final String NEED_RISORSE = "risorse";
    public static final String NEED_RICHIESTA = "richiesta";
    public static final String NEED_RICHIESTO = "richiesto";
    public static final String NEED_LOCATION = "location";
    public static final String NEED_LUOGO = "luogo";
    public static final String NEED_LOCALITA = "localita";
    public static final String NEED_SKILL = "skill";
    public static final String NEED_TIPOLOGIA = "tipologia";
    public static final String NEED_CLIENTE = "cliente";
    public static final String NEED_AZIENDA = "azienda";
    public static final String NEED_BUSINESS = "business";
    public static final String NEED_OWNER = "owner";
    public static final String NEED_PROPRIETARIO = "proprietario";
    public static final String NEED_KEY_PEOPLE = "key people";
    public static final String NEED_KEYPEOPLE = "keypeople";
    public static final String NEED_CONTATTO = "contatto";
    public static final String NEED_CONTACT = "contact";
    public static final String NEED_TIPO = "tipo";
    public static final String NEED_PRIORITA = "priorita";
    public static final String NEED_WEEK = "week";
    public static final String NEED_SETTIMANA = "settimana";
    public static final String NEED_STATO = "stato";
    public static final String NEED_ASSOCIATI = "associati";
    public static final String NEED_PUBBLICAZIONE = "pubblicazione";
    public static final String NEED_SCREENING = "screening";

    //KEY NEED WHERE
    public static final String NEED_WHERE_LOCATION       = " and n.location = ";
    public static final String NEED_WHERE_DESCRIZIONE    = " and n.descrizione = ";
    public static final String NEED_WHERE_ESP_MAG        = " and n.anni_esperienza >= ";
    public static final String NEED_WHERE_ESP_MIN        = " and n.anni_esperienza <= ";
    public static final String NEED_WHERE_ESP_EQ         = " and n.anni_esperienza = ";
    public static final String NEED_WHERE_RISORSE_MAG    = " and n.numero_risorse >= ";
    public static final String NEED_WHERE_RISORSE_MIN    = " and n.numero_risorse <= ";
    public static final String NEED_WHERE_RISORSE_EQ     = " and n.numero_risorse = ";
    public static final String NEED_WHERE_PRIORITA       = " and n.priorita = ";
    public static final String NEED_WHERE_PUBBLICAZIONE  = " and n.pubblicazione = ";
    public static final String NEED_WHERE_SCREENING      = " and n.screening = ";
    public static final String NEED_WHERE_STATI          = " and sn.id_stato in ( ";
    public static final String NEED_WHERE_SKILL          = " and ssn.id_skill in ( ";
    public static final String NEED_WHERE_OWNER          = " and non.id_owner in ( ";
    public static final String NEED_WHERE_TIPO           = " and n.tipo in ( ";
    public static final String NEED_WHERE_TIPOLOGIA      = " and tn.id_tipologia in ( ";

}
