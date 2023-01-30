//In startup_scripts

StartupEvents.registry('item', event => {
	event.create("test")
		.maxDamage(1000)
		.attachCapability(
			CapabilityBuilder.ENERGY.customItemStack()
				.canExtract(i => true)
				.getEnergyStored(i => 1000)
				.getMaxEnergyStored(i => 1000)
				.extractEnergy((itm, i, sim) => {
					if (!sim) {
						//itm.damageValue += 1
						//if (itm.damageValue >= 1000)
						//	itm.shrink(1)
					}
					return 20
				})
		)
})