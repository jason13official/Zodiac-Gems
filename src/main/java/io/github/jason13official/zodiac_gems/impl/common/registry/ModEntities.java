package io.github.jason13official.zodiac_gems.impl.common.registry;

import io.github.jason13official.zodiac_gems.ZodiacGems;
import io.github.jason13official.zodiac_gems.impl.common.entity.PlayerBodyDouble;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static EntityType<PlayerBodyDouble> BODY_DOUBLE;

  public static void register(BiConsumer<EntityType<?>, ResourceLocation> consumer) {

    BODY_DOUBLE = EntityType.Builder.<PlayerBodyDouble>of(PlayerBodyDouble::new, MobCategory.MISC).noSave()
        .noSummon()
        .sized(0.6F, 1.8F)
        .eyeHeight(1.62F)
        .clientTrackingRange(32)
        .updateInterval(2).build(ZodiacGems.identifier("body_double").toString());

    consumer.accept(BODY_DOUBLE, ZodiacGems.identifier("body_double"));
  }

}
