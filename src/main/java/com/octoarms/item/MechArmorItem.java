package com.octoarms.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.octoarms.client.model.MechArmorModel;
import com.octoarms.mech.MechArmorMaterial;
import com.octoarms.mech.MechSuitTier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * One piece of a mech exosuit. Holds the charge capability, per-tier servo
 * attributes and the link to the bulky client-side armor model.
 */
public class MechArmorItem extends ArmorItem implements IChargeableItem {

    private final MechSuitTier tier;
    private final Multimap<Attribute, AttributeModifier> suitModifiers;

    public MechArmorItem(MechSuitTier tier, ArmorItem.Type type) {
        super(new MechArmorMaterial(tier), type,
                new Properties().stacksTo(1).durability(tier.durabilityFor(type)));
        this.tier = tier;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(type.getSlot()));
        UUID uuid = UUID.nameUUIDFromBytes(("octoarms:" + tier.id + ":" + type).getBytes());
        if (type == ArmorItem.Type.CHESTPLATE) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Mech servo strength",
                    tier.attackBonus, AttributeModifier.Operation.ADDITION));
        }
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Mech servo speed",
                tier.speedBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        this.suitModifiers = builder.build();
    }

    public MechSuitTier tier() {
        return tier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == getType().getSlot() ? suitModifiers : ImmutableMultimap.of();
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return tier.capacity;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ChargeUtils.charge(stack).map(c -> c.isFull()).orElse(false);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            ChargeUtils.cool(stack, 0.8F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ChargeUtils.appendChargeTooltip(stack, tooltip);
        if (flag.isAdvanced() || hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.octoarms.suit.capacity", ChargeUtils.format(tier.capacity)));
            tooltip.add(Component.translatable("tooltip.octoarms.suit.speed",
                    String.format("%.0f", tier.speedBonus * 100)));
            tooltip.add(Component.translatable("tooltip.octoarms.suit.jump",
                    String.format("%.0f", tier.jumpBonus * 100)));
            tooltip.add(Component.translatable("tooltip.octoarms.suit.strength",
                    String.format("%.1f", tier.attackBonus)));
            if (tier.canClimb) {
                tooltip.add(Component.translatable("tooltip.octoarms.suit.climb"));
                tooltip.add(Component.translatable("tooltip.octoarms.suit.arms"));
            }
            if (tier.canFly) {
                tooltip.add(Component.translatable("tooltip.octoarms.suit.flight"));
            }
            tooltip.add(Component.translatable("tooltip.octoarms.suit.setbonus", setBonusName()));
        } else {
            tooltip.add(Component.translatable("tooltip.octoarms.hold_shift"));
        }
    }

    private Component setBonusName() {
        return switch (tier) {
            case COPPER -> Component.translatable("effect.minecraft.haste");
            case IRON -> Component.translatable("effect.minecraft.resistance");
            case GOLD -> Component.translatable("effect.minecraft.speed");
            case DIAMOND -> Component.translatable("effect.minecraft.regeneration");
            case NETHERITE -> Component.translatable("effect.minecraft.strength");
            case SPIDER -> Component.translatable("effect.minecraft.strength");
        };
    }

    private static boolean hasShiftDown() {
        return net.minecraft.client.gui.screens.Screen.hasShiftDown();
    }

    // -- capability share-tag sync ----------------------------------------- //

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        return ChargeUtils.writeShare(stack, super.getShareTag(stack));
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
        super.readShareTag(stack, tag);
        ChargeUtils.readShare(stack, tag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ChargeUtils.stored(stack) < getCapacity(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * ChargeUtils.stored(stack) / (float) getCapacity(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return switch (tier) {
            case COPPER -> 0x5FE3C8;
            case IRON -> 0x7FB2FF;
            case GOLD -> 0xFFF6B0;
            case DIAMOND -> 0xDFFFFF;
            case NETHERITE -> 0xFF6A3C;
            case SPIDER -> 0xB4FF3C;
        };
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    // -- client model hook -------------------------------------------------- //

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public net.minecraft.client.model.Model getGenericArmorModel(
                    net.minecraft.world.entity.LivingEntity entity, ItemStack stack,
                    EquipmentSlot slot, net.minecraft.client.model.HumanoidModel<?> original) {
                MechArmorModel model = MechArmorModel.forTier(tier.id);
                ForgeHooksClient.copyModelProperties(original, model);
                return model;
            }
        });
    }
}
