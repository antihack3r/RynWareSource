// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.server.management;

import java.text.ParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.common.collect.Iterators;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParseException;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.io.Reader;
import net.minecraft.util.JsonUtils;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import com.google.gson.GsonBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.ParameterizedType;
import java.io.File;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.GameProfile;
import java.util.Deque;
import java.util.UUID;
import java.util.Map;
import java.text.SimpleDateFormat;

public class PlayerProfileCache
{
    public static final SimpleDateFormat DATE_FORMAT;
    private static boolean onlineMode;
    private final Map<String, ProfileEntry> usernameToProfileEntryMap;
    private final Map<UUID, ProfileEntry> uuidToProfileEntryMap;
    private final Deque<GameProfile> gameProfiles;
    private final GameProfileRepository profileRepo;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType TYPE;
    
    public PlayerProfileCache(final GameProfileRepository profileRepoIn, final File usercacheFileIn) {
        this.usernameToProfileEntryMap = Maps.newHashMap();
        this.uuidToProfileEntryMap = Maps.newHashMap();
        this.gameProfiles = Lists.newLinkedList();
        this.profileRepo = profileRepoIn;
        this.usercacheFile = usercacheFileIn;
        final GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeHierarchyAdapter((Class)ProfileEntry.class, (Object)new Serializer());
        this.gson = gsonbuilder.create();
        this.load();
    }
    
    private static GameProfile lookupProfile(final GameProfileRepository profileRepoIn, final String name) {
        final GameProfile[] agameprofile = { null };
        final ProfileLookupCallback profilelookupcallback = (ProfileLookupCallback)new ProfileLookupCallback() {
            public void onProfileLookupSucceeded(final GameProfile p_onProfileLookupSucceeded_1_) {
                agameprofile[0] = p_onProfileLookupSucceeded_1_;
            }
            
            public void onProfileLookupFailed(final GameProfile p_onProfileLookupFailed_1_, final Exception p_onProfileLookupFailed_2_) {
                agameprofile[0] = null;
            }
        };
        profileRepoIn.findProfilesByNames(new String[] { name }, Agent.MINECRAFT, profilelookupcallback);
        if (!isOnlineMode() && agameprofile[0] == null) {
            final UUID uuid = EntityPlayer.getUUID(new GameProfile((UUID)null, name));
            final GameProfile gameprofile = new GameProfile(uuid, name);
            profilelookupcallback.onProfileLookupSucceeded(gameprofile);
        }
        return agameprofile[0];
    }
    
    public static void setOnlineMode(final boolean onlineModeIn) {
        PlayerProfileCache.onlineMode = onlineModeIn;
    }
    
    private static boolean isOnlineMode() {
        return PlayerProfileCache.onlineMode;
    }
    
    public void addEntry(final GameProfile gameProfile) {
        this.addEntry(gameProfile, null);
    }
    
    private void addEntry(final GameProfile gameProfile, Date expirationDate) {
        final UUID uuid = gameProfile.getId();
        if (expirationDate == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(2, 1);
            expirationDate = calendar.getTime();
        }
        final String s = gameProfile.getName().toLowerCase(Locale.ROOT);
        final ProfileEntry playerprofilecache$profileentry = new ProfileEntry(gameProfile, expirationDate);
        if (this.uuidToProfileEntryMap.containsKey(uuid)) {
            final ProfileEntry playerprofilecache$profileentry2 = this.uuidToProfileEntryMap.get(uuid);
            this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry2.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(gameProfile);
        }
        this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), playerprofilecache$profileentry);
        this.uuidToProfileEntryMap.put(uuid, playerprofilecache$profileentry);
        this.gameProfiles.addFirst(gameProfile);
        this.save();
    }
    
    @Nullable
    public GameProfile getGameProfileForUsername(final String username) {
        final String s = username.toLowerCase(Locale.ROOT);
        ProfileEntry playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
        if (playerprofilecache$profileentry != null && new Date().getTime() >= playerprofilecache$profileentry.expirationDate.getTime()) {
            this.uuidToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(playerprofilecache$profileentry.getGameProfile());
            playerprofilecache$profileentry = null;
        }
        if (playerprofilecache$profileentry != null) {
            final GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        }
        else {
            final GameProfile gameprofile2 = lookupProfile(this.profileRepo, s);
            if (gameprofile2 != null) {
                this.addEntry(gameprofile2);
                playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
            }
        }
        this.save();
        return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
    }
    
    public String[] getUsernames() {
        final List<String> list = Lists.newArrayList((Iterable)this.usernameToProfileEntryMap.keySet());
        return list.toArray(new String[list.size()]);
    }
    
    @Nullable
    public GameProfile getProfileByUUID(final UUID uuid) {
        final ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
        return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
    }
    
    private ProfileEntry getByUUID(final UUID uuid) {
        final ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
        if (playerprofilecache$profileentry != null) {
            final GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        }
        return playerprofilecache$profileentry;
    }
    
    public void load() {
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = Files.newReader(this.usercacheFile, StandardCharsets.UTF_8);
            final List<ProfileEntry> list = JsonUtils.func_193841_a(this.gson, bufferedreader, PlayerProfileCache.TYPE);
            this.usernameToProfileEntryMap.clear();
            this.uuidToProfileEntryMap.clear();
            this.gameProfiles.clear();
            if (list != null) {
                for (final ProfileEntry playerprofilecache$profileentry : Lists.reverse((List)list)) {
                    if (playerprofilecache$profileentry != null) {
                        this.addEntry(playerprofilecache$profileentry.getGameProfile(), playerprofilecache$profileentry.getExpirationDate());
                    }
                }
            }
        }
        catch (final FileNotFoundException ex) {}
        catch (final JsonParseException ex2) {}
        finally {
            IOUtils.closeQuietly((Reader)bufferedreader);
        }
    }
    
    public void save() {
        final String s = this.gson.toJson((Object)this.getEntriesWithLimit(1000));
        BufferedWriter bufferedwriter = null;
        try {
            bufferedwriter = Files.newWriter(this.usercacheFile, StandardCharsets.UTF_8);
            bufferedwriter.write(s);
        }
        catch (final FileNotFoundException ex) {}
        catch (final IOException var9) {}
        finally {
            IOUtils.closeQuietly((Writer)bufferedwriter);
        }
    }
    
    private List<ProfileEntry> getEntriesWithLimit(final int limitSize) {
        final List<ProfileEntry> list = Lists.newArrayList();
        for (final GameProfile gameprofile : Lists.newArrayList(Iterators.limit((Iterator)this.gameProfiles.iterator(), limitSize))) {
            final ProfileEntry playerprofilecache$profileentry = this.getByUUID(gameprofile.getId());
            if (playerprofilecache$profileentry != null) {
                list.add(playerprofilecache$profileentry);
            }
        }
        return list;
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        TYPE = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { ProfileEntry.class };
            }
            
            @Override
            public Type getRawType() {
                return List.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
    
    class ProfileEntry
    {
        private final GameProfile gameProfile;
        private final Date expirationDate;
        
        private ProfileEntry(final GameProfile gameProfileIn, final Date expirationDateIn) {
            this.gameProfile = gameProfileIn;
            this.expirationDate = expirationDateIn;
        }
        
        public GameProfile getGameProfile() {
            return this.gameProfile;
        }
        
        public Date getExpirationDate() {
            return this.expirationDate;
        }
    }
    
    class Serializer implements JsonDeserializer<ProfileEntry>, JsonSerializer<ProfileEntry>
    {
        private Serializer() {
        }
        
        public JsonElement serialize(final ProfileEntry p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("name", p_serialize_1_.getGameProfile().getName());
            final UUID uuid = p_serialize_1_.getGameProfile().getId();
            jsonobject.addProperty("uuid", (uuid == null) ? "" : uuid.toString());
            jsonobject.addProperty("expiresOn", PlayerProfileCache.DATE_FORMAT.format(p_serialize_1_.getExpirationDate()));
            return (JsonElement)jsonobject;
        }
        
        public ProfileEntry deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (!p_deserialize_1_.isJsonObject()) {
                return null;
            }
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final JsonElement jsonelement = jsonobject.get("name");
            final JsonElement jsonelement2 = jsonobject.get("uuid");
            final JsonElement jsonelement3 = jsonobject.get("expiresOn");
            if (jsonelement == null || jsonelement2 == null) {
                return null;
            }
            final String s = jsonelement2.getAsString();
            final String s2 = jsonelement.getAsString();
            Date date = null;
            if (jsonelement3 != null) {
                try {
                    date = PlayerProfileCache.DATE_FORMAT.parse(jsonelement3.getAsString());
                }
                catch (final ParseException var14) {
                    date = null;
                }
            }
            if (s2 != null && s != null) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(s);
                }
                catch (final Throwable var15) {
                    return null;
                }
                final PlayerProfileCache this$0 = PlayerProfileCache.this;
                this$0.getClass();
                return new ProfileEntry(new GameProfile(uuid, s2), date);
            }
            return null;
        }
    }
}
