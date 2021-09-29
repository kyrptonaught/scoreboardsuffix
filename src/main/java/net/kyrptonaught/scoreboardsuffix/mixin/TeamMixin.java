package net.kyrptonaught.scoreboardsuffix.mixin;

import net.kyrptonaught.scoreboardsuffix.SuffixFormat;
import net.kyrptonaught.scoreboardsuffix.ScoreboardSuffixMod;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TeamS2CPacket.SerializableTeam.class)
public class TeamMixin {

    @Redirect(method = "<init>(Lnet/minecraft/scoreboard/Team;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/Team;getSuffix()Lnet/minecraft/text/Text;"))
    public Text injectSuffix(Team team) {
        if (team.getPlayerList().size() == 1 && ScoreboardSuffixMod.playerSuffixStorage != null && ScoreboardSuffixMod.playerSuffixStorage.suffixFormat != null) {
            Scoreboard scoreboard = team.getScoreboard();
            String player = team.getPlayerList().iterator().next();
            MutableText suffix = team.getSuffix().copy();
            ScoreboardSuffixMod.playerSuffixStorage.suffixFormat.scoreboardSuffixes.forEach(newSuffix -> {
                if (newSuffix instanceof SuffixFormat.ScoreboardSuffix) {
                    String scoreboardName = newSuffix.suffix;
                    int score = team.getScoreboard().getPlayerScore(player, scoreboard.getObjective(scoreboardName)).getScore();
                    ((SuffixFormat.ScoreboardSuffix) newSuffix).updateText(score);
                }
                Style style = newSuffix.displayText.getStyle();
                String fontPlaceHolder = style.getFont().toString();
                style = style.withFont(new Identifier(ScoreboardSuffixMod.playerSuffixStorage.getFont(player, fontPlaceHolder)));
                suffix.append(newSuffix.displayText.copy().setStyle(style));
            });
            return suffix;
        }
        return team.getSuffix();
    }
}