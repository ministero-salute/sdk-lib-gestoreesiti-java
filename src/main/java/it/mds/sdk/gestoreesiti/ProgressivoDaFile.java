package it.mds.sdk.gestoreesiti;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * Implementazione del progressivo che memorizza lo stato su un file.
 * Il file è salvato di default nella cartella temporanea del sistema operativo
 * in un file denominato progressivo.dat.
 * In caso il file non esiste viene creato all'inizializzazione della classe.
 * </p>
 */
@Slf4j
class ProgressivoDaFile implements Progressivo {
    private final File file;

    public ProgressivoDaFile() throws IOException {
        this(System.getProperty("java.io.tmpdir") + "/" + "progressivo.dat");

    }

    public ProgressivoDaFile(final String percorsoFile) throws IOException {
        this.file = new File(FilenameUtils.normalize(percorsoFile));
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
    }

    @Override
    public String generaProgressivo() {
        try {
            final Path path = file.toPath();
            log.debug("Path del file di memorizzazione del progressivo {}", FilenameUtils.normalize(String.valueOf(path.toAbsolutePath())));
            final String contenuto = Files.readString(path);
            final AtomicInteger valore = new AtomicInteger(NumberUtils.toInt(contenuto));
            final String progressivo = String.valueOf(valore.get());
            log.debug("Valore attuale {}", progressivo);
            Files.writeString(path, String.valueOf(valore.incrementAndGet()), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            return progressivo;
        } catch (IOException e) {
            log.error("Errore nella scrittura dello stato su file", e);
            return "0";
        }
    }

}
