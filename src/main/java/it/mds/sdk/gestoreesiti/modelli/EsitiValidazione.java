package it.mds.sdk.gestoreesiti.modelli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class EsitiValidazione extends AbstractSecurityModel {
    String idRun;
    List<Esito> esiti;

    @JsonCreator
    public EsitiValidazione(
            @JsonProperty("idRun") String idRun,
            @JsonProperty("esiti") List<Esito> esiti) {
        this.idRun = idRun;
        this.esiti = esiti;
    }
}
