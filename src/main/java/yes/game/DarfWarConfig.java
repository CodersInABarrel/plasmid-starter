package yes.game;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import xyz.nucleoid.plasmid.game.config.PlayerConfig;
import yes.game.map.DarfWarMapConfig;

public class DarfWarConfig {
    public static final Codec<DarfWarConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.playerConfig),
            DarfWarMapConfig.CODEC.fieldOf("map").forGetter(config -> config.mapConfig),
            Codec.INT.fieldOf("time_limit_secs").forGetter(config -> config.timeLimitSecs)
    ).apply(instance, DarfWarConfig::new));

    public final PlayerConfig playerConfig;
    public final DarfWarMapConfig mapConfig;
    public final int timeLimitSecs;

    public DarfWarConfig(PlayerConfig players, DarfWarMapConfig mapConfig, int timeLimitSecs) {
        this.playerConfig = players;
        this.mapConfig = mapConfig;
        this.timeLimitSecs = timeLimitSecs;
    }
}
