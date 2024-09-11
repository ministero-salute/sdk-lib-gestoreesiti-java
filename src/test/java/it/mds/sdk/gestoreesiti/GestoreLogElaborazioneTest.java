/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti;

import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestoreesiti.exception.FusFilePresenteException;
import it.mds.sdk.gestoreesiti.exception.FusNonSalvataException;
import it.mds.sdk.gestoreesiti.modelli.EsitoElaborazione;
import it.mds.sdk.gestoreesiti.modelli.InfoElaborazione;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class GestoreLogElaborazioneTest {

    @Mock
    private GestoreFile gestoreFile;

    @InjectMocks
    private GestoreLogElaborazione gestoreLogElaborazione;

    @Test
    void creaFileLog_ok() throws IOException {
        final String idUpload = "0";
        Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                .thenReturn(true);
        final InfoElaborazione result = InfoElaborazione.builder()
                .withIdUpload(idUpload)
                .withNomeFlusso("testflusso")
                .withIdClient("testIdClient")
                .withTimestampElaborazione(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ))
                .build();
        InfoElaborazione infoElaborazione = gestoreLogElaborazione.creaFileLog(result);

        assertEquals(result, infoElaborazione);
    }

    @Test
    void creaFileLog_file_non_salvato() throws IOException {
        Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                .thenReturn(false);
        final String idUpload = "0";
        final InfoElaborazione result = InfoElaborazione.builder()
                .withIdUpload(idUpload)
                .withNomeFlusso("testflusso")
                .withIdClient("testIdClient")
                .withTimestampElaborazione(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ))
                .build();

        assertThrows(FusNonSalvataException.class, () -> gestoreLogElaborazione.creaFileLog(result));
    }

    @Test
    void creaFileLog_file_non_salvato_IOException() throws IOException {
        Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                .thenThrow(IOException.class);
        final String idUpload = "0";
        final InfoElaborazione result = InfoElaborazione.builder()
                .withIdUpload(idUpload)
                .withNomeFlusso("testflusso")
                .withIdClient("testIdClient")
                .withTimestampElaborazione(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ))
                .build();
        assertThrows(FusNonSalvataException.class, () -> gestoreLogElaborazione.creaFileLog(result));
    }

    @Test
    void aggiornaFileLog_ok() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            final String idUpload = "3";
            final InfoElaborazione result = InfoElaborazione.builder()
                    .withIdUpload(idUpload)
                    .withIdClient("123456")
                    .withNomeFlusso("nome_flusso_test")
                    .withTimestampElaborazione("2022-06-21T12:38:11.858372700Z")
                    .withEsitoElaborazioneMds("KO")
                    .withCodiceErroreElaborazioneMds("Mx22")
                    .withTestoErroreElaborazioneMds("Operazione completata con scarti")
                    .withStatoDownloadFus("DA RICHIEDERE")
                    .build();

            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.when(gestoreFile.scriviFile(anyString(), contains("\"esitoElaborazioneMds\":\"KO\"")))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idUpload\":\"3\",\"idClient\":\"123456\"," +
                            "\"nomeFlusso\":\"nome_flusso_test\",\"timestampElaborazione\":\"2022-06-21T12:38:11" +
                            ".858372700Z\",\"version\":null,\"esitoElaborazioneMds\":\"KO\"," +
                            "\"codiceErroreElaborazioneMds\":\"Mx22\",\"testoErroreElaborazioneMds\":\"Operazione " +
                            "completata con scarti\",\"statoDownloadFus\":\"DA RICHIEDERE\",\"esitoDownloadFus\":null,\"descrizioneErroreDownloadFus\":null,\"codiceErroreDownloadFus\":null}");

            assertEquals(result, gestoreLogElaborazione.aggiornaFileLog(idUpload, Map.ofEntries(Map.entry("esitoElaborazioneMds", "KO"))));
        }
    }

    @Test
    void aggiornaFileLog_non_salvato() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
           // Mockito.when(gestoreFile.scriviFile(anyString(), contains("\"esitoElaborazioneMds\":\"KO\"")))
            //        .thenReturn(false);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"esitoElaborazioneMds\":\"OK\",\"codiceErroreElaborazione\":null,\"descrizioneErroreElaborazione\":null,\"validationPath\":null,\"statoDownloadFus\":null,\"esitoDownloadFus\":null,\"codiceErroreDownload\":null,\"descrizioneErroreDownload\":null}");

            assertThrows(FusNonSalvataException.class, () -> gestoreLogElaborazione.aggiornaFileLog("0", Map.ofEntries(Map.entry("esitoElaborazioneMds", "KO"))));
        }
    }

    @Test
    void aggiornaFileLog_non_salvato_IOException() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
           // Mockito.when(gestoreFile.scriviFile(anyString(), contains("\"esitoElaborazioneMds\":\"KO\"")))
             //       .thenThrow(IOException.class);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"esitoElaborazioneMds\":\"OK\",\"codiceErroreElaborazione\":null,\"descrizioneErroreElaborazione\":null,\"validationPath\":null,\"statoDownloadFus\":null,\"esitoDownloadFus\":null,\"codiceErroreDownload\":null,\"descrizioneErroreDownload\":null}");

            assertThrows(FusNonSalvataException.class, () -> gestoreLogElaborazione.aggiornaFileLog("0", Map.ofEntries(Map.entry("esitoElaborazioneMds", "KO"))));
        }
    }

    @Test
    void checkFileFus_ko() throws IOException {
        Mockito.when(gestoreFile.leggiFile(anyString()))
                .thenReturn(Path.of("test"));

        assertFalse(gestoreLogElaborazione.checkFileFus("0"));
    }

    @Test
    void checkFileFus_file_non_trovato() throws IOException {
        Mockito.when(gestoreFile.leggiFile(anyString()))
                .thenReturn(null);

        assertFalse(gestoreLogElaborazione.checkFileFus("0"));
    }

    @Test
    void checkFileFus_lettura_file_IOException() throws IOException {
        Mockito.when(gestoreFile.leggiFile(anyString()))
                .thenThrow(IOException.class);

        assertFalse(gestoreLogElaborazione.checkFileFus("0"));
    }

}
