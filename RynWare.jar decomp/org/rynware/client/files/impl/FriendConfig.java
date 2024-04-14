// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.files.impl;

import java.util.Iterator;
import org.rynware.client.friend.Friend;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.rynware.client.Main;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.rynware.client.files.FileManager;

public class FriendConfig extends FileManager.CustomFile
{
    public FriendConfig(final String name, final boolean loadOnStart) {
        super(name, loadOnStart);
    }
    
    @Override
    public void loadFile() {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                if (Main.instance.friendManager == null) {
                    continue;
                }
                Main.instance.friendManager.addFriend(name);
            }
            br.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveFile() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (final Friend friend : Main.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
