/* SPDX-License-Identifier: BSD-3-Clause */

package it.mds.sdk.gestoreesiti;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgressivoDaFileTest {
    private ProgressivoDaFile progressivo;

    @BeforeEach
    void setUp(@TempDir Path tmpDir) throws IOException {
        progressivo = new ProgressivoDaFile(tmpDir.resolve("progressivo.dat").toString());
    }

    @Test
    void inizia_sequenza_da_zero_test() {
        assertEquals("0", progressivo.generaProgressivo());
    }

    @Test
    void progressivo_in_sequenza_test() {
        assertEquals("0", progressivo.generaProgressivo());
        assertEquals("1", progressivo.generaProgressivo());
        assertEquals("2", progressivo.generaProgressivo());
    }
}
