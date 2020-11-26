package yes.game;

import net.minecraft.util.ActionResult;
import xyz.nucleoid.plasmid.game.*;
import xyz.nucleoid.plasmid.game.event.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import yes.game.map.DarfWarMap;
import yes.game.map.DarfWarMapGenerator;
import xyz.nucleoid.fantasy.BubbleWorldConfig;

public class DarfWarWaiting {
    private final GameSpace gameSpace;
    private final DarfWarMap map;
    private final DarfWarConfig config;
    private final DarfWarSpawnLogic spawnLogic;

    private DarfWarWaiting(GameSpace gameSpace, DarfWarMap map, DarfWarConfig config) {
        this.gameSpace = gameSpace;
        this.map = map;
        this.config = config;
        this.spawnLogic = new DarfWarSpawnLogic(gameSpace, map);
    }

    public static GameOpenProcedure open(GameOpenContext<DarfWarConfig> context) {
        DarfWarConfig config = context.getConfig();
        DarfWarMapGenerator generator = new DarfWarMapGenerator(config.mapConfig);
        DarfWarMap map = generator.build();

        BubbleWorldConfig worldConfig = new BubbleWorldConfig()
                .setGenerator(map.asGenerator(context.getServer()))
                .setDefaultGameMode(GameMode.SPECTATOR);

        return context.createOpenProcedure(worldConfig, game -> {
            DarfWarWaiting waiting = new DarfWarWaiting(game.getSpace(), map, context.getConfig());

            GameWaitingLobby.applyTo(game, config.playerConfig);

            game.on(RequestStartListener.EVENT, waiting::requestStart);
            game.on(PlayerAddListener.EVENT, waiting::addPlayer);
            game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
        });
    }

    private StartResult requestStart() {
        DarfWarActive.open(this.gameSpace, this.map, this.config);
        return StartResult.OK;
    }

    private void addPlayer(ServerPlayerEntity player) {
        this.spawnPlayer(player);
    }

    private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
        player.setHealth(20.0f);
        this.spawnPlayer(player);
        return ActionResult.FAIL;
    }

    private void spawnPlayer(ServerPlayerEntity player) {
        this.spawnLogic.resetPlayer(player, GameMode.ADVENTURE);
        this.spawnLogic.spawnPlayer(player);
    }
}
