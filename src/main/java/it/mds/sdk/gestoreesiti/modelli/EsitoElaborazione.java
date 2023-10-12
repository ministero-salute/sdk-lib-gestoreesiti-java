package it.mds.sdk.gestoreesiti.modelli;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@Value
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = false)
public class EsitoElaborazione extends AbstractSecurityModel {
    String esitoElaborazioneMds;
    String codiceErroreElaborazione;
    String descrizioneErroreElaborazione;
    String validationPath;
    String statoDownloadFus;
    String esitoDownloadFus;
    String codiceErroreDownload;
    String descrizioneErroreDownload;

    public EsitoElaborazione(@JsonProperty("esitoElaborazioneMds") String esitoElaborazioneMds,
                             @JsonProperty("codiceErroreElaborazione") String codiceErroreElaborazione,
                             @JsonProperty("descrizioneErroreElaborazione") String descrizioneErroreElaborazione,
                             @JsonProperty("validationPath") String validationPath,
                             @JsonProperty("statoDownloadFus") String statoDownloadFus,
                             @JsonProperty("esitoDownloadFus") String esitoDownloadFus,
                             @JsonProperty("codiceErroreDownload") String codiceErroreDownload,
                             @JsonProperty("descrizioneErroreDownload") String descrizioneErroreDownload) {
        this.esitoElaborazioneMds = esitoElaborazioneMds;
        this.codiceErroreElaborazione = codiceErroreElaborazione;
        this.descrizioneErroreElaborazione = descrizioneErroreElaborazione;
        this.validationPath = validationPath;
        this.statoDownloadFus = statoDownloadFus;
        this.esitoDownloadFus = esitoDownloadFus;
        this.codiceErroreDownload = codiceErroreDownload;
        this.descrizioneErroreDownload = descrizioneErroreDownload;
    }

    /**
     * <p>
     * Il metodo ritorna un nuovo oggetto {@link EsitoElaborazione} con i valori aggiornati.
     * </p>
     *
     * @param esitoElaborazione oggetto da aggiornare
     * @param valori            nuovi valori in ordine di dichiarazione delle property della classe
     * @return l'oggetto modificato
     */
    public static EsitoElaborazione aggiorna(EsitoElaborazione esitoElaborazione,
                                             Map<String, Object> valori) {
        final EsitoElaborazioneBuilder builder = EsitoElaborazione.builder()
                .withEsitoElaborazioneMds(esitoElaborazione.esitoElaborazioneMds)
                .withCodiceErroreElaborazione(esitoElaborazione.codiceErroreElaborazione)
                .withDescrizioneErroreElaborazione(esitoElaborazione.descrizioneErroreElaborazione)
                .withValidationPath(esitoElaborazione.validationPath)
                .withStatoDownloadFus(esitoElaborazione.statoDownloadFus)
                .withEsitoDownloadFus(esitoElaborazione.esitoDownloadFus)
                .withCodiceErroreDownload(esitoElaborazione.codiceErroreDownload)
                .withDescrizioneErroreDownload(esitoElaborazione.descrizioneErroreDownload);
        for (final var entry : valori.entrySet()) {
            switch (entry.getKey()) {
                case "esitoElaborazioneMds":
                    builder.esitoElaborazioneMds = (String) valori.get(entry.getKey());
                    break;
                case "codiceErroreElaborazione":
                    builder.codiceErroreElaborazione = (String) valori.get(entry.getKey());
                    break;
                case "descrizioneErroreElaborazione":
                    builder.descrizioneErroreElaborazione = (String) valori.get(entry.getKey());
                    break;
                case "validationPath":
                    builder.validationPath = (String) valori.get(entry.getKey());
                    break;
                case "statoDownloadFus":
                    builder.statoDownloadFus = (String) valori.get(entry.getKey());
                    break;
                case "esitoDownloadFus":
                    builder.esitoDownloadFus = (String) valori.get(entry.getKey());
                    break;
                case "codiceErroreDownload":
                    builder.codiceErroreDownload = (String) valori.get(entry.getKey());
                    break;
                case "descrizioneErroreDownload":
                    builder.descrizioneErroreDownload = (String) valori.get(entry.getKey());
                    break;
                default:
                    // Non fà niente
                    break;
            }
        }
        return builder.build();
    }

}
