package it.mds.sdk.gestoreesiti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mds.sdk.gestoreesiti.conf.Configurazione;
import it.mds.sdk.gestoreesiti.exception.ConversioneJsonException;
import it.mds.sdk.gestoreesiti.exception.FusNonSalvataException;
import it.mds.sdk.gestoreesiti.modelli.InfoElaborazione;
import it.mds.sdk.gestorefile.GestoreFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * <p>
 * Il gestore log elaborazione è il componente che si occupa di creare e modificare
 * i file di log contenenti il dettaglio della singola elaborazione e di aggiornarli
 * con l'esito del download del FUS.
 * I file gestiti in questa classe vengono salvati nel percorso indicato file di configurazione
 * config.properties nella variabile fus.percorso.
 * </p>
 */
@Slf4j
public class GestoreLogElaborazione {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final GestoreFile gestoreFile;
    private final Configurazione config;
    private static final String FORMATPERCORSO = "%s/%s.log";

    public GestoreLogElaborazione(final GestoreFile gestoreFile) {
        this.gestoreFile = gestoreFile;
        this.config = new Configurazione();
    }

    /**
     * <p>
     * Crea un nuovo file di log verificando se non ci sono file già associati all'idUpload,
     * e in caso positivo salva il file con l'esito dell'elaborazione.
     * </p>
     *
     * @param infoElaborazione elaborazione da creare
     * @return le informazioni dell'elaborazione
     */
    public InfoElaborazione creaFileLog(final InfoElaborazione infoElaborazione) {
        log.trace("Chiamata al metodo GestoreLogElaborazione.creaFileLog.\nInput:\n\tidUpload: {}\n\tesitoElaborazione: {}\n", infoElaborazione.getIdUpload(), infoElaborazione.getEsitoElaborazioneMds());

        String contenuto;
        try {
            // Converto esitoElaborazione in un JSON
            contenuto = OBJECT_MAPPER.writeValueAsString(infoElaborazione);
        } catch (JsonProcessingException e) {
            log.error("Non è possibile effettuare la conversione in JSON dell'oggetto in input");
            throw new ConversioneJsonException("Conversione in JSON non è andata a buon fine", e);
        }

        if (contenuto != null) {
            try {
                final String percorso = String.format(FORMATPERCORSO, config.getMonitoraggio().getLog(), infoElaborazione.getIdUpload());
                final boolean esito = gestoreFile.scriviFile(percorso, contenuto);
                if (!esito) {
                    log.error("Non è stato possibile scrivere nel file {}", infoElaborazione.getIdUpload());
                    throw new FusNonSalvataException("Non è stato possibile salvare il file");
                }
            } catch (IOException e) {
                log.error("Non è stato possibile scrivere nel file {}", infoElaborazione.getIdUpload());
                throw new FusNonSalvataException("Non è stato possibile salvare il file con idUpload " + infoElaborazione.getIdUpload(), e);
            }
        }

        log.trace("Termine del metodo GestoreLogElaborazione.creaFileLog.\nOutput:\n\tInfoElaborazione: {}\n", infoElaborazione);

        return infoElaborazione;
    }

    /**
     * <p>
     * Aggiorna i valori dell'esito dell'elaborazione salvati nel file di log.
     * </p>
     *
     * @param idUpload identificativo dell'upload
     * @param valori   nuovi valori da salvare nel file di log
     * @return le informazioni dell'elaborazione
     */
    public InfoElaborazione aggiornaFileLog(final String idUpload,
                                            final Map<String, Object> valori) {
        log.trace("Chiamata al metodo GestoreLogElaborazione.aggiornaFileLog.\nInput:\n\tidUpload: {}\n\tValori: {}\n", idUpload, valori);
        try {
            final String percorso = String.format(FORMATPERCORSO, config.getMonitoraggio().getLog(), idUpload);
            log.info("Percorso del file {}", percorso);
            final String contenuto = Files.readString(gestoreFile.leggiFile(percorso), StandardCharsets.UTF_8);
            final InfoElaborazione infoElaborazione = OBJECT_MAPPER.readValue(contenuto, InfoElaborazione.class);

            String esitoElaborazioneMds = infoElaborazione.getEsitoElaborazioneMds();

            if (esitoElaborazioneMds != null && !esitoElaborazioneMds.matches("^[A-Za-z0-9]*$")) {
                // Se si finisce in questo if, è presente una manipolazione della stringa
                // probabile inject di codice malevolo (fix SAST_086)
                log.trace("ATTENZIONE: esitoElaborazione contiene caratteri non validi.");
                esitoElaborazioneMds = esitoElaborazioneMds.replaceAll("[^A-Za-z0-9 $]", "");
            }

            log.debug("Esito dell'elaborazione {}", esitoElaborazioneMds);
            InfoElaborazione aggiornata = InfoElaborazione.aggiorna(infoElaborazione, valori);
            final boolean esito = gestoreFile.scriviFile(percorso, OBJECT_MAPPER.writeValueAsString(aggiornata));
            if (!esito) {
                log.error("Non è stato possibile scrivere nel file {}", idUpload);
                throw new FusNonSalvataException("Non è stato possibile salvare il file");
            }

            log.trace("Termine del metodo GestoreLogElaborazione.aggiornaFileLog.\nOutput:\n\tInfoElaborazione: {}\n", infoElaborazione);
            return aggiornata;
        } catch (IOException e) {
            throw new FusNonSalvataException("Non è stato possibile salvare il file", e);
        }
    }

    /**
     * <p>
     * Verifica se è presente il file FUS con l'identificativo dell'upload.
     * </p>
     *
     * @param idUpload identificativo dell'upload
     * @return true se il file è presente, altrimenti false.
     */
    public boolean checkFileFus(final String idUpload) {
        log.trace("Chiamata al metodo GestoreLogElaborazione.checkFileFus.\nInput:\n\tidUpload: {}\n", idUpload);
        try {
            final Path path = gestoreFile.leggiFile(String.format("%s/%s.zip", config.getMonitoraggio().getFus(), idUpload));
            log.trace("Termine del metodo GestoreLogElaborazione.checkFileFus.\nOutput:\n\tEsito: {}\n", path != null);
            return path != null ? Files.exists(path) : false;
        } catch (IOException e) {
            log.error("Si è verificato un errore nella richiesta del file {}\n{}", idUpload, e);
            return false;
        }
    }

    public InfoElaborazione recuperaInfoElaborazione(String idUpload) {
        log.trace("Chiamata al metodo GestoreLogElaborazione.recuperaInfoElaborazione.\nInput:\n\tidUpload: {}\n", idUpload);
        final String percorso = String.format(FORMATPERCORSO, config.getMonitoraggio().getLog(), idUpload);
        log.info("Percorso del file {}", percorso);
        final String contenuto;
        try {
            contenuto = Files.readString(gestoreFile.leggiFile(percorso), StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(contenuto, InfoElaborazione.class);
        } catch (IOException e) {
            log.error("Si è verificato un errore nella lettura stato download del file {}\n{}", idUpload, e);
        }
        return InfoElaborazione.builder().build();
    }
}
