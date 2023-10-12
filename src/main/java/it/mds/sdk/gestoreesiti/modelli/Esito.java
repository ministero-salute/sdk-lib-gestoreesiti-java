package it.mds.sdk.gestoreesiti.modelli;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Esito extends AbstractSecurityModel {
    String campo;
    private String valoreScarto;
    boolean valoreEsito;
    List<ErroreValidazione> erroriValidazione;

    @Builder(setterPrefix = "with")
    public Esito(@JsonProperty("campo") final String campo,
                 @JsonProperty("valoreScarto") final String valoreScarto,
                 @JsonProperty("valoreEsito") final boolean valoreEsito,
                 @JsonProperty("erroriValidazione") List<ErroreValidazione> erroriValidazione) {
        this.campo = campo;
        this.valoreScarto = valoreScarto;
        this.valoreEsito = valoreEsito;
        this.erroriValidazione = erroriValidazione;
    }

}
