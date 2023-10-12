package it.mds.sdk.gestoreesiti;

import it.mds.sdk.gestoreesiti.modelli.StatoRun;

import java.util.function.BiPredicate;

/**
 * Classe per la validazione degli stati
 */
public class StatoRunValidazione implements BiPredicate<StatoRun, StatoRun> {

    /**
     * <p>Verifica se il passaggio di stato è coerente.</p>
     *
     * @param s1 stato di partenza
     * @param s2 stato di arrivo
     * @return true se lo stato di arrivo è coerente con lo stato di partenza
     */
    @Override
    public boolean test(StatoRun s1, StatoRun s2) {
        if (s1 == StatoRun.CREATA) {
            return s2 == StatoRun.IN_ELABORAZIONE;
        } else if (s1 == StatoRun.IN_ELABORAZIONE) {
            return s2 == StatoRun.ELABORATA || s2 == StatoRun.KO_GENERICO || s2 == StatoRun.KO_GESTIONE_VALIDAZIONE
                    || s2 == StatoRun.KO_GESTIONE_FILE || s2 == StatoRun.KO_INVIO_MINISTERO || s2 == StatoRun.KO_VALIDAZIONE_SDK || s2 == StatoRun.KO_INVIO_SOGLIA;
        }
        return false;
    }

}
