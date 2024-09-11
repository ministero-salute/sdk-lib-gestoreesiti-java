/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.exception.InfoRunCambioStatoNegatoException;
import it.mds.sdk.gestoreesiti.exception.InfoRunNonSalvataException;
import it.mds.sdk.gestoreesiti.exception.InfoRunNonTrovataException;
import it.mds.sdk.gestoreesiti.modelli.InfoRun;
import it.mds.sdk.gestoreesiti.modelli.ModalitaOperativa;
import it.mds.sdk.gestoreesiti.modelli.StatoRun;
import it.mds.sdk.gestorefile.GestoreFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.owasp.esapi.ESAPI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * <p>
 * La classe offre i metodi per la gestione dei run log.
 * I file gestiti in questa classe vengono salvati nel percorso indicato file di configurazione
 * config.properties nella variabile run.percorso.
 * </p>
 */
@Slf4j
public class GestoreRunLog {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final GestoreFile gestoreFile;
    private final Progressivo progressivo;
    private final Configurazione config;

    public GestoreRunLog(final GestoreFile gestoreFile,
                         final Progressivo progressivo) {
        this.config = new Configurazione();
        this.gestoreFile = gestoreFile;
        this.progressivo = progressivo;
    }

    /**
     * <p>Crea un nuovo run per un certo client.</p>
     *
     * @param idClient identificativo del client
     * @return le informazioni riguardanti alla run creata
     */
    public InfoRun creaRunLog(final String idClient, ModalitaOperativa modalitaOperativa, int numRecords, String nomeFlusso) {
        log.trace("Chiamata del metodo GestoreRunLog.creaRunLog.\nInput:\n\tidClient: {}\n", idClient);
        String idRun = progressivo.generaProgressivo();

        if (idRun != null && !idRun.matches("^[A-Za-z0-9]*$")) {
            // Se si finisce in questo if, è presente una manipolazione della stringa
            // probabile inject di codice malevolo (fix SAST_086)
            log.trace("ATTENZIONE: idRun contiene caratteri non validi.");
            idRun = idRun.replaceAll("[^A-Za-z0-9 $]", "");
        }

        if (nomeFlusso != null && !nomeFlusso.matches("^[A-Za-z0-9]*$")) {
            // Se si finisce in questo if, è presente una manipolazione della stringa
            // probabile inject di codice malevolo (fix SAST_086)
            log.trace("ATTENZIONE: nomeFlusso contiene caratteri non validi.");
            nomeFlusso = nomeFlusso.replaceAll("[^A-Za-z0-9 $]", "");
        }
        // controlla se esiste inforun con quel numero

        final String percorsoInfoRun = this.getPercorsoRunLog(idRun);
        if (isFileExists(Path.of(percorsoInfoRun))) {
            String percorsoTemp = this.getPercorsoTempRunLog(idRun);
            //private boolean cleanEsitiSovrapposti(String nomeFile, String idRun) {
            this.cleanInfoRunSovrapposte(percorsoInfoRun, percorsoTemp);
        }

        final InfoRun infoRun = InfoRun.builder()
                .withIdRun(idRun)
                .withIdClient(idClient)
                .withStatoEsecuzione(StatoRun.CREATA)
                .withDataInizioEsecuzione(new Timestamp(System.currentTimeMillis()))
                .withNumeroRecord(numRecords)
                .withModOperativa(modalitaOperativa)
                .withNomeFlusso(nomeFlusso)
                .build();
        final boolean esito = salvaRunLog(infoRun);
        if (!esito) {
            log.error("La run {} non è stato possibile salvarla", encode(idRun));
            throw new InfoRunNonSalvataException("La run " + idRun + " non è stato possibile salvarla");
        }
        log.trace("Termine del metodo GestoreRunLog.creaRunLog.");
        return infoRun;
    }

    public boolean isFileExists(Path toPath) {
        return toPath.toFile().exists();
    }

    /**
     * <p>Modifica lo stato di un run</p>
     *
     * @param idRun    identificativo di una run
     * @param statoRun nuovo stato da associare alla run
     * @return le informazioni aggiornate della run
     */
    public InfoRun cambiaStatoRun(final String idRun,
                                  final StatoRun statoRun) {
        log.trace("Chiamata del metodo GestoreRunLog.cambiaStatoRun.\nInput:\n\tidRun: {}\n\tstatoRun: {}\n", idRun, statoRun);
        final InfoRun infoRunVecchio = getRun(idRun);
        if (infoRunVecchio == null) {
            log.error("La run {} non è stata trovata", idRun);
            throw new InfoRunNonTrovataException("La run " + idRun + " non è stata trovata");
        }
        // Controlla la coerenza dello stato in input con lo stato corrente della run log
        final StatoRunValidazione statoRunValidazione = new StatoRunValidazione();
        if (!statoRunValidazione.test(infoRunVecchio.getStatoEsecuzione(), statoRun)) {
            log.error("La run {} non può modificare il suo stato da {} a {}", idRun, infoRunVecchio.getStatoEsecuzione(), statoRun);
            throw new InfoRunCambioStatoNegatoException("La run " + idRun + " non può modificare il suo stato da " + infoRunVecchio.getStatoEsecuzione() + " a " + statoRun);
        }
        final InfoRun infoRunNuovo = InfoRun.builder()
                .withIdRun(infoRunVecchio.getIdRun())
                .withIdClient(infoRunVecchio.getIdClient())
                .withModOperativa(infoRunVecchio.getModOperativa())
                .withDataInizioEsecuzione(infoRunVecchio.getDataInizioEsecuzione())
                .withDataFineEsecuzione(infoRunVecchio.getDataFineEsecuzione())
                .withStatoEsecuzione(statoRun)
                .withNomeFlusso(infoRunVecchio.getNomeFlusso())
                .withTipoElaborazione(infoRunVecchio.getTipoElaborazione())
                .withNumeroRecord(infoRunVecchio.getNumeroRecord())
                .withNumeroRecordAccettati(infoRunVecchio.getNumeroRecordAccettati())
                .withNumeroRecordScartati(infoRunVecchio.getNumeroRecordScartati())
                .withFileAssociatiRun(infoRunVecchio.getFileAssociatiRun())
                .withApi(infoRunVecchio.getApi())
                .withDataRicevutaMds(infoRunVecchio.getDataRicevutaMds())
                .withIdentificativoSoggettoAlimentante(infoRunVecchio.getIdentificativoSoggettoAlimentante())
                .withIdUploads(infoRunVecchio.getIdUploads())
                .withNumeroAtto(infoRunVecchio.getNumeroAtto())
                .withTimestampCreazione(infoRunVecchio.getTimestampCreazione())
                .withTipoAtto(infoRunVecchio.getTipoAtto())
                .withTipoEsitoMds(infoRunVecchio.getTipoEsitoMds())
                .withVersion(infoRunVecchio.getVersion())
                .withCodiceRegione(infoRunVecchio.getCodiceRegione())
                .withAnnoRiferimento(infoRunVecchio.getAnnoRiferimento())
                .withPeriodoRiferimento(infoRunVecchio.getPeriodoRiferimento())
                .withNomeFileOutputMds(infoRunVecchio.getNomeFileOutputMds())
                .withEsitoAcquisizioneFlusso(infoRunVecchio.getEsitoAcquisizioneFlusso())
                .withCodiceErroreInvioFlusso(infoRunVecchio.getCodiceErroreInvioFlusso())
                .withTestoErroreInvioFlusso(infoRunVecchio.getTestoErroreInvioFlusso())
                .build();
        final boolean esito = salvaRunLog(infoRunNuovo);
        if (!esito) {
            log.error("La run {} non è stato possibile salvarla", encode(idRun));
            throw new InfoRunNonSalvataException("La run " + idRun + " non è stato possibile salvarla");
        }
        log.trace("Termine del metodo GestoreRunLog.creaRunLog.");
        return infoRunNuovo;
    }

    public InfoRun updateRun(InfoRun infoRun) {
        final InfoRun infoRunNuovo = InfoRun.builder()
                .withIdRun(infoRun.getIdRun())
                .withIdClient(infoRun.getIdClient())
                .withModOperativa(infoRun.getModOperativa())
                .withDataInizioEsecuzione(infoRun.getDataInizioEsecuzione())
                .withDataFineEsecuzione(infoRun.getDataFineEsecuzione())
                .withStatoEsecuzione(infoRun.getStatoEsecuzione())
                .withNomeFlusso(infoRun.getNomeFlusso())
                .withTipoElaborazione(infoRun.getTipoElaborazione())
                .withNumeroRecord(infoRun.getNumeroRecord())
                .withNumeroRecordAccettati(infoRun.getNumeroRecordAccettati())
                .withNumeroRecordScartati(infoRun.getNumeroRecordScartati())
                .withFileAssociatiRun(infoRun.getFileAssociatiRun())
                .withApi(infoRun.getApi())
                .withDataRicevutaMds(infoRun.getDataRicevutaMds())
                .withIdentificativoSoggettoAlimentante(infoRun.getIdentificativoSoggettoAlimentante())
                .withIdUploads(infoRun.getIdUploads())
                .withNumeroAtto(infoRun.getNumeroAtto())
                .withTimestampCreazione(infoRun.getTimestampCreazione())
                .withTipoAtto(infoRun.getTipoAtto())
                .withTipoEsitoMds(infoRun.getTipoEsitoMds())
                .withVersion(infoRun.getVersion())
                .withCodiceRegione(infoRun.getCodiceRegione())
                .withAnnoRiferimento(infoRun.getAnnoRiferimento())
                .withPeriodoRiferimento(infoRun.getPeriodoRiferimento())
                .withNomeFileOutputMds(infoRun.getNomeFileOutputMds())
                .withEsitoAcquisizioneFlusso(infoRun.getEsitoAcquisizioneFlusso())
                .withCodiceErroreInvioFlusso(infoRun.getCodiceErroreInvioFlusso())
                .withTestoErroreInvioFlusso(infoRun.getTestoErroreInvioFlusso())
                .withDescrizioneStatoEsecuzione(infoRun.getDescrizioneStatoEsecuzione())
                .build();
        final boolean esito = salvaRunLog(infoRunNuovo);
        if (!esito) {
            log.error("La run {} non è stato possibile salvarla", encode(infoRun.getIdRun()));
            throw new InfoRunNonSalvataException("La run " + infoRun.getIdRun() + " non è stato possibile salvarla");
        }
        log.trace("Termine del metodo GestoreRunLog.updateRun.");
        return infoRunNuovo;
    }

    /**
     * <p>Ritorna le informazioni della run dato il suo identificativo.</p>
     *
     * @param idRun identificativo della run log
     * @return informazioni della run richiesta
     */
    public InfoRun getRun(final String idRun) {
        try {
            final String percorsoRunLog = getPercorsoRunLog(idRun);
            final Path path = gestoreFile.leggiFile(percorsoRunLog);
            return parseFileToObject(path);
        } catch (IOException e) {
            log.error("Il file non è stato possibile leggerlo", e);
        }
        return null;
    }

    /**
     * <p>Ritorna le informazioni della run dato l'identificativo del client</p>
     *
     * @param idClient identificativo del client
     * @return informazioni della run richiesta
     */
    public InfoRun getRunFromClient(final String idClient) {
        try {
            final File cartellaRun = gestoreFile.leggiFile(FilenameUtils.normalize(config.getRun().getPercorso())).toFile();
            final File[] files = cartellaRun.listFiles(it -> isRunLogConIdClient(it.toPath(), idClient));
            if (files != null && files.length > 0) {
                final File fileTrovato = files[0];
                return parseFileToObject(fileTrovato.toPath());
            }
        } catch (IOException e) {
            log.error("Il file non è stato possibile leggerlo", e);
        }
        return null;
    }

    public boolean salvaRunLog(final InfoRun infoRun) {
        try {
            final String json = OBJECT_MAPPER.writeValueAsString(infoRun);
            return gestoreFile.scriviFile(getPercorsoRunLog(infoRun.getIdRun()), json);
        } catch (IOException e) {
            log.error("Il file non è stato possibile scriverlo", e);
            return false;
        }
    }

    public String getPercorsoRunLog(final String idRun) {
        log.debug("{}.getPercorsoRunLog - FILE ID RUN", this.getClass().getName());
        return String.format("%s/%s.json", config.getRun().getPercorso(), idRun);
    }

    private String getPercorsoTempRunLog(final String idRun) {
        log.debug("{}.getPercorsoRunLog - FILE ID RUN TEMPORANEO", this.getClass().getName());
        String ldt = this.getLocalDateTimeStringWithMillis();
        return String.format("%s/%s_%s.json", config.getRun().getPercorso(), idRun, ldt);
    }

    private String getLocalDateTimeStringWithMillis() {
        String localDateTime = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        // Rimuoviamo i caratteri speciali, poichè la stringa costituirà il nome della tabella.
        localDateTime = localDateTime
                .replace(":", "")
                .replace("-", "")
                .replace(".", "");

        return localDateTime;
    }

    private InfoRun parseFileToObject(final Path path) {
        try {
            final String json = Files.readString(path, StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(json, InfoRun.class);
        } catch (IOException e) {
            log.error("Il file non è stato possibile leggerlo", e);
            return null;
        }
    }

    private boolean isRunLogConIdClient(final Path path,
                                        final String idClient) {
        final InfoRun infoRun = parseFileToObject(path);
        return idClient != null &&
                infoRun != null &&
                Objects.equals(idClient, infoRun.getIdClient());
    }

    private String encode(String message) {
        if (message != null) {
            message = message.replace('\n', '_').replace('\r', '_')
                    .replace('\t', '_');
        }
        message = ESAPI.encoder().encodeForHTML(message);
        System.out.println(message);
        return message;
    }

    public boolean cleanInfoRunSovrapposte(String nomeFile, String percorsoTemp) {

        log.info("*** Inizio Pulizia File Esiti Esistente ***");
        log.debug("{}.formatJsonEsiti - nomeFile{} ", this.getClass().getName(), nomeFile);

        String linea;

//        final String percorsoTemp = String.format(
//                "%s/ESITO_%s_%s.json", config.getEsito().getPercorso(), idRun, localDateTime
//        );

        try (FileReader fr = new FileReader(nomeFile);
             BufferedReader br = new BufferedReader(fr);
             FileWriter fw = new FileWriter(percorsoTemp);
             BufferedWriter bw = new BufferedWriter(fw)
        ) {

            while ((linea = br.readLine()) != null) {
                bw.write(linea);
                bw.newLine();
                bw.flush();
            }

            br.close();
            fr.close();
            bw.close();
            fr.close();

            File fileFinale = Path.of(nomeFile).toFile();

            if (!fileFinale.exists()) {
                log.warn("ATTENZIONE: Il file {} non esiste", Path.of(nomeFile));
                throw new java.io.IOException();
            }

            boolean eliminazioneFile = fileFinale.delete();

            if (!eliminazioneFile) {
                log.warn("Errore durante l'eliminazione del file {}", Path.of(nomeFile));
            }
            log.info("*** Fine Pulizia File Esiti Esistente - ESITO {} ***", (eliminazioneFile ? "OK" : "KO"));

            return eliminazioneFile;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
