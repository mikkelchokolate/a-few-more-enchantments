# A Few More Enchantments

A Fabric mod that adds several useful enchantments to Minecraft.

## Enchantments

| Enchantment | Max Level | Applicable To | Description |
|---|---|---|---|
| **Soulbound** | I | Any item with durability | Items are kept in your inventory on death |
| **Vein Mining** | III | Pickaxes | Mines connected ore blocks of the same type |
| **Lumberjack** | III | Axes | Chops down connected logs of the same tree |
| **Magnetism** | III | Armor (chestplate primary) | Attracts nearby dropped items toward you |
| **Harvesting** | I | Hoes | Right-click mature crops to harvest and replant |
| **Area Mining** | IV | Pickaxes | L1=3x3, L2=5x5, L3=7x7, L4=9x9 area mining |
| **Depth Mine** | III | Pickaxes | Mines additional blocks in depth (level + 1 deep) |

## Requirements

- Minecraft 1.21+
- [Fabric Loader](https://fabricmc.net/) >= 0.15.11
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [MCPitanLib](https://modrinth.com/mod/mcpitanlibarch)

## Installation

1. Install Fabric Loader and Fabric API
2. Install MCPitanLib and Architecture API
3. Place the mod JAR file in your `mods` folder

## Building

```bash
./gradlew build
```

The built JAR will be in `build/libs/`.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
