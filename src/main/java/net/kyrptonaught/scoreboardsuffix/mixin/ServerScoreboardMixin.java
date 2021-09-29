package net.kyrptonaught.scoreboardsuffix.mixin;

import net.kyrptonaught.scoreboardsuffix.ScoreboardSuffixMod;
import net.minecraft.scoreboard.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends Scoreboard {

    @Inject(method = "updateScore", at = @At("TAIL"))
    public void updateTeamSuffix(ScoreboardPlayerScore score, CallbackInfo ci) {
        String name = score.getObjective() != null ? score.getObjective().getName() : "";
        if (ScoreboardSuffixMod.playerSuffixStorage != null && ScoreboardSuffixMod.playerSuffixStorage.suffixFormat != null && ScoreboardSuffixMod.playerSuffixStorage.suffixFormat.scoreboardNames.contains(name) && score.getScore() != 0) {
            Team team = this.getPlayerTeam(score.getPlayerName());
            if (team != null)
                this.updateScoreboardTeam(team);
        }
    }
}
