package it.innotek.wehub.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import it.innotek.wehub.entity.Appuntamento;
import it.innotek.wehub.entity.Owner;
import it.innotek.wehub.repository.AppuntamentoRepository;
import it.innotek.wehub.request.AppuntamentoRequest;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/calendar")
public class GoogleCalendarController {

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    /**
     * Application name.
     */
    private static final String      APPLICATION_NAME  = "Google Calendar API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY      = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String      TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS, CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarController.class);

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials_old.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
        throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarController.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("online")
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("torchyClient");
        //returns an authorized Credential object.
        return credential;
    }

    @GetMapping("/get")
    public List<Event> getCalendar() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
            new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                               .setMaxResults(10)
                               .setTimeMin(now)
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }

        return items;
    }

    @PostMapping("/insert")
    public String insertCalendar(@Valid @RequestBody AppuntamentoRequest appuntamentoRequest){
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            Calendar service =
                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Event event = new Event()
                .setSummary(appuntamentoRequest.getOggetto())
                .setLocation(appuntamentoRequest.getLuogo())
                .setDescription(appuntamentoRequest.getNote());

            DateTime startDateTime = DateTime.parseRfc3339(appuntamentoRequest.getInizio());
            EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("Italy/Rome");
            event.setStart(start);

            DateTime endDateTime = DateTime.parseRfc3339(appuntamentoRequest.getFine());
            EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("Italy/Rome");
            event.setEnd(end);

            ConferenceData conferenceData = new ConferenceData();

            conferenceData.setCreateRequest(
                new CreateConferenceRequest()
                    .setConferenceSolutionKey(
                        new ConferenceSolutionKey()
                            .setType("hangoutsMeet")));

            event.setConferenceData(conferenceData);

            String[] recurrence = new String[] { "RRULE:FREQ=DAILY;COUNT=2" };
            event.setRecurrence(Arrays.asList(recurrence));

            String[] destinatari = appuntamentoRequest.getDestinatari().split(";");
            EventAttendee[] attendees = new EventAttendee[destinatari.length];

            for (int i = 0; i< destinatari.length; i++) {
                attendees[i] = new EventAttendee().setEmail(destinatari[i]);
            }

            event.setAttendees(Arrays.asList(attendees));

            EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
            };

            Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));

            event.setReminders(reminders);

            String calendarId = "primary";
            service.events().insert(calendarId, event).execute();

            Appuntamento appuntamento = getAppuntamento(appuntamentoRequest);

            appuntamentoRepository.save(appuntamento);

        } catch (Exception e){
            logger.error(e.toString());
            return "ERRORE";
        }
        return "OK";
    }

    @NotNull
    private static Appuntamento getAppuntamento(AppuntamentoRequest appuntamentoRequest) {

        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setOggetto(appuntamentoRequest.getOggetto());
        appuntamento.setData(OffsetDateTime.parse(appuntamentoRequest.getInizio()));

        List<Owner> owners = new ArrayList<>();

        for (String ownerId : appuntamentoRequest.getOwnerIds().split(";")) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(ownerId));

            owners.add(owner);
        }

        appuntamento.setOwners(owners);
        return appuntamento;
    }
}