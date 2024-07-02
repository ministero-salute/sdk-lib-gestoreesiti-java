/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti.modelli;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRun extends AbstractSecurityModel {
    String idRun;
    String idClient;
    List<String> idUploads;
    TipoElaborazione tipoElaborazione;
    ModalitaOperativa modOperativa;
    Timestamp dataInizioEsecuzione;
    Timestamp dataFineEsecuzione;
    StatoRun statoEsecuzione;
    String descrizioneStatoEsecuzione;
    String fileAssociatiRun;
    String nomeFlusso;
    int numeroRecord;
    int numeroRecordAccettati;
    int numeroRecordScartati;
    String version;
    Timestamp timestampCreazione;
    String api;
    String identificativoSoggettoAlimentante;
    String tipoAtto;
    String numeroAtto;
    String tipoEsitoMds;
    Timestamp dataRicevutaMds;
    String codiceRegione;
    String annoRiferimento;
    String periodoRiferimento;
    String nomeFileOutputMds;
    String esitoAcquisizioneFlusso;
    String codiceErroreInvioFlusso;
    String testoErroreInvioFlusso;



    @Builder(setterPrefix = "with")
    public InfoRun(@JsonProperty("idRun") String idRun,
                   @JsonProperty("idClient") String idClient,
                   @JsonProperty("idUploads") List<String> idUploads,
                   @JsonProperty("tipoElaborazione") TipoElaborazione tipoElaborazione,
                   @JsonProperty("modOperativa") ModalitaOperativa modOperativa,
                   @JsonProperty("dataInizioEsecuzione") Timestamp dataInizioEsecuzione,
                   @JsonProperty("dataFineEsecuzione") Timestamp dataFineEsecuzione,
                   @JsonProperty("statoEsecuzione") StatoRun statoEsecuzione,
                   @JsonProperty("descrizioneStatoEsecuzione") String descrizioneStatoEsecuzione,
                   @JsonProperty("fineAssociatiRun") String fileAssociatiRun,
                   @JsonProperty("nomeFlusso") String nomeFlusso,
                   @JsonProperty("numeroRecord") int numeroRecord,
                   @JsonProperty("numeroRecordAccettati") int numeroRecordAccettati,
                   @JsonProperty("numeroRecordScartati") int numeroRecordScartati,
                   @JsonProperty("version") String version,
                   @JsonProperty("timestampCreazione") Timestamp timestampCreazione,
                   @JsonProperty("api") String api,
                   @JsonProperty("identificativoSoggettoAlimentante") String identificativoSoggettoAlimentante,
                   @JsonProperty("tipoAtto") String tipoAtto,
                   @JsonProperty("numeroAtto") String numeroAtto,
                   @JsonProperty("tipoEsitoMds") String tipoEsitoMds,
                   @JsonProperty("dataRicevutaMds") Timestamp dataRicevutaMds,
                   @JsonProperty("codiceRegione") String codiceRegione,
                   @JsonProperty("annoRiferimento") String annoRiferimento,
                   @JsonProperty("periodoRiferimento") String periodoRiferimento,
                   @JsonProperty("nomeFileOutputMds") String nomeFileOutputMds,
                   @JsonProperty("esitoAcquisizioneFlusso") String esitoAcquisizioneFlusso,
                   @JsonProperty("codiceErroreInvioFlusso") String codiceErroreInvioFlusso,
                   @JsonProperty("testoErroreInvioFlusso") String testoErroreInvioFlusso
                   ) {
        this.idRun = idRun;
        this.idClient = idClient;
        this.idUploads = idUploads;
        this.tipoElaborazione = tipoElaborazione;
        this.modOperativa = modOperativa;
        this.dataInizioEsecuzione = dataInizioEsecuzione;
        this.dataFineEsecuzione = dataFineEsecuzione;
        this.statoEsecuzione = statoEsecuzione;
        this.descrizioneStatoEsecuzione = descrizioneStatoEsecuzione;
        this.fileAssociatiRun = fileAssociatiRun;
        this.nomeFlusso = nomeFlusso;
        this.numeroRecord = numeroRecord;
        this.numeroRecordAccettati = numeroRecordAccettati;
        this.numeroRecordScartati = numeroRecordScartati;
        this.version = version;
        this.timestampCreazione = timestampCreazione;
        this.api = api;
        this.identificativoSoggettoAlimentante = identificativoSoggettoAlimentante;
        this.tipoAtto = tipoAtto;
        this.numeroAtto = numeroAtto;
        this.tipoEsitoMds = tipoEsitoMds;
        this.dataRicevutaMds = dataRicevutaMds;
        this.codiceRegione = codiceRegione;
        this.annoRiferimento = annoRiferimento;
        this.periodoRiferimento = periodoRiferimento;
        this.nomeFileOutputMds =  nomeFileOutputMds;
        this.esitoAcquisizioneFlusso = esitoAcquisizioneFlusso;
        this.codiceErroreInvioFlusso = codiceErroreInvioFlusso;
        this.testoErroreInvioFlusso = testoErroreInvioFlusso;
    }

}
