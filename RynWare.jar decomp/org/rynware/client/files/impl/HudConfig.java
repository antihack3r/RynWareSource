// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.files.impl;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import org.rynware.client.draggable.component.DraggableComponent;
import org.rynware.client.Main;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import org.rynware.client.files.FileManager;

public class HudConfig extends FileManager.CustomFile
{
    public HudConfig(final String name, final boolean loadOnStart) {
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
                final String x = curLine.split(":")[1];
                final String y = curLine.split(":")[2];
                for (final DraggableComponent hudModule : Main.instance.draggableHUD.getComponents()) {
                    if (hudModule.getName().equals(curLine.split(":")[0])) {
                        hudModule.setX(Integer.parseInt(x));
                        hudModule.setY(Integer.parseInt(y));
                    }
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
            for (final DraggableComponent draggableModule : Main.instance.draggableHUD.getComponents()) {
                out.write(draggableModule.getName() + ":" + draggableModule.getX() + ":" + draggableModule.getY());
                out.write("\r\n");
            }
            out.close();
        }
        catch (final Exception ex) {}
    }
}
