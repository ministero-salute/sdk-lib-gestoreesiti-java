package it.mds.sdk.gestoreesiti.modelli;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidazioneFlusso extends AbstractSecurityModel {
    String numeroRecord;
    Object recordProcessato;
    List<Esito> listEsiti;

    public List<Esito> getListEsiti() {
        if (listEsiti == null) {
            listEsiti = new ArrayList<>();
        }
        return listEsiti;
    }

}
