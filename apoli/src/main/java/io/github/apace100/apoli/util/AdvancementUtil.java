//package io.github.apace100.apoli.util;
//
//import io.github.apace100.apoli.mixin.AdvancementCommandAccessor;
//import net.minecraft.advancement.AdvancementEntry;
//import net.minecraft.advancement.AdvancementManager;
//import net.minecraft.advancement.PlacedAdvancement;
//import net.minecraft.server.command.AdvancementCommand;
//import net.minecraft.server.level.ServerPlayer;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class AdvancementUtil {
//
//    public static List<AdvancementEntry> selectEntries(AdvancementManager advancementManager, AdvancementEntry advancementEntry, AdvancementCommand.Selection selection) {
//
//        PlacedAdvancement placedAdvancement = advancementManager.get(advancementEntry);
//        if (placedAdvancement == null) {
//            return List.of(advancementEntry);
//        }
//
//        List<AdvancementEntry> advancementEntries = new ArrayList<>();
//        if (selection.before) {
//
//            for (PlacedAdvancement parent = placedAdvancement.getParent(); parent != null; parent = parent.getParent()) {
//                advancementEntries.add(parent.getAdvancementEntry());
//            }
//
//        }
//
//        advancementEntries.add(advancementEntry);
//        if (selection.after) {
//            AdvancementCommandAccessor.callAddChildrenRecursivelyToList(placedAdvancement, advancementEntries);
//        }
//
//        return advancementEntries;
//
//    }
//
//    public static void processCriteria(AdvancementEntry advancementEntry, Collection<String> criteria, AdvancementCommand.Operation operation, ServerPlayer serverPlayer) {
//        for (String criterion : criteria.stream().filter(c -> advancementEntry.value().criteria().containsKey(c)).toList()) {
//            operation.processEachCriterion(serverPlayer, advancementEntry, criterion);
//        }
//    }
//
//    public static void processAdvancements(Collection<AdvancementEntry> advancementEntries, AdvancementCommand.Operation operation, ServerPlayer serverPlayer) {
//        for (AdvancementEntry advancementEntry : advancementEntries) {
//            operation.processEach(serverPlayer, advancementEntry);
//        }
//    }
//
//}
