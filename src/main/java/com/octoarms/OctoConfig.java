package com.octoarms;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Common (server-authoritative) configuration for every mech system.
 */
public final class OctoConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue PU_PER_BURN_TICK;
    public static final ForgeConfigSpec.IntValue CHARGER_TRANSFER_RATE;
    public static final ForgeConfigSpec.IntValue ASSEMBLER_DRAIN_RATE;
    public static final ForgeConfigSpec.IntValue ASSEMBLER_BUFFER;

    public static final ForgeConfigSpec.DoubleValue CLIMB_SPEED;
    public static final ForgeConfigSpec.DoubleValue CLIMB_COST_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue FLIGHT_COST_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue SPRINT_COST_MULTIPLIER;

    public static final ForgeConfigSpec.IntValue PLASMA_DAMAGE;
    public static final ForgeConfigSpec.IntValue RAILGUN_DAMAGE;
    public static final ForgeConfigSpec.IntValue PLASMA_BLADE_BONUS;

    public static final ForgeConfigSpec.BooleanValue ALLOW_FLIGHT;
    public static final ForgeConfigSpec.BooleanValue ALLOW_CLIMBING;
    public static final ForgeConfigSpec.BooleanValue CELLS_FEED_INVENTORY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Energy / fuel").push("energy");
        PU_PER_BURN_TICK = builder.comment("Power Units generated per tick of fuel burn time in machines.")
                .defineInRange("puPerBurnTick", 3, 1, 1000);
        CHARGER_TRANSFER_RATE = builder.comment("PU per tick moved from the dock buffer into the item being charged.")
                .defineInRange("chargerTransferRate", 400, 1, 100000);
        ASSEMBLER_DRAIN_RATE = builder.comment("PU per tick consumed by the mech assembler while crafting.")
                .defineInRange("assemblerDrainRate", 240, 1, 100000);
        ASSEMBLER_BUFFER = builder.comment("Internal PU buffer of the mech assembler.")
                .defineInRange("assemblerBuffer", 400000, 1000, 10000000);
        builder.pop();

        builder.comment("Locomotion").push("locomotion");
        CLIMB_SPEED = builder.comment("Base blocks-per-tick climbing speed (scaled by surface grip).")
                .defineInRange("climbSpeed", 0.11D, 0.01D, 1.0D);
        CLIMB_COST_MULTIPLIER = builder.comment("Multiplier applied to the climbing charge cost.")
                .defineInRange("climbCostMultiplier", 1.0D, 0.0D, 100.0D);
        FLIGHT_COST_MULTIPLIER = builder.comment("Multiplier applied to the hover-thruster charge cost.")
                .defineInRange("flightCostMultiplier", 1.0D, 0.0D, 100.0D);
        SPRINT_COST_MULTIPLIER = builder.comment("Multiplier applied to the servo-sprint charge cost.")
                .defineInRange("sprintCostMultiplier", 1.0D, 0.0D, 100.0D);
        builder.pop();

        builder.comment("Weapons").push("weapons");
        PLASMA_DAMAGE = builder.comment("Damage of integrated/arm-cannon plasma bolts.")
                .defineInRange("plasmaDamage", 9, 1, 1000);
        RAILGUN_DAMAGE = builder.comment("Damage of a fully charged rail slug.")
                .defineInRange("railgunDamage", 22, 1, 1000);
        PLASMA_BLADE_BONUS = builder.comment("Bonus damage of the plasma blade while charged.")
                .defineInRange("plasmaBladeBonus", 6, 0, 1000);
        builder.pop();

        builder.comment("Features").push("features");
        ALLOW_FLIGHT = builder.comment("Allow hover thrusters on diamond/netherite suits.")
                .define("allowFlight", true);
        ALLOW_CLIMBING = builder.comment("Allow wall crawling on the Spider Mech.")
                .define("allowClimbing", true);
        CELLS_FEED_INVENTORY = builder.comment("Power cells in the inventory slowly feed equipped suits.")
                .define("cellsFeedInventory", true);
        builder.pop();

        SPEC = builder.build();
    }

    private OctoConfig() {
    }
}
