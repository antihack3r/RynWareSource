// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.files.impl;

import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.rynware.client.command.macro.Macro;
import org.lwjgl.input.Keyboard;
import org.rynware.client.Main;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import org.rynware.client.files.FileManager;

public class MacroConfig extends FileManager.CustomFile
{
    public MacroConfig(final String name, final boolean loadOnStart) {
        super(name, loadOnStart);
    }
    
    @Override
    public void loadFile() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            final DataInputStream in = new DataInputStream(fileInputStream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String bind = curLine.split(":")[0];
                final String value = curLine.split(":")[1];
                if (Main.instance.macroManager != null) {
                    Main.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
                }
            }
            br.close();
        }
        catch (final Exception ex) {}
    }
    
    @Override
    public void saveFile() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (final Macro m : Main.instance.macroManager.getMacros()) {
                if (m != null) {
                    out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                    out.write("\r\n");
                }
            }
            out.close();
        }
        catch (final Exception ex) {}
    }
}
