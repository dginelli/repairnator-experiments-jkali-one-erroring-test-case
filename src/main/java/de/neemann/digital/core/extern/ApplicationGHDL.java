/*
 * Copyright (c) 2018 Helmut Neemann.
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.core.extern;

import de.neemann.digital.core.element.Keys;
import de.neemann.digital.core.extern.handler.ProcessInterface;
import de.neemann.digital.core.extern.handler.StdIOInterface;
import de.neemann.digital.gui.Settings;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Abstraction of the ghdl Application.
 * See https://github.com/ghdl/ghdl
 */
public class ApplicationGHDL extends ApplicationVHDLStdIO {

    @Override
    public ProcessInterface start(String label, String code, PortDefinition inputs, PortDefinition outputs) throws IOException {
        File file = null;
        try {
            String ghdl = getGhdlPath().getPath();

            file = createVHDLFile(label, code, inputs, outputs);
            ProcessStarter.start(file.getParentFile(), ghdl, "-a", "--ieee=synopsys", file.getName());
            ProcessStarter.start(file.getParentFile(), ghdl, "-e", "--ieee=synopsys", "stdIOInterface");
            ProcessBuilder pb = new ProcessBuilder(ghdl, "-r", "--ieee=synopsys", "stdIOInterface").redirectErrorStream(true).directory(file.getParentFile());
            return new GHDLProcessInterface(pb.start(), file.getParentFile());
        } catch (IOException e) {
            if (file != null)
                ProcessStarter.removeFolder(file.getParentFile());
            throw e;
        }
    }

    @Override
    public boolean checkSupported() {
        return true;
    }

    @Override
    public String checkCode(String label, String code, PortDefinition inputs, PortDefinition outputs) throws IOException {
        File file = null;
        try {
            String ghdl = getGhdlPath().getPath();

            file = createVHDLFile(label, code, inputs, outputs);
            String m1 = ProcessStarter.start(file.getParentFile(), ghdl, "-a", "--ieee=synopsys", file.getName());
            String m2 = ProcessStarter.start(file.getParentFile(), ghdl, "-e", "--ieee=synopsys", "stdIOInterface");
            return ProcessStarter.joinStrings(m1, m2);
        } finally {
            if (file != null)
                ProcessStarter.removeFolder(file.getParentFile());
        }
    }

    private static File getGhdlPath() {
        return Settings.getInstance().get(Keys.SETTINGS_GHDL_PATH);
    }

    private static final class GHDLProcessInterface extends StdIOInterface {
        private final File folder;

        private GHDLProcessInterface(Process process, File folder) {
            super(process);
            this.folder = folder;
        }

        @Override
        public String getConsoleOutNoWarn(LinkedList<String> consoleOut) {
            StringBuilder sb = new StringBuilder();
            for (String s : consoleOut) {
                if (!s.contains("(assertion warning)"))
                    sb.append(s).append("\n");
            }
            return sb.toString();
        }

        @Override
        public void close() throws IOException {
            super.close();
            ProcessStarter.removeFolder(folder);
        }
    }
}
