/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti;

import it.mds.sdk.gestoreesiti.exception.InfoRunCambioStatoNegatoException;
import it.mds.sdk.gestoreesiti.exception.InfoRunNonSalvataException;
import it.mds.sdk.gestoreesiti.exception.InfoRunNonTrovataException;
import it.mds.sdk.gestoreesiti.modelli.InfoRun;
import it.mds.sdk.gestoreesiti.modelli.ModalitaOperativa;
import it.mds.sdk.gestoreesiti.modelli.StatoRun;
import it.mds.sdk.gestorefile.GestoreFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GestoreRunLogTest {

    @Mock
    private GestoreFile gestoreFile;

    @Mock
    private Progressivo progressivo;

    private GestoreRunLog gestoreRunLog;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        gestoreRunLog = spy(new GestoreRunLog(gestoreFile, progressivo));
    }

    @Test
    void cleanedFile_ok() throws IOException {

        File file = Mockito.mock(File.class);

        //Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
        //        .thenReturn(true);
        Mockito.doReturn(true).when(gestoreFile).scriviFile(anyString(), anyString());
        Mockito.doReturn("").when(gestoreRunLog).getPercorsoRunLog(anyString());
        Mockito.doReturn(true).when(gestoreRunLog).cleanInfoRunSovrapposte(anyString(), anyString());
        Mockito.doReturn(true).when(gestoreRunLog).isFileExists(any());
        Mockito.when(progressivo.generaProgressivo())
                .thenReturn("0");

        //Mockito.when(gestoreRunLog.salvaRunLog(any())).thenReturn(true);
        InfoRun infoRun = new InfoRun(
                "0", "0", null, null, null,
                null, null, StatoRun.CREATA, null, null, null,
                1, 1, 1, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null
        );
        Mockito.doReturn(true).when(gestoreRunLog).salvaRunLog(infoRun);
        gestoreRunLog.creaRunLog("0", ModalitaOperativa.T, 0, "FLUSSO_TEST");
        assertEquals("0", infoRun.getIdRun());
        assertEquals("0", infoRun.getIdClient());
        assertEquals(StatoRun.CREATA, infoRun.getStatoEsecuzione());
    }

    @Test
    void creaRunLog_ok() throws IOException {

        File file = Mockito.mock(File.class);

        //Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
        //        .thenReturn(true);
        Mockito.doReturn(true).when(gestoreFile).scriviFile(anyString(), anyString());
        Mockito.doReturn("").when(gestoreRunLog).getPercorsoRunLog(anyString());
        Mockito.doReturn(false).when(gestoreRunLog).isFileExists(any());
        Mockito.when(progressivo.generaProgressivo())
                .thenReturn("0");

        //Mockito.when(gestoreRunLog.salvaRunLog(any())).thenReturn(true);
        InfoRun infoRun = new InfoRun(
                "0", "0", null, null, null,
                null, null, StatoRun.CREATA, null, null, null,
                1, 1, 1, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null
        );
        Mockito.doReturn(true).when(gestoreRunLog).salvaRunLog(infoRun);
        gestoreRunLog.creaRunLog("0", ModalitaOperativa.T, 0, "FLUSSO_TEST");
        assertEquals("0", infoRun.getIdRun());
        assertEquals("0", infoRun.getIdClient());
        assertEquals(StatoRun.CREATA, infoRun.getStatoEsecuzione());
    }

    @Test
    void creaRunLog_idRun_non_salvato() throws IOException {
        Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                .thenThrow(IOException.class);
        Mockito.when(progressivo.generaProgressivo())
                .thenReturn("0");

        assertThrows(InfoRunNonSalvataException.class, () ->
                gestoreRunLog.creaRunLog("0", ModalitaOperativa.T, 0, "FLUSSO_TEST"));
    }

    @Test
    void getRun_ok() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"CREATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            final InfoRun run = gestoreRunLog.getRun("0");
            assertAll(() -> {
                assertEquals("0", run.getIdRun());
                assertEquals("0", run.getIdClient());
                assertNull(run.getTipoElaborazione());
                assertNull(run.getModOperativa());
                assertNull(run.getDataInizioEsecuzione());
                assertNull(run.getDataFineEsecuzione());
                assertEquals(StatoRun.CREATA, run.getStatoEsecuzione());
                assertNull(run.getFileAssociatiRun());
                assertNull(run.getNomeFlusso());
                assertEquals(0, run.getNumeroRecord());
                assertEquals(0, run.getNumeroRecordAccettati());
                assertEquals(0, run.getNumeroRecordScartati());
            });
        }
    }

    @Test
    void cambiaStatoRun_ok() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"CREATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            final InfoRun run = gestoreRunLog.cambiaStatoRun("0", StatoRun.IN_ELABORAZIONE);
            assertAll(() -> {
                assertEquals("0", run.getIdRun());
                assertEquals("0", run.getIdClient());
                assertNull(run.getTipoElaborazione());
                assertNull(run.getModOperativa());
                assertNull(run.getDataInizioEsecuzione());
                assertNull(run.getDataFineEsecuzione());
                assertEquals(StatoRun.IN_ELABORAZIONE, run.getStatoEsecuzione());
                assertNull(run.getFileAssociatiRun());
                assertNull(run.getNomeFlusso());
                assertEquals(0, run.getNumeroRecord());
                assertEquals(0, run.getNumeroRecordAccettati());
                assertEquals(0, run.getNumeroRecordScartati());
            });
        }
    }

    @Test
    void cambiaStatoRun_idRun_non_salvata() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                    .thenThrow(IOException.class);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"CREATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            assertThrows(InfoRunNonSalvataException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.IN_ELABORAZIONE));
        }
    }

    @Test
    void cambiaStatoRun_idRun_non_trovata() throws IOException {
        Mockito.when(gestoreFile.leggiFile(anyString()))
                .thenThrow(IOException.class);
        assertThrows(InfoRunNonTrovataException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.IN_ELABORAZIONE));
    }

    @Test
    void cambiaStatoRun_statoRun_creata() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"CREATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            Assertions.assertDoesNotThrow(() -> gestoreRunLog.cambiaStatoRun("0", StatoRun.IN_ELABORAZIONE));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.ELABORATA));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.KO_GENERICO));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.KO_INVIO_MINISTERO));
        }
    }

    @Test
    void cambiaStatoRun_statoRun_InElaborazione() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.when(gestoreFile.scriviFile(anyString(), anyString()))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"IN_ELABORAZIONE\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            Assertions.assertDoesNotThrow(() -> gestoreRunLog.cambiaStatoRun("0", StatoRun.ELABORATA));
            Assertions.assertDoesNotThrow(() -> gestoreRunLog.cambiaStatoRun("0", StatoRun.KO_GENERICO));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.CREATA));
        }
    }


    @Test
    void cambiaStatoRun_statoRun_Elaborata() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(Paths.get("test"));
            Mockito.lenient()
                    .when(gestoreFile.scriviFile(anyString(), anyString()))
                    .thenReturn(true);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"ELABORATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.ELABORATA));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.IN_ELABORAZIONE));
            assertThrows(InfoRunCambioStatoNegatoException.class, () -> gestoreRunLog.cambiaStatoRun("0", StatoRun.CREATA));
        }
    }


    @Test
    void getRunFromClient_ok() throws IOException {
        try (final MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            final Path pathMock = Mockito.mock(Path.class);
            final File fileMock = Mockito.mock(File.class);
            Mockito.when(gestoreFile.leggiFile(anyString()))
                    .thenReturn(pathMock);
            Mockito.when(pathMock.toFile())
                    .thenReturn(fileMock);
            Mockito.when(fileMock.listFiles(any(FileFilter.class)))
                    .thenReturn(new File[]{fileMock});
            Mockito.when(fileMock.toPath())
                    .thenReturn(pathMock);
            filesMockedStatic.when(() -> Files.readString(any(Path.class), any(Charset.class)))
                    .thenReturn("{\"idRun\":\"0\",\"idClient\":\"0\",\"tipoElaborazione\":null,\"modOperativa\":null,\"dataInizioEsecuzione\":null,\"dataFineEsecuzione\":null,\"statoEsecuzione\":\"CREATA\",\"fineAssociatiRun\":null,\"nomeFlusso\":null,\"numeroRecord\":0,\"numeroRecordAccettati\":0,\"numeroRecordScartati\":0}\n");

            final InfoRun run = gestoreRunLog.getRunFromClient("0");
            assertAll(() -> {
                assertEquals("0", run.getIdRun());
                assertEquals("0", run.getIdClient());
                assertNull(run.getTipoElaborazione());
                assertNull(run.getModOperativa());
                assertNull(run.getDataInizioEsecuzione());
                assertNull(run.getDataFineEsecuzione());
                assertEquals(StatoRun.CREATA, run.getStatoEsecuzione());
                assertNull(run.getFileAssociatiRun());
                assertNull(run.getNomeFlusso());
                assertEquals(0, run.getNumeroRecord());
                assertEquals(0, run.getNumeroRecordAccettati());
                assertEquals(0, run.getNumeroRecordScartati());
            });
        }
    }
    @Test
    void updateInfoRun_OK() {
        InfoRun infoRun = new InfoRun(
                "0", "0", null, null, null,
                null, null, StatoRun.CREATA, null, null, null,
                1, 1, 1, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null
        );
        Mockito.doReturn(true).when(gestoreRunLog).salvaRunLog(infoRun);
        gestoreRunLog.updateRun(infoRun);
    }
    @Test
    void updateInfoRun_KO() {
        InfoRun infoRun = new InfoRun(
                "0", "0", null, null, null,
                null, null, StatoRun.CREATA, null, null, null,
                1, 1, 1, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null
        );
        Mockito.doReturn(false).when(gestoreRunLog).salvaRunLog(infoRun);
        assertThrows(InfoRunNonSalvataException.class, () -> gestoreRunLog.updateRun(infoRun));
    }
}
