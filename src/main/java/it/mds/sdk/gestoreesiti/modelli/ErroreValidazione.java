package it.mds.sdk.gestoreesiti.modelli;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ErroreValidazione {
    String codice;
    String descrizione;

    @Builder(setterPrefix = "with")
    public ErroreValidazione(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }
}
