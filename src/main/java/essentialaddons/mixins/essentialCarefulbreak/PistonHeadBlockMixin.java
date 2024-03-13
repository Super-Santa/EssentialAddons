package essentialaddons.mixins.essentialCarefulbreak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
	@ModifyExpressionValue(
		method = "onBreak",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"
		)
	)
	private boolean shouldBreak(boolean original) {
		// Minecraft for some reason only handles creative
		// players breaking here...?
		// We make it handle all players
		return true;
	}

	@Redirect(
		method = "onBreak",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;breakBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
		)
	)
	private boolean breakBlock(
		World instance,
		BlockPos blockPos,
		boolean drop,
		World world,
		BlockPos pos,
		BlockState state,
		PlayerEntity player
	) {
		return instance.breakBlock(blockPos, !player.getAbilities().creativeMode, player);
	}
}
