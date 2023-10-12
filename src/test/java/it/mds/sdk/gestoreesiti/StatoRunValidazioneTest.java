package it.mds.sdk.gestoreesiti;

import it.mds.sdk.gestoreesiti.modelli.StatoRun;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static it.mds.sdk.gestoreesiti.modelli.StatoRun.*;
import static org.junit.jupiter.api.Assertions.*;

class StatoRunValidazioneTest {
    final StatoRunValidazione statoRunValidazione = new StatoRunValidazione();

    @Test
    void creata_test() {
        final Predicate<StatoRun> pred = nuovoStato -> statoRunValidazione.test(CREATA, nuovoStato);
        assertAll(() -> {
            assertTrue(pred.test(IN_ELABORAZIONE));
            assertFalse(pred.test(ELABORATA));
            assertFalse(pred.test(KO_GENERICO));
            assertFalse(pred.test(KO_GESTIONE_VALIDAZIONE));
            assertFalse(pred.test(KO_GESTIONE_FILE));
            assertFalse(pred.test(KO_INVIO_MINISTERO));
        });
    }

    @Test
    void inElaborazione_test() {
        final Predicate<StatoRun> pred = nuovoStato -> statoRunValidazione.test(IN_ELABORAZIONE, nuovoStato);
        assertAll(() -> {
            assertFalse(pred.test(CREATA));
            assertTrue(pred.test(ELABORATA));
            assertTrue(pred.test(KO_GENERICO));
            assertTrue(pred.test(KO_GESTIONE_VALIDAZIONE));
            assertTrue(pred.test(KO_GESTIONE_FILE));
            assertTrue(pred.test(KO_INVIO_MINISTERO));
        });
    }

    @Test
    void inErrore_test() {
        final Predicate<StatoRun> pred = nuovoStato -> statoRunValidazione.test(KO_GENERICO, nuovoStato);
        assertAll(() -> {
            assertFalse(pred.test(CREATA));
            assertFalse(pred.test(IN_ELABORAZIONE));
            assertFalse(pred.test(ELABORATA));
            assertFalse(pred.test(KO_GESTIONE_VALIDAZIONE));
            assertFalse(pred.test(KO_GESTIONE_FILE));
            assertFalse(pred.test(KO_INVIO_MINISTERO));
        });
    }

    @Test
    void elaborata_test() {
        final Predicate<StatoRun> pred = nuovoStato -> statoRunValidazione.test(ELABORATA, nuovoStato);
        assertAll(() -> {
            assertFalse(pred.test(CREATA));
            assertFalse(pred.test(IN_ELABORAZIONE));
            assertFalse(pred.test(KO_GENERICO));
            assertFalse(pred.test(KO_GESTIONE_VALIDAZIONE));
            assertFalse(pred.test(KO_GESTIONE_FILE));
            assertFalse(pred.test(KO_INVIO_MINISTERO));
        });
    }

    @Test
    void inviata_ministero_test() {
        final Predicate<StatoRun> pred = nuovoStato -> statoRunValidazione.test(KO_INVIO_MINISTERO, nuovoStato);
        assertAll(() -> {
            assertFalse(pred.test(CREATA));
            assertFalse(pred.test(ELABORATA));
            assertFalse(pred.test(IN_ELABORAZIONE));
            assertFalse(pred.test(KO_GENERICO));
            assertFalse(pred.test(KO_GESTIONE_VALIDAZIONE));
            assertFalse(pred.test(KO_GESTIONE_FILE));
            assertFalse(pred.test(KO_INVIO_MINISTERO));
        });
    }

}
