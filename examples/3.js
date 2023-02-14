//In startup_scripts

StartupEvents.registry("block", event => {
	event.create("sussy_dynamo", "powerfuljs:dummy_block_entity")
		.attachCapability(CapabilityBuilder.ENERGY.customBlockEntity()
			.canExtract(() => true)
			.canReceive(() => true)
			.extractEnergy((be, amount, simulate) => {
				let energy = be.persistentData.getInt("energy")
				let extracted = Math.min(energy, amount)
				if (!simulate) {
					be.persistentData.putInt("energy", energy - extracted)
				}
				return extracted
			})
			.receiveEnergy((be, amount, simulate) => {
				let energy = be.persistentData.getInt("energy")
				let received = Math.min(1919810 - energy, amount)
				if (!simulate) {
					be.persistentData.putInt("energy", energy + received)
				}
				return received

			})
			.getEnergyStored(be => {
				return be.persistentData.getInt("energy")
			})
			.getMaxEnergyStored(() => 1919810)
		)
})