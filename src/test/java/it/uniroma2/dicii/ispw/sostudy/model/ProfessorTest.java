package it.uniroma2.dicii.ispw.sostudy.model;

import it.uniroma2.dicii.ispw.sostudy.eng.observer.MessageObserver;
import it.uniroma2.dicii.ispw.sostudy.model.Student;
import it.uniroma2.dicii.ispw.sostudy.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserObserverTest {

    // 1. Il finto observer
    class FintaSchermataNotifiche extends MessageObserver {
        boolean notificaRicevuta = false;

        @Override
        public void update() {
            notificaRicevuta = true;
        }
    }

    @Test
    void testAggiornamentoInterfaccia() {
        // ARRANGE: Preparazione
        User utenteDaTestare = new Student("giuseppe", "bianchi", "giuseppe.bianchi@gmail.com"); // Sostituisci con il tuo costruttore reale
        FintaSchermataNotifiche uiObserver = new FintaSchermataNotifiche();

        utenteDaTestare.attach(uiObserver);

        // ACT: Azione (scatena l'evento che dovrebbe chiamare update())
        // Nel tuo sistema, questo potrebbe essere il metodo che il Controller
        // chiama sull'utente quando viene pubblicato un test.
        utenteDaTestare.notifyObservers();

        // ASSERT: Verifica
        assertTrue(uiObserver.notificaRicevuta, "ERRORE: L'Observer non è stato notificato. Il pattern ha fallito!");
    }
}