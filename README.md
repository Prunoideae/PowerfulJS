# PowerfulJS
An addon for adding multiple features to the KubeJS. However, the mod is only for testing purposes, and will never be publicly released, or have any identical features to KubeJS itself.

Currently supports attaching (multiple) capabilities to an Item:

```javascript
onEvent('item.registry', event => {
    event.create("super_capacitor", "capability_basic")
        .addCapability((itemstack, nbt) => new EnergyItemStack(itemstack, 10000))
        .addCapability((itemstack, nbt) => new FluidHandlerItemStack(itemstack, 10000))
})
```
![d1](https://user-images.githubusercontent.com/24620047/163082667-779b0cb4-8612-4aa8-bbf3-bf337f428e0e.gif)
![d2](https://user-images.githubusercontent.com/24620047/163083066-4372d91c-4d12-49f9-ab29-0c0e1612ddd7.gif)
