/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.exception.ConversioneJsonException;
import it.mds.sdk.gestoreesiti.exception.EsitoNonSalvatoException;
import it.mds.sdk.gestoreesiti.modelli.EsitiValidazione;
import it.mds.sdk.gestoreesiti.modelli.Esito;
import it.mds.sdk.gestoreesiti.modelli.ValidazioneFlusso;
import it.mds.sdk.gestorefile.GestoreFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * La classe permette di salvare l'esito della validazione su un file
 * gestito dalla classe.
 * I file gestiti in questa classe vengono salvati nel percorso indicato file di configurazione
 * config.properties nella variabile esito.percorso.
 * </p>
 */
@Slf4j
public class GestoreEsiti {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final GestoreFile gestoreFile;
    private final Configurazione config;
    private static final String ERRORMESSAGE = "Non è stato possibile salvare il file dell'esito";

    public GestoreEsiti(final GestoreFile gestoreFile) {
        this.config = new Configurazione();
        this.gestoreFile = gestoreFile;
    }

    public GestoreEsiti(final GestoreFile gestoreFile, final Configurazione config) {
        this.gestoreFile = gestoreFile;
        this.config = config;
    }

    /**
     * <p>
     * Crea un nuovo oggetto che correla idRun con gli esiti e
     * scrive il file nella cartella degli esiti nel percorso configurato nella variabile esiti.percorso
     * </p>
     *
     * @param idRun identificativo del run log
     * @param esiti esiti della validazione del flusso identificato da idRun
     * @return un nuovo oggetto con idRun e la lista degli esiti
     */
    public EsitiValidazione creaEsitiValidazione(final String idRun,
                                                 final List<Esito> esiti) {
        log.trace("Chiamata al metodo GestoreEsiti.creaEsitiValidazione.\n\tIdRun: {}\n\tEsiti: {}", idRun, (esiti != null ? Arrays.toString(esiti.toArray()) : "null"));
        try {
            final String percorso = String.format("%s/ESITO_%s.json", config.getEsito().getPercorso(), idRun);
            final Supplier<List<Esito>> esitiNonValidi =
                    () -> esiti != null && esiti.stream().allMatch(Esito::isValoreEsito) ? Collections.emptyList() : esiti;

            final String contenuto = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(esitiNonValidi.get());
            final boolean esito = gestoreFile.scriviFile(percorso, contenuto);
            if (!esito) {
                log.error(ERRORMESSAGE);
                throw new EsitoNonSalvatoException(ERRORMESSAGE);
            }
        } catch (JsonProcessingException e) {
            log.error("Non è possibile effettuare la conversione in JSON dell'oggetto in input");
            throw new ConversioneJsonException("Conversione in JSON non è andata a buon fine", e);
        } catch (IOException e) {
            log.error(ERRORMESSAGE, e);
            throw new EsitoNonSalvatoException(ERRORMESSAGE, e);
        }

        final EsitiValidazione esitiValidazione = new EsitiValidazione(idRun, esiti);
        log.trace("Termine del metodo GestoreEsiti.creaEsitiValidazione.\nOutput:\n\tEsitiValidazione: {}\n", esitiValidazione);
        return esitiValidazione;
    }

    /**
     * Crea un nuovo file di esiti o va in append nel caso in cui il file esista. Il file rispetta la nomenclatura
     * ESITO_IDRUN.json dove IDRUN è quello specificato in input. Il metodo scrive sul file solo gli esiti che non
     * sono stati validati dal sistema, cioè quei record la cui invocazione di isRecordValido è false.
     *
     * @param idRun   dell'esito da scrivere sul file
     * @param records esiti non validati da scrivere sul file
     */
    public void creaValidazioneFlusso(final String idRun,
                                      final List<ValidazioneFlusso> records) {
        try {
            final String percorso = String.format("%s/ESITO_%s.json", config.getEsito().getPercorso(), idRun);
            final Supplier<List<ValidazioneFlusso>> recordNonValidati =
                    () -> records.stream().filter(it -> !isRecordValido(it)).collect(Collectors.toList());
            final String contenuto = OBJECT_MAPPER.writeValueAsString(recordNonValidati.get());
            final boolean esito = gestoreFile.scriviFileAppend(percorso, contenuto + ",");
            if (!esito) {
                log.error(ERRORMESSAGE);
                throw new EsitoNonSalvatoException(ERRORMESSAGE);
            }
        } catch (JsonProcessingException e) {
            log.error("Non è possibile effettuare la conversione in JSON dell'oggetto in input");
            throw new ConversioneJsonException("Conversione in JSON non è andata a buon fine", e);
        } catch (IOException e) {
            log.error(ERRORMESSAGE, e);
            throw new EsitoNonSalvatoException(ERRORMESSAGE, e);
        }

    }

    boolean isRecordValido(final ValidazioneFlusso validazioneFlusso) {
        return validazioneFlusso.getListEsiti()
                .stream()
                .allMatch(Esito::isValoreEsito);
    }

}
