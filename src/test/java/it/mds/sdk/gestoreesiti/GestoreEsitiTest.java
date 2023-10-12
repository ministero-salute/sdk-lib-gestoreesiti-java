package it.mds.sdk.gestoreesiti;

import com.opencsv.bean.CsvBindByPosition;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.exception.EsitoNonSalvatoException;
import it.mds.sdk.gestoreesiti.modelli.ErroreValidazione;
import it.mds.sdk.gestoreesiti.modelli.EsitiValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.gestoreesiti.modelli.ValidazioneFlusso;
import it.mds.sdk.gestorefile.GestoreFile;
import it.mds.sdk.gestorefile.GestoreFileCSVImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestoreEsitiTest {

    @Mock
    private GestoreFile gestoreFile;
    @Mock
    private Configurazione conf;

    @InjectMocks
    private GestoreEsiti gestoreEsiti;

    @BeforeEach
    void initGestoreDefConf() {
        gestoreEsiti = new GestoreEsiti(gestoreFile);
    }

    @Test
    void creaEsitiValidazione_ok() throws IOException {
        when(gestoreFile.scriviFile(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        final Esito e1 = Esito.builder().withCampo("A").withValoreEsito(true).build();
        final Esito e2 = Esito.builder().withCampo("B").withValoreEsito(true).build();
        final Esito e3 = Esito.builder().withCampo("C").withValoreEsito(true).build();
        final Esito e4 = Esito.builder().withCampo("D").withValoreEsito(true).build();
        final List<Esito> esiti = List.of(e1, e2, e3, e4);
        final EsitiValidazione attuale = gestoreEsiti.creaEsitiValidazione("0", esiti);

        assertAll(() -> {
            assertEquals("0", attuale.getIdRun());
            assertThat(attuale.getEsiti(), hasItems(e1, e2, e3, e4));
        });
    }

    @Test
    void creaEsitiValidazione_non_salvato() throws IOException {
        when(gestoreFile.scriviFile(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);

        final Esito e1 = Esito.builder().withCampo("A").withValoreEsito(true).build();
        final Esito e2 = Esito.builder().withCampo("B").withValoreEsito(true).build();
        final Esito e3 = Esito.builder().withCampo("C").withValoreEsito(true).build();
        final Esito e4 = Esito.builder().withCampo("D").withValoreEsito(true).build();
        final List<Esito> esiti = List.of(e1, e2, e3, e4);

        final EsitoNonSalvatoException cause = assertThrows(EsitoNonSalvatoException.class, () -> gestoreEsiti.creaEsitiValidazione("0", esiti));
        assertEquals("Non è stato possibile salvare il file dell'esito", cause.getMessage());
    }

    @Test
    void creaEsitiValidazione_non_salvato_IOException() throws IOException {
        when(gestoreFile.scriviFile(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(IOException.class);

        final Esito e1 = Esito.builder().withCampo("A").withValoreEsito(true).build();
        final Esito e2 = Esito.builder().withCampo("B").withValoreEsito(true).build();
        final Esito e3 = Esito.builder().withCampo("C").withValoreEsito(true).build();
        final Esito e4 = Esito.builder().withCampo("D").withValoreEsito(true).build();
        final List<Esito> esiti = List.of(e1, e2, e3, e4);

        final EsitoNonSalvatoException cause = assertThrows(EsitoNonSalvatoException.class, () -> gestoreEsiti.creaEsitiValidazione("0", esiti));
        assertAll(() -> {
            assertThat(cause.getCause(), instanceOf(IOException.class));
            assertEquals("Non è stato possibile salvare il file dell'esito", cause.getMessage());
        });
    }

    //@Disabled
//    @Test
    void creaValidazioneFlussoOK() {
        List<ValidazioneFlusso> validazioneFlussoList = new ArrayList<>();
        ValidazioneFlusso vl = new ValidazioneFlusso();
        RecordDtoAvnNonSomResidenti record = new RecordDtoAvnNonSomResidenti("23", "mod 1", "P", "035", 6);
        vl.setRecordProcessato(record);
        List<Esito> esitoList = new ArrayList<>();
        List<ErroreValidazione> errore = new ArrayList<>();
        ErroreValidazione ev = new ErroreValidazione("codi", "desc");
        errore.add(ev);
        Esito e1 = new Esito("campo", "scarto", false, errore);
        esitoList.add(e1);
        vl.setNumeroRecord("3");
        vl.setListEsiti(esitoList);
        validazioneFlussoList.add(vl);
        GestoreFile gestoreFile1 = new GestoreFileCSVImpl();
        Configurazione conf = mock(Configurazione.class);
        GestoreEsiti gestoreEsitiMock = new GestoreEsiti(gestoreFile1, conf);
        when(conf.getEsito()).thenReturn(Configurazione.Esito.builder().withPercorso("./").build());
      //  gestoreEsitiMock.creaValidazioneFlusso("TEST", validazioneFlussoList);
    }
    @Test
    void esitoNonSalvatoException() {
        EsitoNonSalvatoException esitoNonSalvatoException = new EsitoNonSalvatoException();
        assertTrue(esitoNonSalvatoException instanceof EsitoNonSalvatoException);
    }
    @Getter
    @AllArgsConstructor
    public class RecordDtoAvnNonSomResidenti {

        @CsvBindByPosition(position = 0)
        private String codIdAssistito;
        @CsvBindByPosition(position = 1)
        private String modalita;
        @CsvBindByPosition(position = 2)
        private String tipoTrasmissione;
        @CsvBindByPosition(position = 3)
        private String codRegione;
        @CsvBindByPosition(position = 4)
        private Integer validitaCI;

    }

}
