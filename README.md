# PowerfulJS
An addon for adding multiple features to the KubeJS. However, the mod is only for testing purposes, and will never be publicly released, or have any identical features to KubeJS itself.

Currently supports Enchantments and MobEffects:

```javascript
onEvent("enchantment.registry", event => {
	event.create("vector", "pjs_basic")
		.rarity(EnchantmentRarity.COMMON)
		.onPostAttack(attackInfo => {
			var victim = attackInfo.victim;
			victim.addMotion(0, 1, 0);
		});
})
```

![2B7ABCFC916E252EABD5C92A7AC366A6](https://user-images.githubusercontent.com/24620047/161933928-ef479413-1fa0-48e2-8f75-2ece17df5e2e.gif)

```javascript
onEvent("mob_effect.registry", event => {
	event.create("fast", "pjs_basic")
		.color(Color.WHITE)
		.addModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070640", 3, ModifyOperation.ADDITION)
})
```

![d](https://user-images.githubusercontent.com/24620047/161934005-6e24d48e-0ffa-4522-8891-636620de9e2b.gif)
