package it.mds.sdk.gestoreesiti.modelli;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class EsitoElaborazioneTest {

    @Test
    void aggiorna_validationPath() {
        final EsitoElaborazione input = EsitoElaborazione.builder()
                .withEsitoElaborazioneMds("OK")
                .withValidationPath("MY_PATH")
                .withStatoDownloadFus("OK")
                .withEsitoDownloadFus("OK")
                .build();
        final EsitoElaborazione output = EsitoElaborazione.builder()
                .withEsitoElaborazioneMds("OK")
                .withValidationPath("NEW_PATH")
                .withStatoDownloadFus("OK")
                .withEsitoDownloadFus("OK")
                .build();

        final Map<String, Object> map = Map.ofEntries(
                Map.entry("esitoElaborazioneMds", "OK"),
                Map.entry("validationPath", "NEW_PATH"),
                Map.entry("statoDownloadFus", "OK"),
                Map.entry("esitoDownloadFus", "OK")
        );
        final EsitoElaborazione attuale = EsitoElaborazione.aggiorna(input, map);
        assertAll(() -> {
            assertThat(attuale, not(sameInstance(input)));
            assertThat(attuale, equalTo(output));
        });
    }

    @Test
    void aggiorna_tutti_valori() {
        final EsitoElaborazione input = EsitoElaborazione.builder()
                .withEsitoElaborazioneMds("OK")
                .withValidationPath("MY_PATH")
                .withStatoDownloadFus("OK")
                .withEsitoDownloadFus("OK")
                .build();
        final EsitoElaborazione output = EsitoElaborazione.builder()
                .withEsitoElaborazioneMds("KO")
                .withCodiceErroreElaborazione("0000")
                .withDescrizioneErroreElaborazione("ERRORE ELABORAZIONE")
                .withValidationPath("NEW_PATH")
                .withStatoDownloadFus("KO")
                .withEsitoDownloadFus("KO")
                .withCodiceErroreDownload("0000")
                .withDescrizioneErroreDownload("ERRORE DOWNLOAD")
                .build();

        final Map<String, Object> map = Map.ofEntries(
                Map.entry("esitoElaborazioneMds", "KO"),
                Map.entry("codiceErroreElaborazione", "0000"),
                Map.entry("descrizioneErroreElaborazione", "ERRORE ELABORAZIONE"),
                Map.entry("validationPath", "NEW_PATH"),
                Map.entry("statoDownloadFus", "KO"),
                Map.entry("esitoDownloadFus", "KO"),
                Map.entry("codiceErroreDownload", "0000"),
                Map.entry("descrizioneErroreDownload", "ERRORE DOWNLOAD")
        );
        final EsitoElaborazione attuale = EsitoElaborazione.aggiorna(input, map);
        assertAll(() -> {
            assertThat(attuale, not(sameInstance(input)));
            assertThat(attuale, equalTo(output));
        });
    }

}
