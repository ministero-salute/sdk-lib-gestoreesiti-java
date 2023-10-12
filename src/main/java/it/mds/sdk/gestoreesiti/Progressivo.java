package it.mds.sdk.gestoreesiti;

import it.mds.sdk.gestoreesiti.conf.Configurazione;

import java.io.IOException;

public interface Progressivo {

    /**
     * <p>Genera un nuovo progressivo idRun</p>
     *
     * @return ritorna un progressivo
     */
    String generaProgressivo();

    /**
     * <p>Crea un nuovo progressivo che mantiene nella fonte scelta.</p>
     *
     * @param fonte dove mantiene lo stato del progressivo
     * @return nuovo oggetto per la generazione dei progressivi
     */
    static Progressivo creaProgressivo(final Fonte fonte) {
        switch (fonte) {
            case FILE:
                try {
                    return new ProgressivoDaFile(new Configurazione().getFileProgressivo().getPercorso());
                } catch (IOException e) {
                    return null;
                }
            case MEMORIA:
                throw new UnsupportedOperationException("Tipo di memorizzazione ancora non implementata");
            default:
                throw new IllegalArgumentException("Il tipo di memorizzazione non è supportata");
        }
    }

    /**
     * <p>Fonti per la memorizzazione del progressivo</p>
     */
    enum Fonte {
        MEMORIA,
        FILE
    }

}
