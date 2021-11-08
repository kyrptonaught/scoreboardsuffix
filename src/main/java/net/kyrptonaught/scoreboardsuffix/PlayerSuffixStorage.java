package net.kyrptonaught.scoreboardsuffix;

import blue.endless.jankson.api.SyntaxError;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

import java.util.HashMap;

public class PlayerSuffixStorage extends PersistentState {

    public final HashMap<String, HashMap<String, String>> playerFonts = new HashMap<>();
    public String rawSuffixFormat;
    public SuffixFormat suffixFormat;

    public PlayerSuffixStorage() {
        super();
    }

    public static PlayerSuffixStorage fromNbt(NbtCompound tag) {
        PlayerSuffixStorage cman = new PlayerSuffixStorage();
        cman.setSuffixFormatInput(tag.getString("suffixformat"));

        NbtCompound playersNBT = (NbtCompound) tag.get("playerFonts");
        playersNBT.getKeys().forEach(playerName -> {
            NbtCompound playerFontsNBT = (NbtCompound) playersNBT.get(playerName);
            playerFontsNBT.getKeys().forEach(placeholder -> {
                String font = playerFontsNBT.getString(placeholder);
                cman.setFont(playerName, placeholder, font);
            });
        });
        return cman;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putString("suffixformat", rawSuffixFormat);
        NbtCompound playersNBT = new NbtCompound();
        playerFonts.forEach((playerName, playerFonts) -> {
            NbtCompound playerFontsNBT = new NbtCompound();
            playerFonts.forEach(playerFontsNBT::putString);
            playersNBT.put(playerName, playerFontsNBT);
        });
        tag.put("playerFonts", playersNBT);
        return tag;
    }

    public void setSuffixFormatInput(String input) {
        rawSuffixFormat = input;
        try {
            suffixFormat = ScoreboardSuffixMod.JANKSON.fromJson(rawSuffixFormat, SuffixFormat.class);
            suffixFormat.format();
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
    }

    public String getFont(String playerName, String placeholder) {
        if (playerFonts.containsKey(playerName)) {
            return playerFonts.get(playerName).getOrDefault(placeholder, placeholder);
        }
        return placeholder;
    }

    public void setFont(String playerName, String placeholder, String font) {
        playerFonts.putIfAbsent(playerName, new HashMap<>());
        playerFonts.get(playerName).put(placeholder, font);
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}