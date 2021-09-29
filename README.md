
# Scoreboard Suffix

Server side mod adding two commands for adding a more customizable suffix for players in tablist and above their player. This mod will not do anything when installed on clients, and will function with vanilla clients.

## How to use

This mod adds two commands: 

    /setScoreboardSuffix <format>
    /setSuffixFont <fontPlaceHolder> <font> [player]
`/setScoreboardSuffix` has it's own format, here's an example of how they work: `[{MinecraftJsonHere}, "String", "scoreboard=ScoreboardObjectiveHere"]`

`MinecraftJsonHere` Uses standard Minecraft tellraw JSON data, you can easily generate some using [MinecraftJson.com](https://www.minecraftjson.com/). Now when referencing a font, you can reference a placeholder, which can then be set by `/setSuffixFont`

`String` Is pretty self-explanitory, you can put a string of text here.

`scoreboard=ScoreboardObjectiveHere` Can reference a scoreboard objective here, and it will be displayed. Do keep in mind if a player is on a team with a color, that color will be applied to the number displayed.

`/setSuffixFont` Has 3 arguments `fontPlaceHolder`, `font`, and `player`

`fontPlaceHolder` Can be set in `/setScoreboardSuffix` when referencing a font.

`font` Is the font to set the placeholder to reference, if you are using a custom namespace in a resource pack, put the font in quotations, for example `"mypack:fontname"`.

Here are some examples of these commands:

    /setScoreboardSuffix [{"text":"This is in uniform font","color":"white","font":"minecraft:uniform"}, {"text":"placeholder font!","color":"white","font":"font1"}, "scoreboard=killcount"]
    /setSuffixFont font1 "mypack:coolskeleton95" @a[gamemode=survival]

## Installing

You may either compile the mod yourself with gradle, or download it from the [Releases Page](https://github.com/kyrptonaught/scoreboardsuffix/releases).

**This mod was a request from the wonderful folks behind the Legacy Edition Battle project**
Check them out [here](https://www.planetminecraft.com/project/legacy-edition-battle/), or their [github page](https://github.com/DBTDerpbox/Legacy-Edition-Battle).

