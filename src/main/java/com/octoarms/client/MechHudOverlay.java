package com.octoarms.client;

import com.octoarms.item.ChargeUtils;
import com.octoarms.item.MechArmorItem;
import com.octoarms.mech.ClimbHandler;
import com.octoarms.mech.MechCharge;
import com.octoarms.mech.MechSuit;
import com.octoarms.mech.MechSuitTier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Optional;

/**
 * Mech HUD: suit charge, weapon heat, mode flags and the surface grip readout
 * while crawling.
 */
public class MechHudOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }
        Optional<MechSuitTier> worn = MechSuit.wornTier(player);
        if (worn.isEmpty()) {
            return;
        }
        MechSuitTier tier = worn.get();

        int x = screenWidth - 96;
        int y = screenHeight - 64;

        graphics.fill(x, y, x + 88, y + 56, 0xAA12161C);
        graphics.fill(x + 1, y + 1, x + 87, y + 2, 0xFF39424C);
        graphics.fill(x + 1, y + 54, x + 87, y + 55, 0xFF39424C);

        // charge bar
        float fraction = MechSuit.suitFraction(player);
        graphics.fill(x + 6, y + 8, x + 82, y + 16, 0xFF20262C);
        int w = Math.round(76 * fraction);
        int color = fraction > 0.5F ? 0xFF46E0B4 : fraction > 0.2F ? 0xFFE8C24A : 0xFFE0503C;
        graphics.fill(x + 6, y + 8, x + 6 + w, y + 16, color);
        graphics.drawString(mc.font, ChargeUtils.format(Math.round(fraction * tier.capacity)) + " PU",
                x + 8, y + 9, 0xFF0B0E12, false);

        // heat bar (chest piece / held weapon)
        float heat = 0;
        var chest = ChargeUtils.charge(player.getItemBySlot(EquipmentSlot.CHEST));
        if (chest.isPresent()) {
            heat = chest.get().getHeat() / MechCharge.MAX_HEAT;
        }
        var main = ChargeUtils.charge(player.getMainHandItem());
        if (main.isPresent()) {
            heat = Math.max(heat, main.get().getHeat() / MechCharge.MAX_HEAT);
        }
        graphics.fill(x + 6, y + 20, x + 82, y + 26, 0xFF20262C);
        graphics.fill(x + 6, y + 20, x + 6 + Math.round(76 * heat), y + 26,
                heat > 0.8F ? 0xFFFF5030 : 0xFFE8842C);

        // mode flags
        int fx = x + 6;
        int fy = y + 32;
        drawFlag(graphics, mc, fx, fy, "C", MechClientFlags.cling && tier.canClimb, tier.canClimb);
        drawFlag(graphics, mc, fx + 18, fy, "H", MechClientFlags.hover && tier.canFly, tier.canFly);
        drawFlag(graphics, mc, fx + 36, fy, "V", MechClientFlags.visor && tier.visorNightVision, tier.visorNightVision);

        // grip readout while crawling
        if (ClimbHandler.isClimbing(player)) {
            ClimbHandler.ClimbState state = ClimbHandler.state(player);
            String grip = state.profile == null ? "--" : Math.round(state.profile.grip * 100) + "%";
            graphics.drawString(mc.font, "GRIP " + grip, x + 6, y + 44, 0xFFB4FF3C, false);
            graphics.drawString(mc.font, state.wall == null ? "" : state.wall.getName(), x + 52, y + 44,
                    0xFF9AA7B4, false);
        } else if (player.getAbilities().flying) {
            graphics.drawString(mc.font, "THRUST", x + 6, y + 44, 0xFF7FD4FF, false);
        }
    }

    private void drawFlag(GuiGraphics graphics, Minecraft mc, int x, int y, String label, boolean on, boolean available) {
        graphics.fill(x, y, x + 14, y + 12, on ? 0xFF2E7D5B : 0xFF20262C);
        graphics.drawString(mc.font, label, x + 4, y + 2, on ? 0xFFDFFFFF : available ? 0xFF7C8894 : 0xFF3A424B, false);
    }
}
