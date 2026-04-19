package io.github.jason13official.zodiac_gems.impl.client.model;

import io.github.jason13official.zodiac_gems.impl.common.entity.PlayerBodyDouble;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public class PlayerBodyDoubleModel extends HumanoidModel<PlayerBodyDouble> {

  public PlayerBodyDoubleModel(ModelPart pRoot) {
    super(pRoot);
  }

  @Override
  public void setupAnim(PlayerBodyDouble pEntity, float pLimbSwing, float pLimbSwingAmount,
      float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm,
        pEntity.isAttacking(), this.attackTime, pAgeInTicks);
  }
}
