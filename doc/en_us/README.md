[简体中文](../../README.md) | [English](README.md)

# Create: Slide Rail
#### An experimental mod that adds simple Guide Rails, Slide Blocks, and Belt Fixing Clips for Create: Aeronautics.

---

## The following information is based on the latest version. Please verify in-game for actual behavior.

### Overview:

| Block               | Description                                                                                     |
|---------------------|-------------------------------------------------------------------------------------------------|
| `guide_rail`        | Provides a stable moving environment for `slide_block`.                                         |
| `slide_block`       | Utilizes Sable's tags to achieve low friction and moves within the stable <br/> environment provided by `guide_rail`. |
| `belt_fixing_clip`  | Uses a constraint to bind its parent sub-dimension to the surface of a belt.                   |

### Usage:

| Block               | Description                                                                                                               |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------|
| `guide_rail`        | Can be placed directly.                                                                                                     |
| `slide_block`       | Can be placed directly. To move it into a `guide_rail`, it must be assembled as a sub-dimension first, then moved into it. |
| `belt_fixing_clip`  | Hold the item and right-click on a belt surface to instantly generate a sub-dimension carrying this block and establish the constraint. |

### Current Limitations:

| Block               | Description                                                                                                               |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------|
| `guide_rail`        | Composite blocks are not yet implemented (i.e., guide rails and slide blocks combined into a single block that can directly assemble structures on the slide block). |
| `slide_block`       | Same as `guide_rail`.                                                                                                       |
| `belt_fixing_clip`  | Currently, only the functionality on the horizontal top surface is implemented and *potentially* working as intended.     |

### Dependencies:

| Mod Name | Purpose                                                                 |
|----------|-------------------------------------------------------------------------|
| Create   | Provides the target `belt` block required by `belt_fixing_clip`.        |
| Sable    | The core foundation for physics simulation.                             |

[//]: # (| Create: Aeronautics | Source for composite block assembly tools |)