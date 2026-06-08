
"use strict";

let scriptName = "packages.js"
let modName = "tantros"

const tantrosModPackages = new Packages(Vars.mods.getMod(modName).loader);


importPackage(tantrosModPackages.tantros);
importPackage(tantrosModPackages.tantros.ai);
importPackage(tantrosModPackages.tantros.ai.spawn);
importPackage(tantrosModPackages.tantros.ai.types);
importPackage(tantrosModPackages.tantros.content);
importPackage(tantrosModPackages.tantros.content.blocks);
importPackage(tantrosModPackages.tantros.content.planets);
importPackage(tantrosModPackages.tantros.content.recipes);
importPackage(tantrosModPackages.tantros.content.world);
importPackage(tantrosModPackages.tantros.content.world.meta);
importPackage(tantrosModPackages.tantros.entities);
importPackage(tantrosModPackages.tantros.entities.abilities);
importPackage(tantrosModPackages.tantros.entities.abilities.burrow);
importPackage(tantrosModPackages.tantros.entities.comp);
importPackage(tantrosModPackages.tantros.entities.effect);
importPackage(tantrosModPackages.tantros.game);
importPackage(tantrosModPackages.tantros.graphics);
importPackage(tantrosModPackages.tantros.graphics.overlays);
importPackage(tantrosModPackages.tantros.input);
importPackage(tantrosModPackages.tantros.maps);
importPackage(tantrosModPackages.tantros.maps.planet);
importPackage(tantrosModPackages.tantros.mod);
importPackage(tantrosModPackages.tantros.net);
importPackage(tantrosModPackages.tantros.net.packets);
importPackage(tantrosModPackages.tantros.type);
importPackage(tantrosModPackages.tantros.type.blockConfig);
importPackage(tantrosModPackages.tantros.type.blockConfig.logic);
importPackage(tantrosModPackages.tantros.type.blockInput);
importPackage(tantrosModPackages.tantros.type.blockInput.util);
importPackage(tantrosModPackages.tantros.type.blockUtil);
importPackage(tantrosModPackages.tantros.type.buildConfig);
importPackage(tantrosModPackages.tantros.type.buildingState.logic);
importPackage(tantrosModPackages.tantros.type.effect);
importPackage(tantrosModPackages.tantros.type.effect.logic);
importPackage(tantrosModPackages.tantros.type.effect.projector);
importPackage(tantrosModPackages.tantros.type.effect.projector.range);
importPackage(tantrosModPackages.tantros.type.production);
importPackage(tantrosModPackages.tantros.type.units);
importPackage(tantrosModPackages.tantros.ui);
importPackage(tantrosModPackages.tantros.util);
importPackage(tantrosModPackages.tantros.util.io);
importPackage(tantrosModPackages.tantros.world);
importPackage(tantrosModPackages.tantros.world.blocks);
importPackage(tantrosModPackages.tantros.world.blocks.defense);
importPackage(tantrosModPackages.tantros.world.blocks.defense.modifiers);
importPackage(tantrosModPackages.tantros.world.blocks.distribution);
importPackage(tantrosModPackages.tantros.world.blocks.distribution.liquidTransport);
importPackage(tantrosModPackages.tantros.world.blocks.distribution.payload);
importPackage(tantrosModPackages.tantros.world.blocks.drill);
importPackage(tantrosModPackages.tantros.world.blocks.drill.deep);
importPackage(tantrosModPackages.tantros.world.blocks.effect);
importPackage(tantrosModPackages.tantros.world.blocks.effect.projector);
importPackage(tantrosModPackages.tantros.world.blocks.effect.projector.draw);
importPackage(tantrosModPackages.tantros.world.blocks.environment);
importPackage(tantrosModPackages.tantros.world.blocks.logic);
importPackage(tantrosModPackages.tantros.world.blocks.payload);
importPackage(tantrosModPackages.tantros.world.blocks.power);
importPackage(tantrosModPackages.tantros.world.blocks.production);
importPackage(tantrosModPackages.tantros.world.blocks.turrets);
importPackage(tantrosModPackages.tantros.world.blocks.units);
importPackage(tantrosModPackages.tantros.world.blocks.units.unitAssembly);
importPackage(tantrosModPackages.tantros.world.consumers);
importPackage(tantrosModPackages.tantros.world.draw);
importPackage(tantrosModPackages.tantros.world.draw.extended);
importPackage(tantrosModPackages.tantros.world.draw.output);
importPackage(tantrosModPackages.tantros.world.draw.util);
importPackage(tantrosModPackages.tantros.world.draw.wallDrill);
importPackage(tantrosModPackages.tantros.world.environment);
importPackage(tantrosModPackages.tantros.world.meta);