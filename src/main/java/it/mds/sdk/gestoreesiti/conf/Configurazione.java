package it.mds.sdk.gestoreesiti.conf;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Getter
public class Configurazione {
    Run run;
    Esito esito;
    Monitoraggio monitoraggio;
    FileProgressivo fileProgressivo;

    public Configurazione() {
        this(leggiConfigurazione("config.properties"));
    }

    public Configurazione(final Properties conf) {
        log.debug("Properties salvate\n");
        // Imposta gli oggetti con le configurazioni lette dal file
        this.run = Run.builder()
                .withPercorso(conf.getProperty("run.percorso", ""))
                .build();
        this.esito = Esito.builder()
                .withPercorso(conf.getProperty("esito.percorso", ""))
                .build();
        this.monitoraggio = Monitoraggio.builder()
                .withFus(conf.getProperty("monitoraggio.fus", ""))
                .withLog(conf.getProperty("monitoraggio.log", ""))
                .build();
        this.fileProgressivo = FileProgressivo.builder()
                .withPercorso(conf.getProperty("progressivo.percorso", ""))
                .build();
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Run {
        String percorso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Esito {
        String percorso;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class Monitoraggio {
        String fus;
        String log;
    }

    @Value
    @Builder(setterPrefix = "with")
    public static class FileProgressivo {
        String percorso;
    }

    private static Properties leggiConfigurazione(final String nomeFile) {
        final Properties prop = new Properties();
        if(Configurazione.class.getClassLoader() == null){
            log.trace("{}.getClassLoader() is null", Configurazione.class);
            throw new NullPointerException();
        }

        try (final InputStream is = Configurazione.class.getClassLoader().getResourceAsStream(nomeFile)) {
            prop.load(is);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
        }
        return prop;
    }

}
