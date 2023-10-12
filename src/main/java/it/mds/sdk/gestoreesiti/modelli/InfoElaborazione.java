package it.mds.sdk.gestoreesiti.modelli;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = false)
public class InfoElaborazione extends AbstractSecurityModel {
    String idUpload;
    String idClient;
    String nomeFlusso;
    String timestampElaborazione;
    String version;
    String codiceStatoRun;
    String descrizioneCodiceStatoRun;
    String esitoElaborazioneMds;
    String descrizioneEsitoElaborazioneMds;
    String codiceErroreElaborazioneMds;
    String testoErroreElaborazioneMds;
    String statoDownloadFus;
    String esitoDownloadFus;
    String descrizioneEsitoDownloadFus;
    String codiceErroreDownloadFus;
    String descrizioneErroreDownloadFus;
    String allegato;
    public InfoElaborazione(@JsonProperty("idUpload") String idUpload,
                            @JsonProperty("idClient") String idClient,
                            @JsonProperty("nomeFlusso") String nomeFlusso,
                            @JsonProperty("timestampElaborazione") String timestampElaborazione,
                            @JsonProperty("version") String version,
                            @JsonProperty("codiceStatoRun") String codiceStatoRun,
                            @JsonProperty("descrizioneCodiceStatoRun") String descrizioneCodiceStatoRun,
                            @JsonProperty("esitoElaborazioneMds") String esitoElaborazioneMds,
                            @JsonProperty("descrizioneEsitoElaborazioneMds") String descrizioneEsitoElaborazioneMds,
                            @JsonProperty("codiceErroreElaborazioneMds") String codiceErroreElaborazioneMds,
                            @JsonProperty("testoErroreElaborazioneMds") String testoErroreElaborazioneMds,
                            @JsonProperty("statoDownloadFus") String statoDownloadFus,
                            @JsonProperty("esitoDownloadFus") String esitoDownloadFus,
                            @JsonProperty("descrizioneEsitoDownloadFus") String descrizioneEsitoDownloadFus,
                            @JsonProperty("codiceErroreDownloadFus") String codiceErroreDownloadFus,
                            @JsonProperty("descrizioneErroreDownloadFus") String descrizioneErroreDownloadFus,
                            @JsonProperty("allegato") String allegato)
        {
        this.idUpload = idUpload;
        this.idClient = idClient;
        this.nomeFlusso = nomeFlusso;
        this.timestampElaborazione = timestampElaborazione;
        this.version = version;
        this.codiceStatoRun = codiceStatoRun;
        this.descrizioneCodiceStatoRun = descrizioneCodiceStatoRun;
        this.esitoElaborazioneMds = esitoElaborazioneMds;
        this.descrizioneEsitoElaborazioneMds = descrizioneEsitoElaborazioneMds;
        this.codiceErroreElaborazioneMds = codiceErroreElaborazioneMds;
        this.testoErroreElaborazioneMds = testoErroreElaborazioneMds;
        this.statoDownloadFus = statoDownloadFus;
        this.esitoDownloadFus = esitoDownloadFus;
        this.descrizioneEsitoDownloadFus = descrizioneEsitoDownloadFus;
        this.codiceErroreDownloadFus = codiceErroreDownloadFus;
        this.descrizioneErroreDownloadFus = descrizioneErroreDownloadFus;
        this.allegato = allegato;
    }

    public static InfoElaborazione aggiorna(InfoElaborazione infoElaborazione,
                                            Map<String, Object> valori) {
        final InfoElaborazione.InfoElaborazioneBuilder builder = InfoElaborazione.builder()
                .withIdUpload(infoElaborazione.getIdUpload())
                .withIdClient(infoElaborazione.getIdClient())
                .withNomeFlusso(infoElaborazione.getNomeFlusso())
                .withTimestampElaborazione(infoElaborazione.getTimestampElaborazione())
                .withVersion(infoElaborazione.getVersion())
                .withCodiceStatoRun(infoElaborazione.codiceStatoRun)
                .withDescrizioneCodiceStatoRun(infoElaborazione.descrizioneCodiceStatoRun)
                .withEsitoElaborazioneMds(infoElaborazione.getEsitoElaborazioneMds())
                .withDescrizioneEsitoElaborazioneMds(infoElaborazione.descrizioneEsitoElaborazioneMds)
                .withCodiceErroreElaborazioneMds(infoElaborazione.getCodiceErroreElaborazioneMds())
                .withTestoErroreElaborazioneMds(infoElaborazione.getTestoErroreElaborazioneMds())
                .withStatoDownloadFus(infoElaborazione.getStatoDownloadFus())
                .withEsitoDownloadFus(infoElaborazione.getEsitoDownloadFus())
                .withDescrizioneEsitoDownloadFus(infoElaborazione.getDescrizioneEsitoDownloadFus())
                .withCodiceErroreDownloadFus(infoElaborazione.getCodiceErroreDownloadFus())
                .withDescrizioneErroreDownloadFus(infoElaborazione.descrizioneErroreDownloadFus)
                .withAllegato(infoElaborazione.getAllegato());
        for (final var entry : valori.entrySet()) {
            switch (entry.getKey()) {
                case "esitoElaborazioneMds":
                    builder.esitoElaborazioneMds = (String) valori.get(entry.getKey());
                    break;
                case "codiceErroreElaborazioneMds":
                    builder.codiceErroreElaborazioneMds = (String) valori.get(entry.getKey());
                    break;
                case "allegato":
                    builder.allegato = (String) valori.get(entry.getKey());
                    break;
                case "statoDownloadFus":
                    builder.statoDownloadFus = (String) valori.get(entry.getKey());
                    break;
                case "esitoDownloadFus":
                    builder.esitoDownloadFus = (String) valori.get(entry.getKey());
                    break;
                case "codiceErroreDownloadFus":
                    builder.codiceErroreDownloadFus = (String) valori.get(entry.getKey());
                    break;
                case "descrizioneErroreDownloadFus":
                    builder.descrizioneErroreDownloadFus = (String) valori.get(entry.getKey());
                    break;
                default:
                    // Non fà niente
                    break;
            }
        }
        return builder.build();
    }

}
