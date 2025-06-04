package me.supersanta.essential_addons.utils

import me.supersanta.essential_addons.EssentialAddons
import me.supersanta.essential_addons.feature.subscription.EssentialSubscription
import me.supersanta.essential_addons.feature.subscription.EssentialSubscriptions
import net.casual.arcade.utils.registries.RegistryKeySupplier
import net.casual.arcade.utils.registries.RegistrySupplier

object EssentialRegistryKeys: RegistryKeySupplier(EssentialAddons.MOD_ID) {
    val SUBSCRIPTION = this.create<EssentialSubscription>("subscription")
}

object EssentialRegistries: RegistrySupplier() {
    val SUBSCRIPTION = this.create(EssentialRegistryKeys.SUBSCRIPTION) { EssentialSubscriptions.load() }
}