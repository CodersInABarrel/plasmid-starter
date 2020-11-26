package yes;

import net.fabricmc.api.ModInitializer;
import xyz.nucleoid.plasmid.game.GameType;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yes.game.DarfWarConfig;
import yes.game.DarfWarWaiting;

public class DarfWar implements ModInitializer {

    public static final String ID = "dwarfwar";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final GameType<DarfWarConfig> TYPE = GameType.register(
            new Identifier(ID, "dwarfwar"),
            DarfWarWaiting::open,
            DarfWarConfig.CODEC
    );

    @Override
    public void onInitialize() {}
}
