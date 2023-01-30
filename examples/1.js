//In startup_scripts

CapabilityEvents.blockEntity(event => {
	event.attach("thermal:energy_cell",
		BotaniaCapabilityBuilder.MANA.blockEntity()
			.receiveMana((be, amount) => {
				let energy = be.getCapability(ForgeCapabilities.ENERGY).orElse(null)
				energy.receiveEnergy(amount, false)
			})
			.getCurrentMana(be => {
				let energy = be.getCapability(ForgeCapabilities.ENERGY).orElse(null)
				return energy.energyStored
			})
			.isFull(be => {
				let energy = be.getCapability(ForgeCapabilities.ENERGY).orElse(null)
				return energy.energyStored >= energy.maxEnergyStored;
			})
	)

})