package net.kyrptonaught.scoreboardsuffix;


import blue.endless.jankson.Jankson;
import blue.endless.jankson.api.SyntaxError;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class ScoreboardSuffixMod implements ModInitializer {
    public static final String MOD_ID = "scoreboardsuffix";
    public static final Jankson JANKSON = Jankson.builder().build();

    public static PlayerSuffixStorage playerSuffixStorage;

    @Override
    public void onInitialize() {
        //String input = "{\"input\" : [\" ❤\", \"scoreboard=lives\", \" ⚔\", \"scoreboard=kills\"]}";
        CommandRegistrationCallback.EVENT.register(ScoreboardSuffixMod::register);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            playerSuffixStorage = server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(PlayerSuffixStorage::fromNbt, PlayerSuffixStorage::new, MOD_ID);
        });
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(CommandManager.literal("setScoreboardSuffix")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("format", StringArgumentType.greedyString())
                        .executes(context -> {
                            String format = StringArgumentType.getString(context, "format");
                            format = "{\"input\" :" + format + "}";
                            playerSuffixStorage.setSuffixFormatInput(format);
                            triggerTeamsUpdate(context, null);
                            return 1;
                        })));
        dispatcher.register(CommandManager.literal("setSuffixFont")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("fontPlaceholder", StringArgumentType.word())
                        .then(CommandManager.argument("font", StringArgumentType.string())
                                .then(CommandManager.argument("player", EntityArgumentType.players())
                                        .executes(context -> executeSetSuffix(context, EntityArgumentType.getPlayers(context, "player"))))
                                .executes(context -> executeSetSuffix(context, Collections.singleton(context.getSource().getPlayer()))))));
    }

    public static int executeSetSuffix(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        String placeholder = new Identifier(StringArgumentType.getString(context, "fontPlaceholder")).toString();
        String font = new Identifier(StringArgumentType.getString(context, "font")).toString();
        players.forEach(serverPlayerEntity -> {
            String playerName = serverPlayerEntity.getEntityName();
            playerSuffixStorage.setFont(playerName, placeholder, font);
            triggerTeamsUpdate(context, playerName);
        });
        return 1;
    }

    public static void triggerTeamsUpdate(CommandContext<ServerCommandSource> context, String playerName) {
        Scoreboard scoreboard = context.getSource().getServer().getScoreboard();
        if (playerName == null)
            scoreboard.getTeams().forEach(scoreboard::updateScoreboardTeam);
        else {
            scoreboard.updateScoreboardTeam(scoreboard.getPlayerTeam(playerName));
        }
    }
}